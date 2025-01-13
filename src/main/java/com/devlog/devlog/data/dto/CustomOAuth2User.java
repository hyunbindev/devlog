package com.devlog.devlog.data.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.lang.reflect.Member;
import java.util.*;

public class CustomOAuth2User implements OAuth2User {
    private final Map<String,Object> attributes;
    public CustomOAuth2User(MemberDTO memberDTO){
        attributes = new HashMap<>();
        attributes.put("id",memberDTO.getId());
        attributes.put("name",memberDTO.getName());
        attributes.put("bio",memberDTO.getBio());
        attributes.put("email",memberDTO.getEmail());
        attributes.put("avatar_url",memberDTO.getAvatar_url());
    }
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayDeque<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "member";
            }
        });
        return collection;
    }

    @Override
    public String getName() {
        return (String)attributes.get("id");
    }
}