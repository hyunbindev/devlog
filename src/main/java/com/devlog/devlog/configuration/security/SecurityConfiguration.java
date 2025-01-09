package com.devlog.devlog.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final String[] publicURL ={"/","/login"};
    private final String[] devURL ={"/h2-console/*"};

    private Oauth2successHandler oauth2successHandler;
    @Autowired
    public SecurityConfiguration(Oauth2successHandler oauth2successHandler){
        this.oauth2successHandler = oauth2successHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(this::setAuthorizeMatcher)
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Login(this::setOauth2)
                .headers((headers)->headers.frameOptions((HeadersConfigurer.FrameOptionsConfig::disable)));
        return http.build();
    }
    //접근 권환 설정
    private void setAuthorizeMatcher(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry){
        registry.requestMatchers(publicURL).permitAll()
                //개발과정 접근 허용
                .requestMatchers(devURL).permitAll()
                //이외 인증필요
                .anyRequest().authenticated();
    }
    //Oauth2 설정
    private void setOauth2(OAuth2LoginConfigurer<HttpSecurity> oauth2){
        oauth2.loginPage("/oauth2/authorization/github");
        oauth2.successHandler(oauth2successHandler);
    }
}