package com.devlog.devlog.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PageService {
	public Pageable getDefaultPageable(int page) {
		return PageRequest.of(page, 10, Sort.by("id").descending());
	}
}
