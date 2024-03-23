package com.procode.task.model.entity;

import com.procode.task.model.enums.UserRole;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Data
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name",unique = true)
    private UserRole role;

}
