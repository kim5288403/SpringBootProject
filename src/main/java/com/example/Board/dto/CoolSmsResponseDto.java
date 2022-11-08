package com.example.Board.dto;


import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CoolSmsResponseDto {
	private String phone; //pk

	private String verificationCode; //인증번호
	
	
	@Builder
	public CoolSmsResponseDto (String phone, String verificationCode) {
		this.phone = phone;
		this.verificationCode = verificationCode;
	}
}
