package com.example.incubation_planner.models.entity;

import com.example.incubation_planner.models.entity.enums.UserRole;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "roles")
public class UserRoleEntity extends BaseEntity {
    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public UserRole getRole() {
        return role;
    }


    public UserRoleEntity setRole(UserRole role) {
        this.role = role;
        return this;
    }


}
