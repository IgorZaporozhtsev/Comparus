package com.zeecoder.comparus.controller;

import com.zeecoder.comparus.domain.Users;
import com.zeecoder.comparus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserApiController implements UserApi{

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<Users>> retriveAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }
}
