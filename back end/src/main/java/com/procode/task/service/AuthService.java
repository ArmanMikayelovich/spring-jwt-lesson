package com.procode.task.service;

import com.procode.task.model.dto.AuthenticationDto;

public interface AuthService {
    void register(AuthenticationDto dto);

    String authenticate(AuthenticationDto dto);
}
