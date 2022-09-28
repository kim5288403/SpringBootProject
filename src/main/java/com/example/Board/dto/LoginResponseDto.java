package com.example.Board.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LoginResponseDto {
	private String email;
	
	private String token;

	private String Refreshtoken;
	
	@Builder
    public LoginResponseDto(String email, String token, String Refreshtoken) {
        this.email = email;
        this.token = token;
        this.Refreshtoken = Refreshtoken;
    }
}
