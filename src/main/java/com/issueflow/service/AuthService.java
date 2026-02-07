package com.issueflow.service;

import com.issueflow.request.LoginRequest;
import com.issueflow.request.RegisterRequest;
import jakarta.validation.constraints.NotNull;

public interface AuthService {

    @NotNull
    String login(LoginRequest request);

    String login(String idToken);

    @NotNull
    String register(RegisterRequest request);
}
