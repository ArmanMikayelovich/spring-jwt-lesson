package com.procode.task.service;

import com.procode.task.model.dto.FileInfoDto;
import com.procode.task.model.dto.UserDto;
import com.procode.task.model.entity.FileInfoEntity;
import com.procode.task.model.entity.UserEntity;


public interface Mapper {

    void setUserService(UserService service);

    UserDto map(UserEntity entity, UserDto dto);


    FileInfoDto map(FileInfoEntity entity, FileInfoDto dto);

    FileInfoEntity map(FileInfoDto dto, FileInfoEntity entity);


}
