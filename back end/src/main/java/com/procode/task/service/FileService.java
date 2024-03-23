package com.procode.task.service;

import com.procode.task.model.dto.FileInfoDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {


    byte[] getFromFileSystem(Long id);

    void save(FileInfoDto dto, MultipartFile file);

    List<FileInfoDto> getAllByUserId(Long userId);

    List<FileInfoDto> getAll();

    void delete(Long id);
}
