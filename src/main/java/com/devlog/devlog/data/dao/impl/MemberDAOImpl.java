package com.devlog.devlog.data.dao.impl;

import com.devlog.devlog.data.dao.CommonDAO;
import com.devlog.devlog.data.dto.MemberDTO;
import com.devlog.devlog.domain.MemberEntity;
import com.devlog.devlog.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public class MemberDAOImpl implements CommonDAO<MemberEntity , MemberDTO> {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberDAOImpl(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    public MemberDTO getMember(MemberDTO member) {
        Optional<MemberEntity> entity =memberRepository.findById(member.getId());
        if(entity.isEmpty()) {
        	MemberDTO dto = new MemberDTO();
        	dto.setMember(false);
        	return dto;
        }
        return entityToDTO(entity.get());
    }

    public void addMember(MemberDTO member) {
        MemberEntity entity = dtoToEntity(member);
        memberRepository.save(entity);
    }
    
    public MemberEntity getMemberEntity(MemberDTO member) {
    	Optional<MemberEntity> entity = memberRepository.findById(member.getId());
    	return entity.get();
    }
    
    @Override
    public MemberDTO entityToDTO(MemberEntity entity) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(entity.getId());
        memberDTO.setName(entity.getName());
        memberDTO.setBio(entity.getBio());
        memberDTO.setEmail(entity.getEmail());
        memberDTO.setAvatar_url(entity.getAvatar_url());
        return memberDTO;
    }

    @Override
    public MemberEntity dtoToEntity(MemberDTO memberDTO) {
        MemberEntity entity = new MemberEntity();
        entity.setId(memberDTO.getId());
        entity.setName(memberDTO.getName());
        entity.setBio(memberDTO.getBio());
        entity.setEmail(memberDTO.getEmail());
        entity.setAvatar_url(memberDTO.getAvatar_url());
        return entity;
    }
}
