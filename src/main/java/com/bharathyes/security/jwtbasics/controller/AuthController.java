package com.bharathyes.security.jwtbasics.controller;


import com.bharathyes.security.jwtbasics.repository.UserRepo;
import com.bharathyes.security.jwtbasics.entity.UserDetail;
import com.bharathyes.security.jwtbasics.model.LoginCredentials;
import com.bharathyes.security.jwtbasics.security.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private UserRepo userRepo;

    private JWTUtil jwtUtil;

    private AuthenticationManager authenticationManager;

    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public Map<String, Object> registerHandler(@RequestBody UserDetail userDetail) {
        String encodedPass = passwordEncoder.encode(userDetail.getPassword());
        userDetail.setPassword(encodedPass);
        userDetail = userRepo.save(userDetail);
        String token = jwtUtil.generateToken(userDetail.getEmail());
        return Collections.singletonMap("jwt-token", token);
    }

    @PostMapping("/login")
    public Map<String, Object> loginHandler(@RequestBody LoginCredentials body) {
        try {
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(body.getEmail(), body.getPassword());

            log.info(authInputToken.toString());

            authenticationManager.authenticate(authInputToken);

            String token = jwtUtil.generateToken(body.getEmail());
            log.info(token);

            return Collections.singletonMap("jwt-token", token);
        } catch (AuthenticationException authExc) {
            throw new RuntimeException("Invalid Login Credentials");
        }
    }
}
