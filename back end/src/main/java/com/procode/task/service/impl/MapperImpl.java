package com.procode.task.service.impl;

import com.procode.task.model.dto.FileInfoDto;
import com.procode.task.model.dto.UserDto;
import com.procode.task.model.entity.FileInfoEntity;
import com.procode.task.model.entity.UserEntity;
import com.procode.task.service.Mapper;
import com.procode.task.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MapperImpl implements Mapper {
    private  UserService userService;

    @Override
    public void setUserService(UserService userService) {
        this.userService = userService;
    }



    @Override
    public UserDto map(UserEntity entity, UserDto dto) {
        dto.setId(entity.getId());
        dto.setRole(entity.getRoleEntity().getRole().toString());
        dto.setUsername(entity.getUsername());
        return dto;
    }

    @Override
    public FileInfoDto map(FileInfoEntity entity, FileInfoDto dto) {
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setFileName(entity.getFileName());
        dto.setUpdateTimestamp(entity.getUpdateTimestamp());
        dto.setUploadTimestamp(entity.getUploadTimestamp());
        dto.setUserId(entity.getUserEntity().getId());
        return dto;
    }

    @Override
    public FileInfoEntity map(FileInfoDto dto, FileInfoEntity entity) {
        entity.setUserEntity(userService.findEntityById(dto.getUserId()));
        entity.setDescription(dto.getDescription());
        return entity;
    }
}
