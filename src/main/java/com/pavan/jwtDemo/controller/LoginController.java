package com.pavan.jwtDemo.controller;

import com.pavan.jwtDemo.dto.UserSignInResponseDto;
import com.pavan.jwtDemo.entity.UserEntity;
import com.pavan.jwtDemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/details")
    public ResponseEntity<String> getUSerDetails(){
        return new ResponseEntity("hello", HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserSignInResponseDto> createUser(@RequestBody UserEntity userDetails){
        return new ResponseEntity<>(userService.createUser(userDetails), HttpStatus.OK);
    }
}