package com.example.incubation_planner.services.impl;

import com.example.incubation_planner.models.entity.*;
import com.example.incubation_planner.models.entity.enums.UserRole;
import com.example.incubation_planner.models.entity.enums.UserType;
import com.example.incubation_planner.models.service.UserRegistrationServiceModel;
import com.example.incubation_planner.models.view.UserViewModel;
import com.example.incubation_planner.repositories.LogRepository;
import com.example.incubation_planner.repositories.UserRepository;
import com.example.incubation_planner.repositories.UserRoleRepository;
import com.example.incubation_planner.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final IncubationUserService incubationUserService;
    private final LogRepository logRepository;

    public UserServiceImpl(UserRoleRepository userRoleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, IncubationUserService incubationUserService, LogRepository logRepository) {
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.incubationUserService = incubationUserService;
        this.logRepository = logRepository;
    }

    @Override
    public void initUsers() {
        if (userRepository.count() == 0) {
            UserRoleEntity adminRole = this.userRoleRepository.findByRole(UserRole.ADMIN).orElse(null);
            UserRoleEntity userRole = this.userRoleRepository.findByRole(UserRole.USER).orElse(null);

            UserEntity admin = new UserEntity()
                    .setUsername("admin")
                    .setPassword(passwordEncoder.encode("12345"))
                    .setEmail("admin@admin.bg")
                    .setFirstName("Admin")
                    .setLastName("Adminov")
                    .setUserType(UserType.University);
            admin.setRoles(List.of(adminRole, userRole));
            UserEntity user = new UserEntity()
                    .setUsername("user")
                    .setPassword(passwordEncoder.encode("123"))
                    .setFirstName("Pesho")
                    .setLastName("Peshov")
                    .setEmail("pesho@user.bg")
                    .setUserType(UserType.University);
            user.setRoles(List.of(userRole));
            userRepository.saveAll(List.of(user, admin));
        }
    }

    @Override
    public void registerAndLoginUser(UserRegistrationServiceModel userRegistrationServiceModel) {
        UserEntity newUser = modelMapper.map(userRegistrationServiceModel, UserEntity.class);
        newUser.setPassword(passwordEncoder.encode(userRegistrationServiceModel.getPassword()));

        UserRoleEntity userRole = userRoleRepository.findByRole(UserRole.USER).orElseThrow(() -> new IllegalStateException("User role not found. Please seed the roles."));
        newUser.addRole(userRole);
        newUser = userRepository.save(newUser);
        UserDetails principal = incubationUserService.loadUserByUsername(newUser.getUsername());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal,
                newUser.getPassword(),
                principal.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public UserEntity findByUsername(String userName) {
        return userRepository.findByUsername(userName).orElse(null);
    }

    @Override
    public void updateUser(UserEntity collaborator) {
        userRepository.save(collaborator);
    }

    @Override
    @PostFilter("!filterObject.username.equalsIgnoreCase(authentication.name)")
    public List<UserViewModel> getAll() {
        return userRepository.findAll()
                .stream()
                .map(u -> {
                    UserViewModel userViewModel = modelMapper.map(u, UserViewModel.class);
                    userViewModel.setFullNameAndEmail(
                            String.format("%s %s <br /> %s", u.getFirstName(), u.getLastName(), u.getEmail()));
                    userViewModel.setSectorAndType(
                            String.format("%s <br /> %s", u.getSector(), u.getUserType()));

                    StringBuilder sb1 = new StringBuilder();
                    u.getOwnProjects()
                            .stream()
                            .filter(p -> p.isActive())
                            .forEach(p -> sb1.append(String.format("%s <br />", p.getName())));

                    userViewModel.setActiveProjects(sb1.toString());

                    StringBuilder sb2 = new StringBuilder();
                    u.getOwnProjects()
                            .stream()
                            .filter(p -> !p.isActive())
                            .forEach(p -> sb2.append(String.format("%s <br />", p.getName())));

                    userViewModel.setArchivesProjects(sb2.toString());

                    StringBuilder sb3 = new StringBuilder();
                    u.getProjects()
                            .stream()
                            .filter(p -> p.isActive())
                            .forEach(p -> sb3.append(String.format("%s <br />", p.getName())));

                    userViewModel.setActiveCollabs(sb3.toString());

                    StringBuilder sb4 = new StringBuilder();
                    u.getRoles()
                            .forEach(r -> sb4.append(String.format("%s <br />", r.getRole().name())));

                    userViewModel.setRoles(sb4.toString());

                    return userViewModel;
                }).collect(Collectors.toList());

    }

    @Override
    public List<LogEntity> deleteUser(String id) {

        UserEntity user = userRepository.getOne(id);
        user.getProjects().stream().forEach(p -> p.getCollaborators().remove(user));
        List<LogEntity> logs = logRepository.findByUser_Id(id);
        if (!logs.isEmpty()) {
            logs.forEach(l -> logRepository.delete(l));
        }
        userRepository.deleteById(id);
        return logs;
}

    @Override
    public Set<Project> getProjectsByUser(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            UserEntity user = userRepository.findByUsername(username).get();
            Set<Project> projects = user.getProjects();
            return user.getProjects();
        } else {
            throw new IllegalArgumentException("No user with such username");
        }
    }

    @Override
    @PostFilter("!filterObject.equalsIgnoreCase(authentication.name)")
    public Set<String> findAllUsernamesExceptCurrent() {
        return userRepository
                .findAll()
                .stream()
                .map(u -> u.getUsername())
                .collect(Collectors.toSet());
    }

    @Override
    public List<UserRoleEntity> changeRole(String username, List<String> roles) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(IllegalArgumentException::new);
        List<UserRoleEntity> newRoleList = new ArrayList<>();
        roles.forEach(r -> {
            UserRoleEntity userRole = userRoleRepository.findByRole(UserRole.valueOf(r.toUpperCase())).orElseThrow(() -> new IllegalStateException("User role not found. Please seed the roles."));
            newRoleList.add(userRole);
        });
        user.setRoles(newRoleList);
        userRepository.save(user);
        return newRoleList;
    }

}



