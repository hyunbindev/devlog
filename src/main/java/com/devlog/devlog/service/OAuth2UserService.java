package com.devlog.devlog.service;

import com.devlog.devlog.data.dao.impl.MemberDAOImpl;
import com.devlog.devlog.data.dto.CustomOAuth2User;
import com.devlog.devlog.data.dto.MemberDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
@Slf4j
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final MemberDAOImpl memberDAO;
    @Autowired
    public OAuth2UserService(MemberDAOImpl memberDAO){
        this.memberDAO = memberDAO;
    }
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userReq) throws OAuth2AuthenticationException{
        OAuth2User oAuth2User = super.loadUser(userReq);
        Map<String,Object> attributes = oAuth2User.<Map<String,Object>>getAttributes();
        MemberDTO dto = new MemberDTO(attributes);
        updateMember(dto);
        return new CustomOAuth2User(dto);
    }
    public void updateMember(MemberDTO dto){
        memberDAO.addMember(dto);
    }
}
