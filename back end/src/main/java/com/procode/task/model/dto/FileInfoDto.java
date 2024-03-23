package com.procode.task.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileInfoDto {

    private Long id;

    private LocalDateTime uploadTimestamp;

    private LocalDateTime updateTimestamp;

    private String fileName;

    private String description;

    private Long userId;
}
