package com.resiguard.resiguard.service.impl;

import com.resiguard.resiguard.dto.request.LoginRequest;
import com.resiguard.resiguard.dto.request.RegisterRequest;
import com.resiguard.resiguard.dto.response.AuthResponse;
import com.resiguard.resiguard.exception.BadRequestException;
import com.resiguard.resiguard.factory.UserFactory;
import com.resiguard.resiguard.model.User;
import com.resiguard.resiguard.model.enums.ProfileStatus;
import com.resiguard.resiguard.repository.UserRepository;
import com.resiguard.resiguard.security.JwtUtil;
import com.resiguard.resiguard.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final UserFactory userFactory;

    public AuthServiceImpl(UserRepository userRepo, PasswordEncoder encoder,
                            JwtUtil jwtUtil, UserFactory userFactory) {
        this.userRepo = userRepo; this.encoder = encoder;
        this.jwtUtil = jwtUtil; this.userFactory = userFactory;
    }

    @Override
    public AuthResponse register(RegisterRequest req) {
        if (userRepo.existsByEmail(req.email))
            throw new BadRequestException("Email already registered: " + req.email);
        User user = userFactory.createUser(req);
        userRepo.save(user);
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return AuthResponse.builder().token(token).userId(user.getId())
                .name(user.getName()).email(user.getEmail()).role(user.getRole()).build();
    }

    @Override
    public AuthResponse login(LoginRequest req) {
        User user = userRepo.findByEmail(req.email)
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));
        if (!encoder.matches(req.password, user.getPassword()))
            throw new BadRequestException("Invalid credentials");
        if (user.getStatus() == ProfileStatus.PENDING)
            throw new BadRequestException("Profile pending admin approval");
        if (user.getStatus() == ProfileStatus.INACTIVE || user.getStatus() == ProfileStatus.REJECTED)
            throw new BadRequestException("Account inactive or rejected");
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return AuthResponse.builder().token(token).userId(user.getId())
                .name(user.getName()).email(user.getEmail()).role(user.getRole()).build();
    }
}
