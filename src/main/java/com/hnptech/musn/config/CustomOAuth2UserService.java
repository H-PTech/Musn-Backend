package com.hnptech.musn.config;

import com.hnptech.musn.entity.User;
import com.hnptech.musn.entity.enums.StreamingApp;
import com.hnptech.musn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) {
    OAuth2User oAuth2User = super.loadUser(userRequest);
    String provider = userRequest.getClientRegistration().getRegistrationId();
    String providerId = oAuth2User.getName();

    User user = getOrSave(provider, providerId);
    Map<String, Object> attributes = oAuth2User.getAttributes();

    return new CustomUserDetails(user, attributes);
  }

  private User getOrSave(String provider, String providerId) {
    Optional<User> user = userRepository.findByProviderAndProviderId(provider, providerId);

    if (user.isEmpty()) {
      User newUser = User.builder()
          .provider(provider)
          .providerId(providerId)
          .nickname(createRandomNickname())
          .streamingApp(StreamingApp.SPOTIFY)
          .isPush(true)
          .build();

      return userRepository.save(newUser);
    }
    return user.get();
  }

  private String createRandomNickname() {
    Random random = new Random();
    String newNickname;

    do {
      newNickname = "익명" + (random.nextInt(900000) + 100000);
    } while (userRepository.findByNickname(newNickname).isPresent());

    return newNickname;
  }
}


