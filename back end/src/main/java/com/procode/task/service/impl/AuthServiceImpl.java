package com.procode.task.service.impl;

import com.procode.task.config.jwt.JwtTokenProvider;
import com.procode.task.dao.RoleDao;
import com.procode.task.exceptions.AuthenticationException;
import com.procode.task.model.dto.AuthenticationDto;
import com.procode.task.model.entity.RoleEntity;
import com.procode.task.model.entity.UserEntity;
import com.procode.task.model.enums.UserRole;
import com.procode.task.service.AuthService;
import com.procode.task.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(RoleDao roleDao, PasswordEncoder passwordEncoder, UserService userService,
                           JwtTokenProvider jwtTokenProvider) {
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    @Transactional
    public void register(AuthenticationDto dto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(dto.getUsername());

        final String hashedPassword = passwordEncoder.encode(dto.getPassword());
        userEntity.setPassword(hashedPassword);
        Optional<RoleEntity> optional = roleDao.findByRoleName(UserRole.EMPLOYEE);
        optional.ifPresent(userEntity::setRoleEntity);
        userService.save(userEntity);
    }



    @Override
    public String authenticate(AuthenticationDto dto) {
        final UserEntity userEntity = userService.findEntityByUsername(dto.getUsername());

        final boolean isPasswordsMatches = passwordEncoder.matches(dto.getPassword(), userEntity.getPassword());
        if (isPasswordsMatches) {
            return jwtTokenProvider.createToken(userEntity);
        } else {
            throw new AuthenticationException("invalid password");
        }

    }
}
