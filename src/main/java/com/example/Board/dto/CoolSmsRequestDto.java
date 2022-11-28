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
	private String phone;

	@NotNull
	private String verificationCode;
	
	public static CoolSms create(String phone, String verificationCode) {
		return CoolSms.builder()
				.phone(phone)
				.verificationCode(verificationCode)
				.build();
	}
	
}
