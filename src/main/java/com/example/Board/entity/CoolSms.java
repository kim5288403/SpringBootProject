package com.example.Board.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class CoolSms {
	
	@Id
	private String phone;
	
	private String verificationCode;
	
	private LocalDateTime sendDate = LocalDateTime.now();
	
	@Builder
	public CoolSms (String phone, String verificationCode) {
		this.phone = phone;
		this.verificationCode = verificationCode;
	}
	
	
}
