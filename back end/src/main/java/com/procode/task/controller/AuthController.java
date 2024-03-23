package com.procode.task.controller;

import com.procode.task.model.dto.AuthenticationDto;
import com.procode.task.service.AuthService;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PreAuthorize("permitAll()")
    @PostMapping(value = "register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void register(@RequestBody AuthenticationDto dto) {
        authService.register(dto);
    }

    @PreAuthorize("permitAll()")
    @PostMapping(value = "login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> login(@RequestBody AuthenticationDto dto) {
        final String jwtToken = authService.authenticate(dto);
        return Collections.singletonMap("token", jwtToken);
    }
}
