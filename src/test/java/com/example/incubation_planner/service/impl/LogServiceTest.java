package com.example.incubation_planner.service.impl;

import com.example.incubation_planner.models.entity.*;
import com.example.incubation_planner.models.entity.enums.Sector;
import com.example.incubation_planner.models.entity.enums.UserType;
import com.example.incubation_planner.models.view.AddIdeaLogViewModel;
import com.example.incubation_planner.models.view.JoinProjectLogViewModel;
import com.example.incubation_planner.repositories.*;
import com.example.incubation_planner.services.IdeaService;
import com.example.incubation_planner.services.LabService;
import com.example.incubation_planner.services.ProjectService;
import com.example.incubation_planner.services.UserService;
import com.example.incubation_planner.services.impl.IncubationUserService;
import com.example.incubation_planner.services.impl.LogServiceImpl;
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
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class LogServiceTest {

    LogServiceImpl service;

    ModelMapper modelMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Mock
    LogRepository mockLogRepository;
    @Mock
    UserRepository mockUserRepository;
    @Mock
    UserRoleRepository mockUserRoleRepository;
    @Mock
    ProjectRepository mockProjectRepository;
    @Mock
    IdeaRepository mockIdeaRepository;
    @Mock
    EquipmentRepository mockEquipmentRepository;
    @Mock
    LabRepository mockLabRepository;
    @Mock
    ActivityTypeRepository mockActivityTypeRepository;

    @Autowired
    IncubationUserService incubationUserService;
    @Autowired
    ProjectService projectService;
    @Autowired
    UserService userService;
    @Autowired
    IdeaService ideaService;
    @Autowired
    LabService labService;
    LogEntity firstLog;
    LogEntity secondLog;
    LogEntity thirdLog;
    LogEntity fourthLog;


    @BeforeEach
    public void setUp() {
        this.modelMapper = new ModelMapper();
        this.service = new LogServiceImpl(mockLogRepository, userService, projectService, ideaService, modelMapper);
        firstLog = getLogs().get(0);
        secondLog = getLogs().get(1);
        thirdLog = getLogs().get(2);
        fourthLog = getLogs().get(3);
    }

    @Test
    void testFindAllIdeaAddLogs() {
        when(mockLogRepository.findAllByIdeaNotNullOrderByTimeDesc()).thenReturn(List.of(fourthLog, thirdLog));
        List<AddIdeaLogViewModel> result = service.findAllIdeaAddLogs();

        Assertions.assertEquals("pesho", result.get(0).getUser());
        Assertions.assertEquals("pesho", result.get(1).getUser());
        Assertions.assertEquals("012", result.get(0).getIdea());
        Assertions.assertEquals("789", result.get(1).getIdea());
        Assertions.assertEquals("18 MARCH 2021 (10:00)", result.get(0).getDateTime());
        Assertions.assertEquals("AddIdea", result.get(0).getAction());
    }

    @Test
    void testFindAllJoinProjectLogs() {
        when(mockLogRepository.findAllByProjectNotNullOrderByTimeDesc()).thenReturn(List.of(secondLog, firstLog));
        List<JoinProjectLogViewModel> result = service.findAllJoinProjectLogs();

        Assertions.assertEquals("pesho", result.get(0).getUser());
        Assertions.assertEquals("pesho", result.get(1).getUser());
        Assertions.assertEquals("456", result.get(0).getProject());
        Assertions.assertEquals("123", result.get(1).getProject());
        Assertions.assertEquals("19 MARCH 2021 (10:00)", result.get(0).getDateTime());
        Assertions.assertEquals("JoinProject", result.get(0).getAction());
    }

    @Test
    void testGetStatsJoinProjectActivity() {
        when(mockLogRepository.findAllByProjectNotNullOrderByTimeDesc()).thenReturn(List.of(secondLog, firstLog));
        Map<Integer, Integer> result = service.getStatsJoinProjectActivity();
        int dayOfWeek2 = secondLog.getTime().getDayOfWeek().getValue();
        int dayOfWeek1 = firstLog.getTime().getDayOfWeek().getValue();

        Assertions.assertEquals(1, result.get(dayOfWeek2));
        Assertions.assertEquals(1, result.get(dayOfWeek1));
    }

    @Test
    void testGetStatsIdeasCreated() {
        when(mockLogRepository.findAllByIdeaNotNullOrderByTimeDesc()).thenReturn(List.of(fourthLog, thirdLog));
        Map<Integer, Integer> result = service.getStatsIdeasCreated();
        int dayOfWeek2 = fourthLog.getTime().getDayOfWeek().getValue();
        int dayOfWeek1 = thirdLog.getTime().getDayOfWeek().getValue();

        Assertions.assertEquals(1, result.get(dayOfWeek2));
        Assertions.assertEquals(1, result.get(dayOfWeek1));
    }

    //    Initialization methods

    private List<LogEntity> getLogs() {
        UserEntity firstUser = new UserEntity();
        firstUser.setUsername("pesho")
                .setPassword("123456789")
                .setFirstName("Pesho")
                .setLastName("Peshov")
                .setEmail("pesho@pesho.bg")
                .setSector(Sector.Arts)
                .setUserType(UserType.Company);
        Equipment equipment = new Equipment();
        equipment.setEquipmentName("Wood Workshop");
        Lab lab = new Lab();
        lab.setName("Ideation");
        lab.setEquipment(equipment);
        ActivityType activityType = new ActivityType();
        activityType.setActivityName("Lecture");

        Project firstProject = new Project();
        firstProject
                .setName("123")
                .setSector(Sector.IT)
                .setDescription("1234567890")
                .setNeededEquipment(equipment)
                .setLab(lab)
                .setActivityType(activityType)
                .setPromoter(firstUser)
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
                .setStartDate(LocalDateTime.of(2021, 5, 16, 10, 0))
                .setEndDate(LocalDateTime.of(2021, 5, 17, 10, 0));

        Idea firstIdea = new Idea();
        firstIdea
                .setName("789")
                .setSector(Sector.IT)
                .setDescription("1234567890")
                .setNeededEquipment(equipment)
                .setDuration(2)
                .setActivityType(activityType)
                .setPromoter(firstUser);
        Idea secondIdea = new Idea();
        secondIdea
                .setName("012")
                .setSector(Sector.IT)
                .setDescription("1234567890")
                .setNeededEquipment(equipment)
                .setDuration(2)
                .setActivityType(activityType)
                .setPromoter(firstUser);

        LogEntity firstLog = new LogEntity();
        firstLog
                .setProject(firstProject)
                .setUser(firstUser)
                .setAction("JoinProject")
                .setTime(LocalDateTime.of(2021, 3, 17, 10, 0));

        LogEntity secondLog = new LogEntity();
        secondLog
                .setProject(secondProject)
                .setUser(firstUser)
                .setAction("JoinProject")
                .setTime(LocalDateTime.of(2021, 3, 19, 10, 0));

        LogEntity thirdLog = new LogEntity();
        thirdLog
                .setIdea(firstIdea)
                .setUser(firstUser)
                .setAction("AddIdea")
                .setTime(LocalDateTime.of(2021, 3, 17, 10, 0));
        LogEntity fourthLog = new LogEntity();
        fourthLog
                .setIdea(secondIdea)
                .setUser(firstUser)
                .setAction("AddIdea")
                .setTime(LocalDateTime.of(2021, 3, 18, 10, 0));

        return List.of(firstLog, secondLog, thirdLog, fourthLog);
    }


}
