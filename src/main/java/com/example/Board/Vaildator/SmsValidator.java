package com.example.Board.vaildator;

import java.time.LocalDateTime;

import javax.validation.ValidationException;

import com.example.Board.entity.CoolSms;
import com.example.Board.entity.CoolSmsRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SmsValidator {
	private final CoolSmsRepository coolSmsRepository;
	
	public void validateDuplicatePhone(String phone) {
		if (phone.equals("")) {
			throw new ValidationException("전화번호는 필수 값입니다.");
		}
	}
	
	public void validateDuplicateCode(String code) {
		if(code.equals("")) {
			throw new ValidationException("인증번호는 필수 값입니다.");
		}
	}
	
	public CoolSms validateDuplicateCoolSms(String phone, String code) {
		CoolSms coolSms = coolSmsRepository.findByPhoneAndVerificationCode(phone, code);
		
		if (coolSms == null) {
			throw new IllegalStateException("존재하지 않은 인증번호 혹은 전화번호입니다.");
		}
		
		return coolSms;
	}
	
	public void validateDuplicateSendDate(String sendDate) {
		LocalDateTime Before = LocalDateTime.parse(sendDate);
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime After = Before.plusMinutes(3);
		
		if (!now.isAfter(Before) || !now.isBefore(After)) {
			throw new IllegalStateException("인증번호 확인 시간이 유효하지않습니다.");
		}
	}

}
