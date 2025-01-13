package com.devlog.devlog.data.dto;

import com.devlog.devlog.domain.MemberEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Map;
import java.util.Objects;

@Getter
@Setter
public class MemberDTO {
    private boolean isMember;
    private boolean isOwner;
    private String id;
    private String name;
    private String bio;
    private String email;
    private String avatar_url;

    public MemberDTO(Map<String, Object> attributes){
        this.id = (String)attributes.get("node_id");
        this.name = (String)attributes.get("name");
        this.bio = (String)attributes.get("bio");
        this.email = (String)attributes.get("email");
        this.avatar_url = (String)attributes.get("avatar_url");
    }

    public MemberDTO(){
        this.id = null;
        this.name = null;
        this.bio = null;
        this.email = null;
        this.avatar_url = null;
    }
}