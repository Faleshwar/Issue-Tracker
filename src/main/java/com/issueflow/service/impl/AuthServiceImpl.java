package com.issueflow.service.impl;

import com.issueflow.modal.User;
import com.issueflow.modal.UserRole;
import com.issueflow.repository.UserRepository;
import com.issueflow.request.LoginRequest;
import com.issueflow.request.RegisterRequest;
import com.issueflow.security.JwtService;
import com.issueflow.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public String login(LoginRequest request) {

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        }catch(AuthenticationException ex){
            throw ex;
        }

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()->new BadCredentialsException("User cannot find"));
        return jwtService.generateToken(user);
    }

    @Override
    public String login(String idToken) {
        return null;
    }

    @Override
    @Transactional
    public String register(RegisterRequest request) {
        //System.out.println(request.getEmail()+" "+request.getName()+" "+request.getPassword());
        if(userRepository.existsByEmail(request.getEmail())){
            throw new BadCredentialsException("Email is already registered");
        }
        UserRole role;
        try{
            role = UserRole.valueOf(request.getUserRole());
        }catch(IllegalArgumentException | NullPointerException ex){
            role = UserRole.DEVELOPER;
        }

        User user = new User(request.getEmail(), request.getName(),passwordEncoder.encode(request.getPassword()), role);
        userRepository.save(user);

        return jwtService.generateToken(user);
    }
}
