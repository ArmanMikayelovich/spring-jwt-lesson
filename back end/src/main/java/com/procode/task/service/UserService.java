package com.procode.task.service;

import com.procode.task.model.dto.UserDto;
import com.procode.task.model.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
public interface UserService {


    void save(UserEntity entity);

    UserDto findByUserId(Long id);

    UserEntity findEntityById(Long id);

    UserEntity findEntityByUsername(String username);

    List<UserDto> findAll();
}
