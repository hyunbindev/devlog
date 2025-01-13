package com.devlog.devlog.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "member")
public class MemberEntity {
    @Id
    private String id;
    private String name;
    private String bio;
    private String email;
    private String avatar_url;
    
    @OneToMany
    private List<BoardEntity> boards = new ArrayList<>();
}