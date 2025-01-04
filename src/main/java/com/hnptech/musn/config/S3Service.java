package com.hnptech.musn.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

@Service
public class S3Service {
  private final S3Client s3Client;
  private final String filePrefix = "https://musn.s3.ap-northeast-2.amazonaws.com/";
  @Value("${aws.s3.bucket}")
  private String bucketName;

  public S3Service(S3Client s3Client) {
    this.s3Client = s3Client;
  }

  public String uploadFile(MultipartFile file) throws IOException {
    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(fileName)
            .contentType(file.getContentType())
            .build();

    try (InputStream inputStream = file.getInputStream()) {
      PutObjectResponse response = s3Client.putObject(putObjectRequest,
              software.amazon.awssdk.core.sync.RequestBody.fromInputStream(inputStream, file.getSize()));

      if (response.sdkHttpResponse().isSuccessful()) {
        return filePrefix+fileName;
      } else {
        throw new RuntimeException("파일 업로드 실패");
      }
    }
  }
}
