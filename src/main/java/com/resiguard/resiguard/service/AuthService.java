package com.resiguard.resiguard.service;
import com.resiguard.resiguard.dto.request.LoginRequest;
import com.resiguard.resiguard.dto.request.RegisterRequest;
import com.resiguard.resiguard.dto.response.AuthResponse;
public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
