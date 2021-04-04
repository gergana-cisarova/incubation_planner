package com.example.incubation_planner.services;

import com.example.incubation_planner.models.entity.LogEntity;
import com.example.incubation_planner.models.entity.Project;
import com.example.incubation_planner.models.entity.UserEntity;
import com.example.incubation_planner.models.entity.UserRoleEntity;
import com.example.incubation_planner.models.service.UserRegistrationServiceModel;
import com.example.incubation_planner.models.view.UserViewModel;

import java.util.List;
import java.util.Set;

public interface UserService {

    void initUsers();

    void registerAndLoginUser(UserRegistrationServiceModel userRegistrationServiceModel);

    boolean usernameExists(String username);

    UserEntity findByUsername(String userName);

    void updateUser(UserEntity collaborator);

    List<UserViewModel> getAll();

    List<LogEntity> deleteUser(String id);

    Set<Project> getProjectsByUser(String username);

    Set<String> findAllUsernamesExceptCurrent();

    List<UserRoleEntity> changeRole(String username, List<String> roles);
}
