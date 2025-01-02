package com.hnptech.musn.config;

import com.hnptech.musn.filter.JwtFilter;
import com.hnptech.musn.handler.OAuth2MemberSuccessHandler;
import com.hnptech.musn.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.client.oidc.authentication.OidcAuthorizationCodeAuthenticationProvider;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/*
*  Spring Security
*
* */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfig {

  private final JwtUtil jwtUtil;
  private final JwtFilter jwtFilter;

  @Bean
  protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
    http.cors(cors -> cors.configurationSource(corsConfigrationSource()))
        .csrf(CsrfConfigurer::disable)
        .httpBasic(HttpBasicConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(request -> request
            .requestMatchers("v1/auth/login/**", "/v1/users/**", "/v1/drop/**").permitAll() // 인증없이 허용 주소
            .anyRequest()
            .authenticated())
//        .authorizeHttpRequests(authorize -> authorize
//            .anyRequest().permitAll()
//        )
        .oauth2Login(oauth2 -> oauth2
            .successHandler(new OAuth2MemberSuccessHandler(jwtUtil))
        )
        .exceptionHandling(exception -> exception
            // 인증되지 않은 사용자 처리 (401)
            .authenticationEntryPoint((request, response, authException) -> {
              response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 403 응답
              response.getWriter().write("Access Denied");
            })
        )
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

//                .exceptionHandling(exception -> exception.accessDeniedPage("/403"));

    return http.build();
  }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient(
            OAuth2AuthorizationCodeGrantRequestEntityConverter oAuth2AuthorizationCodeGrantRequestEntityConverter
    ) {
        var client = new DefaultAuthorizationCodeTokenResponseClient();
        client.setRequestEntityConverter(oAuth2AuthorizationCodeGrantRequestEntityConverter);
        return client;
    }

    @Bean
    public OidcAuthorizationCodeAuthenticationProvider auth2AuthorizationCodeAuthenticationProvider(
            OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient
    ) {
        return new OidcAuthorizationCodeAuthenticationProvider(accessTokenResponseClient, new OidcUserService());
    }

    @Bean
    protected CorsConfigurationSource corsConfigrationSource() {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
