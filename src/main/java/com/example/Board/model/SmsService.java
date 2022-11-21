package com.example.Board.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Random;

import javax.validation.ValidationException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Board.dto.CoolSmsRequestDto;
import com.example.Board.dto.CoolSmsResponseDto;
import com.example.Board.entity.CoolSms;
import com.example.Board.entity.CoolSmsRepository;

import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

@Service
@Transactional
@RequiredArgsConstructor
public class SmsService {
	private final CoolSmsRepository coolSmsRepository;
	
	public String push(String to) throws CoolsmsException {
		validateDuplicateCheck(to);
		
		String api_key = "NCSWIIVMQVPJZEMF";
		String api_secret = "F4RV6AZYFTRNLJSVIP1ECKJWGWHAH498";
		Message message = new Message(api_key, api_secret);
		
		Random rand  = new Random();
	    String verificationCode = "";
	    for(int i=0; i<4; i++) {
	       String ran = Integer.toString(rand.nextInt(10));
	       verificationCode+=ran;
	    }        
		
	    HashMap<String, String> params = new HashMap<String, String>();
	    params.put("to", to);
	    params.put("from", "01073342383");
	    params.put("type", "sms");
	    params.put("text", "인증번호는 [" + verificationCode + "] 입니다.");
	    
	    //문자 발송 테스트 시 주석 해제
//	    message.send(params);
	    CoolSms coolSms = CoolSmsRequestDto.create(to, verificationCode);
	    coolSmsRepository.save(coolSms);
	    
	    return verificationCode;
	}
	
	public void validateDuplicateCheck(String to) {
		if (to.equals("")) {
			throw new ValidationException("전화번호는 필수 값입니다.");
		}
	}
	
	public CoolSmsResponseDto check (CoolSmsRequestDto request) {
		validateDuplicateCheck(request);
		
		CoolSms coolSms = coolSmsRepository.findByPhoneAndVerificationCode(request.getPhone(), request.getVerificationCode());
		
		validateDuplicateCoolSms(coolSms);
		
		return new CoolSmsResponseDto(coolSms.getPhone(), coolSms.getVerificationCode(), coolSms.getSendDate());
	}
	
	public void validateDuplicateCheck(CoolSmsRequestDto request) {
		if (request.getPhone().equals("")) {
			throw new ValidationException("전화번호는 필수 값입니다.");
		}
		
		if(request.getVerificationCode().equals("")) {
			throw new ValidationException("인증번호는 필수 값입니다.");
		}
	}
	
	//인증번호 확인 유효성검사
	public void validateDuplicateCoolSms(CoolSms coolsms) {
		if (coolsms == null) {
			throw new IllegalStateException("존재하지 않은 인증번호 혹은 전화번호입니다.");
		}
		
		LocalDateTime Before = LocalDateTime.parse(coolsms.getSendDate()+"");
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime After = Before.plusMinutes(3);
		if (!now.isAfter(Before) || !now.isBefore(After)) {
			throw new IllegalStateException("인증번호 확인 시간이 유효하지않습니다.");
		}
	}
			
}
