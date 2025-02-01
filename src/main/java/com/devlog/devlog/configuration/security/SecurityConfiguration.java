package com.devlog.devlog.configuration.security;

import com.devlog.devlog.configuration.security.jwtFilter.filter.ValidateAccessToken;
import com.devlog.devlog.service.OAuth2UserService;
import com.devlog.devlog.utils.JwtTokenProvider;

import java.util.Arrays;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final String[] publicURL ={"/journal/**","/css/*","/js/*"};
    private final String[] devURL ={"/h2-console/*"};

    private final OAuth2UserService oauth2UserService;
    private final Oauth2successHandler oauth2successHandler;
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    public SecurityConfiguration(OAuth2UserService oauth2UserService , Oauth2successHandler oauth2successHandler , JwtTokenProvider jwtTokenProvider){
        this.oauth2UserService = oauth2UserService;
        this.oauth2successHandler = oauth2successHandler;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(this::setAuthorizeMatcher)
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Login(this::setOauth2)
                .headers((headers)->headers.frameOptions((HeadersConfigurer.FrameOptionsConfig::disable)));
        /*
         * 세션으로 전환
        http.addFilterBefore (new ValidateAccessToken(jwtTokenProvider,passFilterURL()), OAuth2LoginAuthenticationFilter.class);

        http.sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        */
        return http.build();
    }
    //접근 권환 설정
    private void setAuthorizeMatcher(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry){
    	registry.requestMatchers(publicURL).permitAll()
        		.requestMatchers(devURL).permitAll()
        		.anyRequest().authenticated();
    }
    //Oauth2 설정
    private void setOauth2(OAuth2LoginConfigurer<HttpSecurity> oauth2){
        oauth2.loginPage("/oauth2/authorization/github");
        oauth2.userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig.userService(oauth2UserService)));
        oauth2.successHandler(oauth2successHandler);
    }
    //필터점프 url
    private String[] passFilterURL() {
    	  return Stream.concat(Arrays.stream(publicURL),Arrays.stream(devURL)).toArray(String[]::new);
    }
}