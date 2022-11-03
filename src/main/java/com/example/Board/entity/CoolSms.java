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
	private String phone; //pk
	
	private String verificationCode; //인증번호
	
	private LocalDateTime sendDate = LocalDateTime.now(); //전송시간
	
	@Builder
	public CoolSms (String phone, String verificationCode) {
		this.phone = phone;
		this.verificationCode = verificationCode;
	}
	
	
}
