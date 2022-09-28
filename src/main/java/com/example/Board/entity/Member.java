package com.example.Board.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.Board.enums.Gender;
import com.example.Board.enums.MemberRole;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Table(name = "member")
@Entity
public class Member {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;
    
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    private String address;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Builder
    public Member (String name, String email, String password, String address, MemberRole role, Gender gender) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.address = address;
        this.role = role;
    }

}