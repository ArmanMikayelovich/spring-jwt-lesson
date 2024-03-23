package com.procode.task.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users",indexes = {
        @Index(name = "users_username_uindex",columnList = "username")
})
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, updatable = false)
    private String username;

    private String password;

    @ManyToOne

    @JoinColumn(name = "role_id", nullable = false,
            foreignKey = @ForeignKey(name = "users_role_id_roles_id_fk",
                    value = ConstraintMode.CONSTRAINT))
    private RoleEntity roleEntity;


    @OneToMany(mappedBy = "userEntity")
    private List<FileInfoEntity> files;
}
