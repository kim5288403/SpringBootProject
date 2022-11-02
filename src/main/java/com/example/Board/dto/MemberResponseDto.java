package com.example.Board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MemberResponseDto {
	private String name;

	private String email;

	private String password;

	private String phone;

	private String gender;

	private String address;

	@Builder
	public MemberResponseDto(MemberRequestDto memberRequestDto) {
		this.name = memberRequestDto.getName();
		this.email = memberRequestDto.getEmail();
		this.password = memberRequestDto.getPassword();
		this.phone = memberRequestDto.getPhone();
		this.gender = memberRequestDto.getGender();
		this.address = memberRequestDto.getAddress();
	}

}
