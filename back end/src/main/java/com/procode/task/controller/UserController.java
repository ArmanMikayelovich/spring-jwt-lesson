package com.procode.task.controller;

import com.procode.task.model.dto.UserDto;
import com.procode.task.service.UserService;
import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users/")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping(value = "{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto getUserById(@PathVariable Long userId) {
        return userService.findByUserId(userId);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDto> getAllUsers() {
        return userService.findAll();
    }
}
