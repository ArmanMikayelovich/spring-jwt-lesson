package com.procode.task.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "file_info")
@Getter
@Setter
public class FileInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "upload_timestamp")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime uploadTimestamp;


    @UpdateTimestamp
    @Column(name = "update_timestamp")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTimestamp;


    @Column(name = "file_name", length = 1024)
    private String fileName;

    @Column(name = "description", length = 1500)
    private String description;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "file_info_user_id___users_id_fk",
                    value = ConstraintMode.CONSTRAINT))
    private UserEntity userEntity;
}
