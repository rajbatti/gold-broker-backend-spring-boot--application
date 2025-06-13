package com.hugoserve.demo.security;

import java.io.IOException;

import com.hugoserve.demo.model.UserData;
import com.hugoserve.demo.service.impl.MyUserDetails;
import com.hugoserve.demo.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class
JwtFilter extends OncePerRequestFilter {
    private final String[] excludedEndpoints = {"/login", "/register"};
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    private final JwtUtils jwtUtil;
    private String token;
    private String username;
    private final ApplicationContext context;

    public JwtFilter(JwtUtils jwtUtil, ApplicationContext context) {
        this.jwtUtil = jwtUtil;
        this.context = context;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        for (String endpoint : excludedEndpoints) {
            if (requestURI.startsWith(endpoint)) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWT_TOKEN".equals(cookie.getName())) { // Check for the cookie with the JWT
                    token = cookie.getValue();
                    username = jwtUtil.extractUsername(token);
                    break;
                }
            }
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserData userDetails = context.getBean(MyUserDetails.class).loadUserByUsername(username);
                Boolean validateToken = jwtUtil.validateToken(token, userDetails);

                if (Boolean.TRUE.equals(validateToken)) {
                    try {
                        logger.info(userDetails.toString());
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);

                    } catch (Exception e) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                    }
                } else {
                    logger.info("Invalid token!");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token please Relogin");
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);


    }
}
