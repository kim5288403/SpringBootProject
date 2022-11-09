package com.example.Board.dto;

import com.example.Board.entity.CoolSms;
import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CoolSmsRequestDto {
	@NotNull
	private String phone; //pk

	@NotNull
	private String verificationCode; //인증번호
	
	public static CoolSms create(String phone, String verificationCode) {
		return CoolSms.builder()
				.phone(phone)
				.verificationCode(verificationCode)
				.build();
	}
	
}
