package com.devlog.devlog.service;

import com.devlog.devlog.data.dto.CustomOAuth2User;
import com.devlog.devlog.data.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
    
    private final MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userReq) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userReq);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        
        MemberDTO dto = new MemberDTO(attributes);
        return new CustomOAuth2User(dto);
    }
}
