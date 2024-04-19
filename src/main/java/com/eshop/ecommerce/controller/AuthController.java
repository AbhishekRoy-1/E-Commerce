package com.eshop.ecommerce.controller;

import com.eshop.ecommerce.common.AuthenticationResponse;
import com.eshop.ecommerce.model.user.AuthenticationRequest;
import com.eshop.ecommerce.model.user.RegisterRequest;
import com.eshop.ecommerce.model.user.Role;
import com.eshop.ecommerce.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ecommerce/v1/auth")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/register/admin")
    public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody RegisterRequest registerRequest){
        AuthenticationResponse authenticationResponse = authService.register(registerRequest, Role.ADMIN);
        return ResponseEntity.ok(authenticationResponse);
    }
    @PostMapping("/register/user")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody RegisterRequest registerRequest){
        AuthenticationResponse authenticationResponse = authService.register(registerRequest, Role.USER);
        return ResponseEntity.ok(authenticationResponse);
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest authenticationRequest){
        AuthenticationResponse authenticationResponse = authService.authenticate(authenticationRequest, Role.USER);
        return ResponseEntity.ok(authenticationResponse);
    }

}
