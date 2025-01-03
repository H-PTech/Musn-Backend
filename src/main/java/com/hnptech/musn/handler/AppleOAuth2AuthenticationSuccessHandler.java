package com.hnptech.musn.handler;

import com.hnptech.musn.entity.User;
import com.hnptech.musn.repository.UserRepository;
import com.hnptech.musn.util.NicknameGenerator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AppleOAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final NicknameGenerator nicknameGenerator;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // OAuth2 제공자 정보 추출
        String email = (String) oAuth2User.getAttributes().get("email"); // 예: 애플 로그인에서 이메일 가져오기
        String name = (String) oAuth2User.getAttributes().get("name");

        // (email == providerId) 이메일과 제공자로 존재 여부 확인
        boolean userExists = userRepository.existsByProviderIdAndProvider(email, "apple");
        if (!userExists) {
            User newUser = User.builder()
                    .providerId(email)
                    .provider("apple")
//                    .name(name)
                    .nickname(nicknameGenerator.generateNickname())
                    .build();
            userRepository.save(newUser);
        }

        // 로그인 성공 리다이렉트(스프링 시큐리티 defaultSuccessUrl 부분과 무슨 차이?)
        response.sendRedirect("/main");
    }
}
