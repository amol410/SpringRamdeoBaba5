package com.demo.empmanagement.controllers;

import com.demo.empmanagement.dto.AuthRequest;
import com.demo.empmanagement.dto.AuthResponse;
import com.demo.empmanagement.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // @RequiredArgsConstructor replaces:
    // public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
