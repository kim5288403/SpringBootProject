package com.example.Board.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CoolSmsRepository extends JpaRepository<CoolSms, String>{
	public List<CoolSms> findByPhoneAndVerificationCode(String phone, String verification_code);
}
