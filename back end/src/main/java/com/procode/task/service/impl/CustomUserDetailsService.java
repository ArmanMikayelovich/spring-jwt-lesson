package com.procode.task.service.impl;

import com.procode.task.dao.UserDao;
import com.procode.task.model.entity.UserEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Component
@Primary
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDao userDAO;

    public CustomUserDetailsService(UserDao userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<UserEntity> optionalUserEntity = userDAO.findByUsername(username);

        if (optionalUserEntity.isPresent()) {
            UserEntity user = optionalUserEntity.get();
            SimpleGrantedAuthority role = new SimpleGrantedAuthority(user.getRoleEntity().getRole().toString());
            return new User(username, user.getPassword(),
                    Collections.singleton(role));
        }
        return null;
    }

}
