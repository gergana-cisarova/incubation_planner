package com.example.incubation_planner.services;

import com.example.incubation_planner.models.entity.Project;
import com.example.incubation_planner.models.entity.UserEntity;
import com.example.incubation_planner.models.entity.enums.UserRole;
import com.example.incubation_planner.models.service.UserRegistrationServiceModel;
import com.example.incubation_planner.models.view.UserViewModel;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    void initUsers();

    void registerAndLoginUser(UserRegistrationServiceModel userRegistrationServiceModel);

    boolean usernameExists(String username);

    UserEntity findByUsername(String userName);

    void updateUser(UserEntity collaborator);

    List<UserViewModel> getAll();

    void deleteUser(String id);

    Set<Project> getProjectsByUser(String username);

    Set<String> findAllUsernamesExceptCurrent();

    void changeRole(String username, List<String> roles);
}
