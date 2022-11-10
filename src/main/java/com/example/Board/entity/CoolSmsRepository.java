package com.example.Board.entity;


import org.springframework.data.jpa.repository.JpaRepository;

public interface CoolSmsRepository extends JpaRepository<CoolSms, String>{
	public CoolSms findByPhoneAndVerificationCode(String phone, String verification_code);
}
