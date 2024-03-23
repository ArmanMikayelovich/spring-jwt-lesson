package com.procode.task.controller;

import com.procode.task.model.dto.FileInfoDto;
import com.procode.task.service.FileService;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping(value = "/upload-file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void uploadFile(@RequestParam("file") MultipartFile multipartFile,
                           @RequestParam String description, @RequestParam Long userId) {
        final FileInfoDto fileInfoDto = new FileInfoDto();
        fileInfoDto.setUserId(userId);
        fileInfoDto.setDescription(description);

        fileService.save(fileInfoDto, multipartFile);
    }


    @GetMapping("/from-FS/{fileId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER')")
    public byte[] getFileFromFileSystem(@PathVariable("fileId") Long fileId) {
        return fileService.getFromFileSystem(fileId);
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('MANAGER') || (hasRole('EMPLOYEE') && #userId == authentication.principal.id) ")
    public List<FileInfoDto> getAll(@RequestParam(value = "userId", required = false) Long userId) {
        if (userId != null) {
            return fileService.getAllByUserId(userId);
        } else {
            return fileService.getAll();
        }
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/delete/{fileId}")
    public void delete(@PathVariable("fileId") Long fileId) {
        fileService.delete(fileId);
    }


}
