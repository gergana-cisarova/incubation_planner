package com.example.incubation_planner.service.impl;

import com.example.incubation_planner.models.entity.*;
import com.example.incubation_planner.models.entity.enums.Sector;
import com.example.incubation_planner.models.entity.enums.UserRole;
import com.example.incubation_planner.models.entity.enums.UserType;
import com.example.incubation_planner.models.view.UserViewModel;
import com.example.incubation_planner.repositories.LogRepository;
import com.example.incubation_planner.repositories.UserRepository;
import com.example.incubation_planner.repositories.UserRoleRepository;
import com.example.incubation_planner.services.impl.IncubationUserService;
import com.example.incubation_planner.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    UserServiceImpl service;
    ModelMapper modelMapper;
    @Mock
    LogRepository mockLogRepository;
    @Mock
    UserRepository mockUserRepository;
    @Mock
    UserRoleRepository mockUserRoleRepository;
    @Mock
    UserEntity mockUserEntity;


    IncubationUserService incubationUserService;
    @Autowired
    PasswordEncoder passwordEncoder;

    UserEntity firstUser;
    UserEntity secondUser;
    Equipment equipment;
    Lab lab;
    ActivityType activityType;
    Project firstProject;
    Project secondProject;
    Project thirdProject;
    Project fourthProject;
    Project fifthProject;

    @BeforeEach
    public void setUp() {
        this.modelMapper = new ModelMapper();
        firstUser = this.getFirstUser();
        secondUser = this.getSecondUser();
        incubationUserService = new IncubationUserService(mockUserRepository);
        this.service = new UserServiceImpl(mockUserRoleRepository, mockUserRepository, passwordEncoder, modelMapper, incubationUserService, mockLogRepository);
        equipment = getEquipment();
        lab = getLab();
        activityType = getActivityType();
        firstProject = getProjects().get(0);
        secondProject = getProjects().get(1);
        thirdProject = getProjects().get(2);
        fourthProject = getProjects().get(3);
        fifthProject = getProjects().get(4);
    }


    @Test
    void testUsernameExists() {

        when(mockUserRepository.findByUsername("pesho")).thenReturn(java.util.Optional.ofNullable(firstUser));
        boolean result = service.usernameExists("pesho");
        Assertions.assertTrue(result);
    }

    @Test
    void testFindByUsername() {

        when(mockUserRepository.findByUsername("pesho")).thenReturn(java.util.Optional.ofNullable(firstUser));
        UserEntity result = service.findByUsername("pesho");
        Assertions.assertEquals("pesho", result.getUsername());
        Assertions.assertEquals("pesho@pesho.bg", result.getEmail());
        Assertions.assertEquals("123456789", result.getPassword());
    }

    @Test
    void testGetAll() {
        firstUser.setOwnProjects(Set.of(firstProject, secondProject)).setProjects(Set.of(thirdProject, fourthProject, fifthProject));
        secondUser.setOwnProjects(Set.of(thirdProject, fourthProject, fifthProject)).setProjects(Set.of(firstProject, secondProject));
        when(mockUserRepository.findAll()).thenReturn(List.of(firstUser, secondUser));

        List<UserViewModel> result = service.getAll();

        Assertions.assertEquals("pesho", result.get(0).getUsername());
        Assertions.assertEquals("gosho", result.get(1).getUsername());
        Assertions.assertEquals("Pesho Peshov <br /> pesho@pesho.bg", result.get(0).getFullNameAndEmail());
        Assertions.assertTrue(result.get(0).getActiveProjects().contains("123"));
        Assertions.assertTrue(result.get(0).getActiveProjects().contains("456"));
        Assertions.assertTrue(result.get(0).getActiveCollabs().contains("789"));
        Assertions.assertTrue(result.get(0).getActiveCollabs().contains("012"));
        Assertions.assertEquals("Arts <br /> Company", result.get(0).getSectorAndType());
        Assertions.assertTrue(result.get(1).getActiveCollabs().contains("123"));
        Assertions.assertTrue(result.get(1).getActiveCollabs().contains("456"));
        Assertions.assertTrue(result.get(1).getActiveProjects().contains("789"));
        Assertions.assertTrue(result.get(1).getActiveProjects().contains("012"));
        Assertions.assertEquals("Archived <br />", result.get(1).getArchivesProjects());
    }

    @Test
    void testDeleteUser() {
        LogEntity log1 = new LogEntity();
        log1.setUser(firstUser);
        log1.setProject(thirdProject);
        LogEntity log2 = new LogEntity();
        log2.setUser(firstUser);
        log2.setProject(fourthProject);

        when(mockUserRepository.getOne(any())).thenReturn(firstUser);
        when(mockLogRepository.findByUser_Id(any())).thenReturn(List.of(log1, log2));

        List<LogEntity> result = service.deleteUser("1");

        Assertions.assertEquals("789", result.get(0).getProject().getName());
        Assertions.assertEquals("012", result.get(1).getProject().getName());
    }

    @Test
    void testGetProjectsByUser() {

        firstUser.setProjects(Set.of(firstProject, secondProject));
        when(mockUserRepository.findByUsername(any())).thenReturn(java.util.Optional.ofNullable(firstUser));

        Set<Project> result = service.getProjectsByUser("pesho");

        Assertions.assertTrue(result.contains(firstProject));
        Assertions.assertTrue(result.contains(secondProject));
    }

    @Test
    void testFindAllUsersExceptCurrent (){
        when(mockUserRepository.findAll()).thenReturn(List.of(firstUser, secondUser));
        Set<String> result = service.findAllUsernamesExceptCurrent();
        Assertions.assertTrue(result.contains(firstUser.getUsername()));
        Assertions.assertTrue(result.contains(secondUser.getUsername()));
        Assertions.assertEquals(2, result.size());
    }

    @Test
    void testChangeRole (){
        UserRoleEntity admin = new UserRoleEntity().setRole(UserRole.ADMIN);
        UserRoleEntity user = new UserRoleEntity().setRole(UserRole.USER);
        List<String> newRoles = List.of("ADMIN", "USER");
        when(mockUserRepository.findByUsername(any())).thenReturn(java.util.Optional.ofNullable(firstUser));
        when(mockUserRoleRepository.findByRole(UserRole.ADMIN)).thenReturn(java.util.Optional.ofNullable(admin));
        when(mockUserRoleRepository.findByRole(UserRole.USER)).thenReturn(java.util.Optional.ofNullable(user));

        List<UserRoleEntity> result =  service.changeRole("1", newRoles);
        Assertions.assertTrue(result.contains(admin));
        Assertions.assertTrue(result.contains(user));
    }

    //    Initialization methods

    private UserEntity getFirstUser() {
        UserEntity firstUser = new UserEntity();
        firstUser.setUsername("pesho")
                .setPassword("123456789")
                .setFirstName("Pesho")
                .setLastName("Peshov")
                .setEmail("pesho@pesho.bg")
                .setSector(Sector.Arts)
                .setUserType(UserType.Company);
        return firstUser;
    }


    private UserEntity getSecondUser() {
        UserEntity secondUser = new UserEntity();
        secondUser.setUsername("gosho")
                .setPassword("123456789")
                .setFirstName("Gosho")
                .setLastName("Goshov")
                .setEmail("gosho@gosho.bg")
                .setSector(Sector.Arts)
                .setUserType(UserType.Company);
        return secondUser;
    }

    private Equipment getEquipment() {
        Equipment equipment = new Equipment();
        equipment.setEquipmentName("Wood Workshop");
        return equipment;
    }

    private Lab getLab() {
        Lab lab = new Lab();
        lab.setName("Ideation");
        lab.setEquipment(equipment);
        return lab;
    }

    private ActivityType getActivityType() {
        ActivityType activityType = new ActivityType();
        activityType.setActivityName("Lecture");
        return activityType;
    }

    private List<Project> getProjects() {
        List<Project> projects = new ArrayList<>();
        Project firstProject = new Project();
        firstProject
                .setName("123")
                .setSector(Sector.IT)
                .setDescription("1234567890")
                .setNeededEquipment(equipment)
                .setLab(lab)
                .setActivityType(activityType)
                .setPromoter(firstUser)
                .setCollaborators(Set.of(secondUser))
                .setStartDate(LocalDateTime.of(2021, 5, 16, 10, 0))
                .setEndDate(LocalDateTime.of(2021, 5, 17, 10, 0));
        Project secondProject = new Project();
        secondProject
                .setName("456")
                .setSector(Sector.IT)
                .setDescription("1234567890")
                .setNeededEquipment(equipment)
                .setLab(lab)
                .setActivityType(activityType)
                .setPromoter(firstUser)
                .setCollaborators(Set.of(secondUser))
                .setStartDate(LocalDateTime.of(2021, 5, 16, 10, 0))
                .setEndDate(LocalDateTime.of(2021, 5, 17, 10, 0));

        Project thirdProject = new Project();
        thirdProject
                .setName("789")
                .setSector(Sector.IT)
                .setDescription("1234567890")
                .setNeededEquipment(equipment)
                .setLab(lab)
                .setActivityType(activityType)
                .setPromoter(secondUser)
                .setCollaborators(Set.of(firstUser))
                .setStartDate(LocalDateTime.of(2021, 5, 16, 10, 0))
                .setEndDate(LocalDateTime.of(2021, 5, 17, 10, 0));
        Project fourthProject = new Project();
        fourthProject
                .setName("012")
                .setSector(Sector.IT)
                .setDescription("1234567890")
                .setNeededEquipment(equipment)
                .setLab(lab)
                .setActivityType(activityType)
                .setPromoter(secondUser)
                .setCollaborators(Set.of(firstUser))
                .setStartDate(LocalDateTime.of(2021, 5, 16, 10, 0))
                .setEndDate(LocalDateTime.of(2021, 5, 17, 10, 0));

        Project fifthProject = new Project();
        fifthProject
                .setName("Archived")
                .setSector(Sector.IT)
                .setDescription("1234567890")
                .setNeededEquipment(equipment)
                .setLab(lab)
                .setActivityType(activityType)
                .setActive(false)
                .setPromoter(secondUser)
                .setCollaborators(Set.of(firstUser))
                .setStartDate(LocalDateTime.of(2021, 5, 16, 10, 0))
                .setEndDate(LocalDateTime.of(2021, 5, 17, 10, 0));

        projects.add(firstProject);
        projects.add(secondProject);
        projects.add(thirdProject);
        projects.add(fourthProject);
        projects.add(fifthProject);
        return projects;
    }
}



