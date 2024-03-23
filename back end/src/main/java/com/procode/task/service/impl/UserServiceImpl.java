package com.procode.task.service.impl;

import com.procode.task.dao.UserDao;
import com.procode.task.exceptions.AlreadyExistsException;
import com.procode.task.exceptions.NotFoundException;
import com.procode.task.exceptions.RestrictedOperationException;
import com.procode.task.model.dto.UserDto;
import com.procode.task.model.entity.UserEntity;
import com.procode.task.service.Mapper;
import com.procode.task.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserDao userDAO;
    private final Mapper mapper;


    public UserServiceImpl(UserDao userDAO, Mapper mapper) {
        this.userDAO = userDAO;
        this.mapper = mapper;
    }

    @PostConstruct
    public void init() {
        mapper.setUserService(this);
    }

    @Override
    @Transactional
    public void save(UserEntity entity) {
        if (entity.getId() != null) {
            throw new RestrictedOperationException("The new entity must not have an identifier");
        }

        final boolean usernameAlreadyTaken = userDAO.findByUsername(entity.getUsername()).isPresent();
        if (usernameAlreadyTaken) {
            throw new AlreadyExistsException("User with username: "
                    + entity.getUsername() + " already exists.");
        }
        userDAO.save(entity);

    }

    @Override
    public UserDto findByUserId(Long id) {
        return mapper.map(findEntityById(id), new UserDto());
    }

    @Override
    public UserEntity findEntityById(Long id) {
        return userDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id: " + id + " not found"));
    }

    @Override
    public UserEntity findEntityByUsername(String username) {
        return userDAO.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User with username: " + username + " not found"));
    }

    @Override
    public List<UserDto> findAll() {
        return userDAO.findAll().stream()
                .map(entity -> mapper.map(entity, new UserDto()))
                .collect(Collectors.toList());
    }
}
