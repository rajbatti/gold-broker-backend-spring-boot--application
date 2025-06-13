package com.hugoserve.demo.controller;

import com.hugoserve.demo.proto.ApiResponse;
import com.hugoserve.demo.proto.AuthRequestDTO;
import com.hugoserve.demo.proto.UserDetailsDTO;
import com.hugoserve.demo.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody UserDetailsDTO user, HttpServletResponse response) {
        return userService.createUser(user, response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> authenticateUser(@RequestBody AuthRequestDTO user, HttpServletResponse response) {
        return userService.authenticateUser(user, response);
    }


}


