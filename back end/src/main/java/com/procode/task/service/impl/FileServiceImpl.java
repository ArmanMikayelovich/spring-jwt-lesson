package com.procode.task.service.impl;

import com.procode.task.dao.FileInfoDAO;
import com.procode.task.exceptions.NotFoundException;
import com.procode.task.exceptions.RestrictedOperationException;
import com.procode.task.exceptions.ServerSideException;
import com.procode.task.model.dto.FileInfoDto;
import com.procode.task.model.entity.FileInfoEntity;
import com.procode.task.service.FileService;
import com.procode.task.service.Mapper;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FileServiceImpl implements FileService {
    private static final String PDF_EXTENSION = "pdf";

    private final FileInfoDAO fileInfoDAO;
    private final Mapper mapper;


    @Value("${fileService.parentPath}")
    private String parentPath;

    public FileServiceImpl(FileInfoDAO fileInfoDAO, Mapper mapper) {
        this.fileInfoDAO = fileInfoDAO;
        this.mapper = mapper;
    }

    private FileInfoEntity findEntityById(Long id) {
        return fileInfoDAO.findById(id).orElseThrow(() -> new NotFoundException("File with id: " + id + " not found"));
    }

    @Override
    public byte[] getFromFileSystem(Long id) {
        FileInfoEntity fileInfoEntity = findEntityById(id);

        Path path = Paths.get(parentPath,
                fileInfoEntity.getUserEntity().getId().toString()
                , fileInfoEntity.getFileName());
        if (path.toFile().exists()) {

            try {
                return Files.readAllBytes(path);
            } catch (IOException e) {
                throw new ServerSideException(e.getMessage());
            }
        }
        throw new NotFoundException("File with name: " + fileInfoEntity.getFileName() + " not found.");
    }

    @Override
    @Transactional
    public void save(FileInfoDto dto, MultipartFile file) {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!PDF_EXTENSION.equals(fileExtension)) {
            throw new RestrictedOperationException("File type is not xml.");
        }

        Path path = checkUserDirectoryExistence(dto.getUserId());
        writeInFileSystem(file, path);

        Optional<FileInfoEntity> optionalFileInfoEntity =
                fileInfoDAO.getFileByNameAndUserId(file.getOriginalFilename(), dto.getUserId());
        if (optionalFileInfoEntity.isPresent()) {
            FileInfoEntity fileInfoEntity = optionalFileInfoEntity.get();
            fileInfoEntity.setUpdateTimestamp(LocalDateTime.now());
            fileInfoDAO.update(fileInfoEntity);

        } else {
            FileInfoEntity fileInfoEntity = mapper.map(dto, new FileInfoEntity());
            fileInfoEntity.setFileName(file.getOriginalFilename());

            fileInfoDAO.save(fileInfoEntity);
        }

    }


    @Override
    public List<FileInfoDto> getAllByUserId(Long userId) {

        List<FileInfoEntity> allByUser = fileInfoDAO.getAllByUser(userId);

        return allByUser.stream()
                .map(fileInfoEntity -> mapper.map(fileInfoEntity, new FileInfoDto()))
                .collect(Collectors.toList());
    }

    @Override
    public List<FileInfoDto> getAll() {
        return fileInfoDAO.getAll().stream()
                .map(fileInfo -> mapper.map(fileInfo, new FileInfoDto()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        FileInfoEntity fileInfoEntity = findEntityById(id);
        Path userFolderPath = getUserFolderPath(fileInfoEntity.getUserEntity().getId());
        File file = new File(userFolderPath.toString(), fileInfoEntity.getFileName());

        if (!file.exists()) {
            throw new NotFoundException("File with name: " + fileInfoEntity.getFileName() + " not found.");
        }

        try {
            Files.delete(file.toPath());
        } catch (IOException exception) {
            throw new ServerSideException("Can't delete file: " + fileInfoEntity.getFileName());
        }

        fileInfoDAO.remove(id);
    }

    private Path checkUserDirectoryExistence(Long userId) {
        Path path = getUserFolderPath(userId);
        if (Files.notExists(path)) {
            try {
                return Files.createDirectory(path);
            } catch (IOException e) {
                throw new ServerSideException(e.getMessage());
            }
        }
        return path;
    }

    private Path getUserFolderPath(Long userId) {
        return Paths.get(parentPath, userId.toString());
    }

    private void writeInFileSystem(MultipartFile file, Path path) {
        final String name = file.getOriginalFilename();

        try (
                FileOutputStream fileOutputStream =
                        new FileOutputStream(new File(path.toString(), Objects.requireNonNull(name)));
                BufferedOutputStream stream = new BufferedOutputStream(fileOutputStream)) {

            byte[] bytes = file.getBytes();
            stream.write(bytes);
        } catch (Exception e) {
            throw new ServerSideException(e.getMessage());
        }
    }
}
