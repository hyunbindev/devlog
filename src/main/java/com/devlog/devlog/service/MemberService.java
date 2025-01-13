package com.devlog.devlog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devlog.devlog.data.dao.impl.MemberDAOImpl;
import com.devlog.devlog.data.dto.MemberDTO;

@Service
public class MemberService{
	private final MemberDAOImpl memberDAOImpl;
	@Autowired
	public MemberService(MemberDAOImpl memberDAOImpl) {
		this.memberDAOImpl = memberDAOImpl;
	}
	public MemberDTO getMember(MemberDTO dto) {
		return memberDAOImpl.getMember(dto);
	}
}
