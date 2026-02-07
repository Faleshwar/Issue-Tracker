package com.issueflow.controller;

import com.issueflow.request.LoginRequest;
import com.issueflow.request.RegisterRequest;
import com.issueflow.response.AuthResponse;
import com.issueflow.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        String jwt = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse("Login success", jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){
        String jwt = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse("Registration success", jwt));
    }
}
