package com.devlog.devlog.data.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VisitorLogHistoryDTO {
	private LocalDate date;
	private int visitorCount;
	
	public VisitorLogHistoryDTO(LocalDate date) {
		this.date = date;
		this.visitorCount = 1;
	}
	public void incrementCount() {
		this.visitorCount++;
	}
}