package com.demo.empmanagement.services;

import com.demo.empmanagement.dto.AuthRequest;
import com.demo.empmanagement.dto.AuthResponse;
import com.demo.empmanagement.models.RefreshToken;
import com.demo.empmanagement.repository.UserRepository;
import com.demo.empmanagement.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor  // Generates constructor for all 'final' fields
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    // =====================================================================
    // @RequiredArgsConstructor replaces this constructor (commented out):
    // =====================================================================
    //
    // public AuthService(UserRepository userRepository, JwtService jwtService,
    //                    AuthenticationManager authenticationManager,
    //                    RefreshTokenService refreshTokenService,
    //                    PasswordEncoder passwordEncoder) {
    //     this.userRepository = userRepository;
    //     this.jwtService = jwtService;
    //     this.authenticationManager = authenticationManager;
    //     this.refreshTokenService = refreshTokenService;
    //     this.passwordEncoder = passwordEncoder;
    // }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }
}
