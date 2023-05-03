package com.korea.triplocation.api.controller;

import com.korea.triplocation.api.dto.request.LoginReqDto;
import com.korea.triplocation.api.dto.request.UserReqDto;
import com.korea.triplocation.api.dto.response.DataRespDto;
import com.korea.triplocation.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	private final UserService userService;

    @PostMapping("/user")
    public ResponseEntity<?> signup(@RequestBody UserReqDto userReqDto) {
    	System.out.println(userReqDto.getEmail() + "\t" + userReqDto.getPassword());
    	userService.signup(userReqDto);
        return ResponseEntity.ok(DataRespDto.ofDefault());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(LoginReqDto loginReqDto) {
        return ResponseEntity.ok(DataRespDto.of(null));
    }
}