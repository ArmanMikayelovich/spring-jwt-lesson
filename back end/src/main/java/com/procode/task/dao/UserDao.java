package com.procode.task.dao;

import com.procode.task.model.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<UserEntity> findById(Long id);


    Optional<UserEntity> findByUsername(String username);

    void save(UserEntity user);

    List<UserEntity> findAll();

}
