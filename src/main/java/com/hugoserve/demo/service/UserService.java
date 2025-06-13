package com.hugoserve.demo.service;

import com.hugoserve.demo.proto.ApiResponse;
import com.hugoserve.demo.proto.AuthRequestDTO;
import com.hugoserve.demo.proto.UserDetailsDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;


public interface UserService {
    ResponseEntity<ApiResponse> createUser(UserDetailsDTO user, HttpServletResponse response);

    ResponseEntity<ApiResponse> authenticateUser(AuthRequestDTO user, HttpServletResponse response);

}
