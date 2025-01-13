package com.devlog.devlog.data.dao;

import com.devlog.devlog.domain.MemberEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface CommonDAO<Entity,DTO> {
    DTO entityToDTO(Entity entity);
    Entity dtoToEntity(DTO dto);
}