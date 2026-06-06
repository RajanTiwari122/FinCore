package com.bank.ib.auth.controller;

import com.bank.ib.auth.dto.LoginRequest;
import com.bank.ib.auth.dto.LoginResponse;
import com.bank.ib.auth.entity.User;
import com.bank.ib.auth.repository.UserRepository;
import com.bank.ib.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private UserRepository userRepository;
    private JwtUtil jwtUtil;
    private AuthenticationManager authenticationManager;
    public AuthController(UserRepository userRepository,
                          JwtUtil jwtUtil,AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }
    @GetMapping("/user")
    public ResponseEntity<User> authenticatedUser(@RequestBody String username){
        System.out.println("Authenticated User");
        return ResponseEntity.ok(userRepository.findByUsername(username).orElseThrow(() ->
                new RuntimeException("User Not Found")));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );

        UserDetails user = (UserDetails) authentication.getPrincipal();

        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getAuthorities().iterator().next().getAuthority()
        );
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
