package com.example.incubation_planner.services.impl;

import com.example.incubation_planner.models.entity.UserRoleEntity;
import com.example.incubation_planner.models.entity.enums.UserRole;
import com.example.incubation_planner.repositories.UserRoleRepository;
import com.example.incubation_planner.services.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;


    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public void initUserRoles() {
        if (userRoleRepository.count() == 0) {
            UserRoleEntity adminRole = new UserRoleEntity().setRole(UserRole.ADMIN);
            UserRoleEntity userRole = new UserRoleEntity().setRole(UserRole.USER);
            this.userRoleRepository.saveAll(List.of(adminRole, userRole));
        }

    }
}
