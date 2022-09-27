package com.example.Board.dto;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LoginDto {
	@NotNull
	private String email;
	
	@NotNull
	private String password;
	
	private String token;
}
