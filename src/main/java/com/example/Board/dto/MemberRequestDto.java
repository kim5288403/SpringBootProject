package com.example.Board.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.Board.entity.Member;
import com.example.Board.enums.Gender;
import com.example.Board.enums.MemberRole;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MemberRequestDto {

	@NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 4, max = 16, message = "비밀번호는 4자 이상, 16자 이하로 입력해주세요.")
    private String password;
    
    @NotEmpty(message = "전화번호는 필수 입력 값입니다.")
    private String phone;
    
    private String gender;
    
    @NotEmpty(message = "주소는 필수 입력 값입니다.")
    private String address;

    @Builder
    public MemberRequestDto(String name, String email, String password, String phone, String address, String gender) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.gender = gender;
        this.address = address;
    }

    public static Member create (MemberRequestDto memberDto, PasswordEncoder passwordEncoder) {
    	return Member.builder()
                .name(memberDto.getName())
                .email(memberDto.getEmail())
                .gender(memberDto.getGender().equals("남") ? Gender.남 : Gender.여)
                .address(memberDto.getAddress())
                .password(passwordEncoder.encode(memberDto.getPassword()))  //암호화처리
                .phone(memberDto.getPhone())
                .role(MemberRole.USER)
                .build();
    }
}
