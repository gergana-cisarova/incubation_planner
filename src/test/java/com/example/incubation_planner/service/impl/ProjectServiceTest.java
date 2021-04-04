package com.example.incubation_planner.service.impl;

import com.example.incubation_planner.models.entity.*;
import com.example.incubation_planner.models.entity.enums.Sector;
import com.example.incubation_planner.models.entity.enums.UserType;
import com.example.incubation_planner.models.service.ProjectResultServiceModel;
import com.example.incubation_planner.models.service.ProjectServiceModel;
import com.example.incubation_planner.models.view.ProjectBasicViewModel;
import com.example.incubation_planner.models.view.ProjectDetailedViewModel;
import com.example.incubation_planner.models.view.ProjectResultViewModel;
import com.example.incubation_planner.repositories.*;
import com.example.incubation_planner.services.LabService;
import com.example.incubation_planner.services.UserService;
import com.example.incubation_planner.services.impl.*;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    ProjectServiceImpl service;
    ModelMapper modelMapper;
    UserService userService;
    LabService labService;
    @Mock
    ProjectRepository mockProjectRepository;
    @Mock
    LogRepository mockLogRepository;
    @Mock
    EquipmentRepository mockEquipmentRepository;
    @Mock
    LabRepository mockLabRepository;
    @Mock
    ActivityTypeRepository mockActivityTypeRepository;
    @Mock
    UserRepository mockUserRepository;
    @Mock
    UserRoleRepository mockUserRoleRepository;


    @InjectMocks
    EquipmentServiceImpl equipmentService;
    @InjectMocks
    IncubationUserService incubationUserService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Value("classpath:init/labs.json")
    Resource labsRes;
    @Autowired
    Gson gson;
    @Mock
    UserEntity mockUserEntity;
    @Mock
    Project mockProject;

    Equipment equipment;
    Lab lab;
    Project firstProject;
    Project secondProject;
    UserEntity firstUser;
    UserEntity secondUser;
    ActivityType activityType;
    ProjectServiceModel projectServiceModel;
    ProjectBasicViewModel projectBasicViewModel;
    ProjectDetailedViewModel projectDetailedViewModel;
    ProjectResultServiceModel projectResultServiceModel;

    @BeforeEach
    public void setUp() {
        this.modelMapper = new ModelMapper();
        Project mockProject = mock(Project.class);
        equipment = this.getEquipment();
        lab = this.getLab();
        activityType = this.getActivityType();
        firstUser = this.getFirstUser();
        firstProject = this.getFirstProject();
        secondProject = this.getSecondProject();
        secondUser = this.getSecondUser();
        projectServiceModel = new ProjectServiceModel();
        projectBasicViewModel = new ProjectBasicViewModel();
        projectDetailedViewModel = new ProjectDetailedViewModel();
        projectResultServiceModel = new ProjectResultServiceModel();

        userService = new UserServiceImpl(mockUserRoleRepository, mockUserRepository, passwordEncoder, modelMapper, incubationUserService, mockLogRepository);
        labService = new LabServiceImpl(labsRes, gson, mockLabRepository, equipmentService, modelMapper);

        this.service = new ProjectServiceImpl(mockProjectRepository, mockLogRepository,
                modelMapper, userService, mockActivityTypeRepository, labService,
                mockEquipmentRepository);
    }


    @Test
    void testAddedProject() {

        when(mockActivityTypeRepository.findByActivityName(any())).thenReturn(java.util.Optional.ofNullable(activityType));
        when(mockEquipmentRepository.findByEquipmentName(any())).thenReturn(java.util.Optional.ofNullable(equipment));
        when(mockUserRepository.findByUsername(any())).thenReturn(java.util.Optional.ofNullable(firstUser));
        when(mockLabRepository.findByName(any())).thenReturn(java.util.Optional.ofNullable(lab));
        projectServiceModel
                .setName("000")
                .setActivityType("Lecture")
                .setDescription("1234567890")
                .setSector(Sector.IT)
                .setNeededEquipment("Computers_Multimedia_Printers")
                .setPromoter("pesho")
                .setStartDate(LocalDateTime.of(2021, 5, 16, 10, 0))
                .setEndDate(LocalDateTime.of(2021, 5, 17, 10, 0));

        ProjectBasicViewModel viewModel = service.createProject(projectServiceModel);

        Assertions.assertEquals("000", viewModel.getName());
        Assertions.assertEquals("2021-05-16T10:00", viewModel.getStartDate());

    }

    @Test
    void testGetActiveProjectsOrderedByStartDate() {

        String project2StartDate = convertDate(secondProject);
        String currentStartDate = convertDate(firstProject);

        when(mockProjectRepository.findAllByActiveTrueOrderByStartDateAsc()).thenReturn(List.of(secondProject, firstProject));

        List<ProjectBasicViewModel> result = service.getActiveProjectsOrderedbyStartDate();

        Assertions.assertEquals("000", result.get(0).getName());
        Assertions.assertEquals("123", result.get(1).getName());
        Assertions.assertEquals(project2StartDate, result.get(0).getStartDate());
        Assertions.assertEquals(currentStartDate, result.get(1).getStartDate());
    }

    @Test
    void testExtractProjectModel() {
        when(mockProjectRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(firstProject));
        when(mockUserRepository.findByUsername(any())).thenReturn(java.util.Optional.ofNullable(firstUser));
        String duration = String.format("%02d %s %s (%02d:%02d) - %02d %s %s (%02d:%02d) <br />",
                firstProject.getStartDate().getDayOfMonth(), firstProject.getStartDate().getMonth(), firstProject.getStartDate().getYear(), firstProject.getStartDate().getHour(), firstProject.getStartDate().getMinute(),
                firstProject.getEndDate().getDayOfMonth(), firstProject.getEndDate().getMonth(), firstProject.getEndDate().getYear(), firstProject.getEndDate().getHour(), firstProject.getEndDate().getMinute());

        ProjectDetailedViewModel model = service.extractProjectModel("1");
        Assertions.assertEquals("123", model.getName());
        Assertions.assertEquals("1234567890", model.getDescription());
        Assertions.assertEquals("IT", model.getSector().name());
        Assertions.assertEquals("Pesho Peshov", model.getPromoter());
        Assertions.assertEquals(duration, model.getDuration());
        Assertions.assertEquals("Carnegie1", model.getLab());
    }


    @Test
    void testDeleteProject() {
        firstProject.setId("1");
        LogEntity log1 = new LogEntity();
        log1.setProject(firstProject);
        LogEntity log2 = new LogEntity();
        log2.setProject(secondProject);
        when(mockLogRepository.findByProject_Id(any())).thenReturn(List.of(log1, log2));

        List<String> result = service.deleteProject("1");

        Assertions.assertEquals("123", result.get(0));
        Assertions.assertEquals("000", result.get(1));


    }

    @Test
    void testGetUserProjectsOrderedByStartDate() {

        String project2StartDate = convertDate(secondProject);
        String currentStartDate = convertDate(firstProject);

        when(mockUserRepository.findByUsername(any())).thenReturn(Optional.ofNullable(firstUser));
        when(mockProjectRepository.findAllByActiveAndPromoterOrderByStartDate(true, firstUser)).thenReturn(List.of(secondProject, firstProject));

        List<ProjectBasicViewModel> result = service.getUserProjectsOrderedByStartDate("1");

        Assertions.assertEquals("000", result.get(0).getName());
        Assertions.assertEquals("123", result.get(1).getName());
        Assertions.assertEquals(project2StartDate, result.get(0).getStartDate());
        Assertions.assertEquals(currentStartDate, result.get(1).getStartDate());
    }

    @Test
    void testGetUserCollaborationsOrderedByStartDate() {
        secondUser.setProjects(Set.of(firstProject, secondProject));

        String project2StartDate = convertDate(secondProject);
        String currentStartDate = convertDate(firstProject);

        when(mockUserRepository.findByUsername(any())).thenReturn(Optional.of(secondUser));

        List<ProjectBasicViewModel> result = service.getUserCollaborationsOrderedByStartDate(secondUser.getUsername());

        Assertions.assertEquals("000", result.get(0).getName());
        Assertions.assertEquals("123", result.get(1).getName());
        Assertions.assertEquals(project2StartDate, result.get(0).getStartDate());
        Assertions.assertEquals(currentStartDate, result.get(1).getStartDate());
    }

    @Test
    void verifyDeleteProjectsOfUser() {

        ProjectServiceImpl mockService = mock(ProjectServiceImpl.class);

        mockService.deleteProjectsOfUser(firstUser.getId());
        doNothing().when(mockService).deleteProjectsOfUser(isA(String.class));

        mockService.deleteProjectsOfUser("1");
        verify(mockService, times(1)).deleteProjectsOfUser("1");

        ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(mockService).deleteProjectsOfUser(valueCapture.capture());
        mockService.deleteProjectsOfUser("1");

        Assertions.assertEquals("1", valueCapture.getValue());

    }

    @Test
    void testFindProjectById() {
        when(mockProjectRepository.getOne(any())).thenReturn(firstProject);
        ProjectServiceModel result = service.findProjectById("1");

        Assertions.assertEquals("123", result.getName());
        Assertions.assertEquals("pesho", result.getPromoter());
    }

    @Test
    void testArchiveProject() {
        when(mockProjectRepository.getOne(any())).thenReturn(firstProject);
        service.archiveProject("1");

        Assertions.assertFalse(firstProject.isActive());
    }

    @Test
    void testFindProjectOwnerStr() {
        when(mockProjectRepository.getOne(any())).thenReturn(firstProject);
        String owner = service.findProjectOwnerStr("1");

        Assertions.assertEquals("pesho", owner);
    }

    @Test
    void testJoinProject() {
        when(mockUserRepository.findByUsername(any())).thenReturn(Optional.ofNullable(secondUser));
        when(mockProjectRepository.findById(any())).thenReturn(Optional.ofNullable(firstProject));

        boolean result = service.joinProject("123", "pesho");
        Set<Project> userProjects = secondUser.getProjects();
        Set<UserEntity> projectCollaborators = firstProject.getCollaborators();

        Assertions.assertTrue(result);
        Assertions.assertTrue(userProjects.contains(firstProject));
        Assertions.assertTrue(projectCollaborators.contains(secondUser));
    }
    @Test
    void testJoinProjectThrowsWhenProjectNotExisting() {
        when(mockUserRepository.findByUsername(any())).thenReturn(Optional.ofNullable(secondUser));
        when(mockProjectRepository.findById(any())).thenReturn(null);
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.joinProject("123", "pesho");
        });

        String expectedMessage = "User could not join project";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void testLeaveProject() {
        secondUser.addProject(firstProject);
        when(mockUserRepository.findByUsername(any())).thenReturn(Optional.ofNullable(secondUser));
        when(mockProjectRepository.findById(any())).thenReturn(Optional.ofNullable(firstProject));

        service.leaveProject("123", "pesho");
        Set<Project> userProjects = secondUser.getProjects();
        Set<UserEntity> projectCollaborators = firstProject.getCollaborators();

        Assertions.assertFalse(userProjects.contains(firstProject));
        Assertions.assertFalse(projectCollaborators.contains(secondUser));
    }

    @Test
    void testLeaveProjectThrowsWhenProjectNotExisting() {
        when(mockUserRepository.findByUsername(any())).thenReturn(Optional.ofNullable(secondUser));
        when(mockProjectRepository.findById(any())).thenReturn(null);
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.leaveProject("123", "pesho");
        });

        String expectedMessage = "User could not leave project";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testCheckIfCollaborating() {
        secondUser.addProject(firstProject);
        when(mockUserRepository.findByUsername(any())).thenReturn(Optional.ofNullable(secondUser));
        when(mockProjectRepository.findById(any())).thenReturn(Optional.ofNullable(firstProject));
        boolean result = service.checkIfCollaborating("123", "pesho");
        Assertions.assertTrue(result);
    }

    @Test
    void testExtractProjectServiceModel() {
        when(mockProjectRepository.findById(any())).thenReturn(Optional.ofNullable(firstProject));
        ProjectServiceModel result = service.extractProjectServiceModel("1");

        Assertions.assertEquals("123", result.getName());
        Assertions.assertEquals("1234567890", result.getDescription());
        Assertions.assertEquals("pesho", result.getPromoter());
        Assertions.assertEquals("Carnegie1", result.getLab());
        Assertions.assertEquals(LocalDateTime.of(2021, 5, 16, 10, 0), result.getStartDate());
    }

    @Test
    void testGetProjectPromoter() {
        when(mockProjectRepository.getOne(any())).thenReturn(firstProject);
        String result = service.getProjectPromoter("1");
        Assertions.assertEquals("pesho", result);
    }

    @Test
    void testUpdateProject() {
        when(mockProjectRepository.getOne(any())).thenReturn(firstProject);
        when(mockActivityTypeRepository.findByActivityName(any())).thenReturn(Optional.ofNullable(activityType));
        when(mockEquipmentRepository.findByEquipmentName(any())).thenReturn(Optional.ofNullable(equipment));
        when(mockLabRepository.findByName(any())).thenReturn(Optional.ofNullable(lab));
        projectServiceModel
                .setName("000")
                .setActivityType("Lecture")
                .setDescription("updated1234")
                .setSector(Sector.Arts)
                .setNeededEquipment("Computers_Multimedia_Printers")
                .setPromoter("pesho")
                .setStartDate(LocalDateTime.of(2021, 5, 16, 10, 0))
                .setEndDate(LocalDateTime.of(2021, 5, 17, 10, 0));

        service.updateProject("1", projectServiceModel);

        Assertions.assertEquals("000", firstProject.getName());
        Assertions.assertEquals("updated1234", firstProject.getDescription());
        Assertions.assertEquals("Arts", firstProject.getSector().name());
        Assertions.assertEquals("Lecture", firstProject.getActivityType().getActivityName());
        Assertions.assertEquals(LocalDateTime.of(2021, 5, 16, 10, 0), firstProject.getStartDate());
        Assertions.assertEquals(LocalDateTime.of(2021, 5, 17, 10, 0), firstProject.getEndDate());
    }


    @Test
    void testPublishProjectResult() {
        projectResultServiceModel
                .setName("123")
                .setDescription("12345678901234567890123456789012345678901234567890");

        when(mockProjectRepository.getOne(any())).thenReturn(firstProject);
        service.publishProjectResult(projectResultServiceModel);
        Assertions.assertEquals("12345678901234567890123456789012345678901234567890", firstProject.getResult());
    }
    @Test
    void testExtractProjectResultServiceModel() {

        when(mockProjectRepository.findById(any())).thenReturn(Optional.ofNullable(firstProject));

        ProjectResultServiceModel resultServiceModel = service.extractProjectResultServiceModel("1");
        Assertions.assertEquals("123", resultServiceModel.getName());
        Assertions.assertEquals("IT", resultServiceModel.getSector().name());

    }

    @Test
    void testGetResults() {
        firstProject.setResult("12345678901234567890123456789012345678901234567890");
        secondProject.setResult("Another12345678901234567890123456789012345678901234567890");
        when(mockProjectRepository.findAllResultsBySector(any())).thenReturn(List.of(firstProject, secondProject));

        List<ProjectResultViewModel> result = service.getResults("IT");
        Assertions.assertEquals("12345678901234567890123456789012345678901234567890", result.get(0).getResult());
        Assertions.assertEquals("123", result.get(0).getName());
        Assertions.assertEquals("Another12345678901234567890123456789012345678901234567890", result.get(1).getResult());
        Assertions.assertEquals("000", result.get(1).getName());
    }
    @Test
    void testGetDurationInDays() {
        projectServiceModel
                .setStartDate(LocalDateTime.of(2021, 5, 16, 10, 0))
                .setEndDate(LocalDateTime.of(2021, 5, 17, 10, 0));

        long result = service.getDurationInDays(projectServiceModel);
        Assertions.assertEquals(1L, result);


    }

    //    Initialization methods
    private Equipment getEquipment() {
        Equipment equipment = new Equipment();
        equipment.setEquipmentName("Computers_Multimedia_Printers");
        return equipment;
    }

    private ActivityType getActivityType() {
        ActivityType activityType = new ActivityType();
        activityType.setActivityName("Lecture");
        return activityType;
    }

    private UserEntity getFirstUser() {
        UserEntity user = new UserEntity();
        user.setUsername("pesho")
                .setPassword("123456789")
                .setFirstName("Pesho")
                .setLastName("Peshov")
                .setEmail("pesho@pesho.bg")
                .setSector(Sector.Arts)
                .setUserType(UserType.Company);
        return user;
    }

    private Project getFirstProject() {

        Project project = new Project();
        project
                .setName("123")
                .setSector(Sector.IT)
                .setDescription("1234567890")
                .setNeededEquipment(equipment)
                .setLab(lab)
                .setActivityType(activityType)
                .setActive(true)
                .setPromoter(firstUser)
                .setStartDate(LocalDateTime.of(2021, 5, 16, 10, 0))
                .setEndDate(LocalDateTime.of(2021, 5, 17, 10, 0));
        return project;
    }

    private Project getSecondProject() {
        Project project2 = new Project();
        project2
                .setName("000")
                .setSector(Sector.IT)
                .setDescription("1234567890000")
                .setNeededEquipment(equipment)
                .setLab(lab)
                .setActivityType(activityType)
                .setPromoter(firstUser)
                .setStartDate(LocalDateTime.of(2021, 5, 10, 10, 0))
                .setEndDate(LocalDateTime.of(2021, 5, 12, 10, 0));
        return project2;
    }

    private UserEntity getSecondUser() {
        UserEntity user2 = new UserEntity();
        user2.setUsername("gosho")
                .setPassword("123456789")
                .setFirstName("Gosho")
                .setLastName("Goshov")
                .setEmail("gosho@gosho.bg")
                .setSector(Sector.Arts)
                .setUserType(UserType.Company);
        return user2;
    }

    private Lab getLab() {
        Lab lab = new Lab();
        lab.setName("Carnegie1");
        lab.setEquipment(equipment);
        return lab;
    }

    private String convertDate(Project project) {
        return String.format("%02d %s %s (%02d:%02d)",
                project.getStartDate().getDayOfMonth(), project.getStartDate().getMonth(),
                project.getStartDate().getYear(), project.getStartDate().getHour(),
                project.getStartDate().getMinute());
    }

}
