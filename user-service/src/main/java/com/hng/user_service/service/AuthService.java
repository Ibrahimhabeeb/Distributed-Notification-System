package com.hng.user_service.service;
import com.hng.user_service.dtos.UserCreationRequest;
import com.hng.user_service.exceptions.BadRequestException;
import com.hng.user_service.models.User;
import com.hng.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.hng.user_service.security.JwtUtils;
import com.hng.user_service.dtos.LoginRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.hng.user_service.dtos.AuthResponse;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(UserCreationRequest request) {

        userRepository.findByEmail(request.getEmail())
                .ifPresent(u -> { throw new BadRequestException("Email already in use"); });

        var user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword())).name(request.getName())
                .pushToken(request.getPushToken())
                .build();
        userRepository.save(user);

        var jwtToken = jwtUtils.generateToken(user.getId(), user.getEmail());
        return new AuthResponse(jwtToken, user.getId());
    }

    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );


        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwtToken = jwtUtils.generateToken(user.getId(), user.getEmail());
        return new AuthResponse(jwtToken, user.getId());
    }


}
