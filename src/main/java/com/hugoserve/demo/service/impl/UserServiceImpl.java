package com.hugoserve.demo.service.impl;

import com.google.protobuf.Any;
import com.hugoserve.demo.Exception.UserException;
import com.hugoserve.demo.Exception.UserExistException;
import com.hugoserve.demo.constants.AppConstants;
import com.hugoserve.demo.constants.StatusCodes;
import com.hugoserve.demo.facade.UserFacade;
import com.hugoserve.demo.facade.WalletFacade;
import com.hugoserve.demo.proto.ApiResponse;
import com.hugoserve.demo.proto.AuthRequestDTO;
import com.hugoserve.demo.proto.AuthResponseDTO;
import com.hugoserve.demo.proto.UserDetailsDTO;
import com.hugoserve.demo.proto.entity.UserEntity;
import com.hugoserve.demo.service.Helper.DTOtoEntity;
import com.hugoserve.demo.service.UserService;
import com.hugoserve.demo.utils.JwtUtils;
import com.hugoserve.demo.utils.ResponseUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserFacade userFacade;
    private final WalletFacade walletFacade;

    UserServiceImpl(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserFacade userFacade, WalletFacade walletFacade) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userFacade = userFacade;
        this.walletFacade = walletFacade;
    }

    @Override
    public ResponseEntity<ApiResponse> createUser(UserDetailsDTO user, HttpServletResponse response) {

        UserEntity userEntity = DTOtoEntity.userEntityCreate(user);
        try {
            userFacade.createUser(userEntity);
            walletFacade.createWallet(userEntity);
        } catch (UserExistException e) {
            throw new UserException(StatusCodes.ERROR_USER_ALREADY_EXISTS);
        }

        AuthRequestDTO authuser = AuthRequestDTO.newBuilder().setPassword(user.getPassword()).setUsername(user.getUsername()).build();
        return authenticateUser(authuser, response);
    }

    @Override
    public ResponseEntity<ApiResponse> authenticateUser(AuthRequestDTO user, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        AuthResponseDTO responseDTO = AuthResponseDTO.newBuilder().setToken(jwtUtils.generateToken(user.getUsername())).build();
        addTokenToCookie(response, responseDTO.getToken());
        return ResponseUtils.buildResponse(HttpStatus.OK, true, StatusCodes.SUCCESS_LOGIN, Any.pack(responseDTO));
    }

    private void addTokenToCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(AppConstants.JWT_TOKEN, token);  // Name of the cookie
        cookie.setHttpOnly(true); // To prevent access from JavaScript (for security)
        cookie.setSecure(true);   // Make sure it's sent over HTTPS (for security)
        cookie.setPath("/");      // The cookie is available for the entire application
        cookie.setMaxAge(60 * 60); // Set expiration (in seconds), for example 1 hour
        response.addCookie(cookie); // Add the cookie to the response
    }
}
