package com.example.Board.model;

import java.util.HashMap;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

@Service
@Transactional
@RequiredArgsConstructor
public class SmsService {
	
	public String pushMessage(String to) throws CoolsmsException {
		String api_key = "NCSWIIVMQVPJZEMF";
		String api_secret = "F4RV6AZYFTRNLJSVIP1ECKJWGWHAH498";
		Message message = new Message(api_key, api_secret);
		
		Random rand  = new Random();
	    String numStr = "";
	    for(int i=0; i<4; i++) {
	       String ran = Integer.toString(rand.nextInt(10));
	       numStr+=ran;
	    }        
		
	    HashMap<String, String> params = new HashMap<String, String>();
	    params.put("to", to);
	    params.put("from", "01073342383");
	    params.put("type", "sms");
	    params.put("text", "인증번호는 [" + numStr + "] 입니다.");
	    
	    message.send(params);
	    
	    return numStr;
		
	}
}
