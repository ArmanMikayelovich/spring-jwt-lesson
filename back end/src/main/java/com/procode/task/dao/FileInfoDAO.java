package com.procode.task.dao;

import com.procode.task.model.entity.FileInfoEntity;

import java.util.List;
import java.util.Optional;

public interface FileInfoDAO {

    void save(FileInfoEntity fileInfoEntity);

    List<FileInfoEntity> getAllByUser(Long userId);

    void update(FileInfoEntity fileInfoEntity);

    Optional<FileInfoEntity> findById(Long id);

    List<FileInfoEntity> getAll();

    void remove(Long id);

    Optional<FileInfoEntity> getFileByNameAndUserId(String name, Long userId);
}
