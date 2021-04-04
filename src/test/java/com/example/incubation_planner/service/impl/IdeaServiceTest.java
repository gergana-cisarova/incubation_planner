package com.example.incubation_planner.service.impl;

import com.example.incubation_planner.models.entity.*;
import com.example.incubation_planner.models.entity.enums.Sector;
import com.example.incubation_planner.models.entity.enums.UserType;
import com.example.incubation_planner.models.service.IdeaServiceModel;
import com.example.incubation_planner.models.view.IdeaViewModel;
import com.example.incubation_planner.repositories.*;
import com.example.incubation_planner.services.impl.IdeaServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class IdeaServiceTest {

    ModelMapper modelMapper;

    @Mock
    IdeaRepository mockIdeaRepository;
    @Mock
    LogRepository mockLogRepository;
    @Mock
    EquipmentRepository mockEquipmentRepository;

    @Mock
    ActivityTypeRepository mockActivityTypeRepository;
    @Mock
    UserRepository mockUserRepository;


    @InjectMocks
    IdeaServiceImpl service;


    Equipment equipment;
    Idea current;
    UserEntity user;
    ActivityType activityType;
    IdeaServiceModel ideaServiceModel;
    IdeaViewModel ideaViewModel;
    Lab lab;

    @BeforeEach
    public void setUp() {
        this.modelMapper = new ModelMapper();
        equipment = this.getEquipment();
        lab = this.getLab();
        activityType = this.getActivityType();
        user = this.getUser();
        current = this.getIdea();
        ideaServiceModel = new IdeaServiceModel();
        ideaViewModel = new IdeaViewModel();

        this.service = new IdeaServiceImpl(mockIdeaRepository,
                modelMapper, mockUserRepository, mockActivityTypeRepository,
                mockLogRepository, mockEquipmentRepository);
    }


    @Test
    void testAddedIdea() {

        when(mockActivityTypeRepository.findByActivityName(any())).thenReturn(java.util.Optional.ofNullable(activityType));
        when(mockEquipmentRepository.findByEquipmentName(any())).thenReturn(java.util.Optional.ofNullable(equipment));
        when(mockUserRepository.findByUsername(any())).thenReturn(java.util.Optional.ofNullable(user));
        ideaServiceModel
                .setName("123")
                .setActivityType("Lecture")
                .setDescription("1234567890")
                .setSector(Sector.IT)
                .setDuration(2)
                .setNeededEquipment("Computers_Multimedia_Printers")
                .setPromoter("pesho");

        IdeaViewModel ideaViewModel = service.createIdea(ideaServiceModel);

        Assertions.assertEquals("123", ideaViewModel.getName());
        Assertions.assertEquals(2, ideaViewModel.getDuration());

    }

    @Test
    void testGetAll() {
        Idea idea2 = new Idea();
        idea2
                .setName("12345")
                .setSector(Sector.IT)
                .setDescription("1234567890")
                .setNeededEquipment(equipment)
                .setDuration(2)
                .setStatus("Accepted")
                .setActivityType(activityType)
                .setPromoter(user);
        when(mockIdeaRepository.findAllByOrderByStatusDesc()).thenReturn(List.of(current, idea2));
        when(mockUserRepository.findByUsername(any())).thenReturn(java.util.Optional.ofNullable(user));
        String fullNameCurrent = String.format("%s %s", current.getPromoter().getFirstName(), current.getPromoter().getLastName());
        String fullNameIdea2 = String.format("%s %s", current.getPromoter().getFirstName(), current.getPromoter().getLastName());

        List<IdeaViewModel> result = service.getAll();

        Assertions.assertEquals(current.getName(), result.get(0).getName());
        Assertions.assertEquals(idea2.getName(), result.get(1).getName());
        Assertions.assertEquals("Pending", result.get(0).getStatus());
        Assertions.assertEquals("Accepted", result.get(1).getStatus());
        Assertions.assertEquals(fullNameCurrent, result.get(0).getPromoter());
        Assertions.assertEquals(fullNameIdea2, result.get(1).getPromoter());

    }

    @Test
    void markIdeaAsAcceptedTestWithValidIdea() {
        when(mockIdeaRepository.findById("1")).thenReturn(java.util.Optional.ofNullable(current));

        boolean isAccepted = service.markIdeaAsAccepted("1");

        Assertions.assertTrue(isAccepted);
    }

    @Test
    void markIdeaAsAcceptedTestWithNullIdea() {
        when(mockIdeaRepository.findById("1")).thenReturn(java.util.Optional.empty());

        boolean isAccepted = service.markIdeaAsAccepted("1");

        Assertions.assertFalse(isAccepted);
    }


    @Test
    void DeleteIdeaTest() {
        LogEntity log = new LogEntity();
        log.setIdea(current).setAction("action").setTime(LocalDateTime.now()).setUser(user);
        when(mockLogRepository.findByIdea_Id(any())).thenReturn(List.of(log));

        List<String> deletedLogs = service.deleteIdea("1");

        Assertions.assertEquals(current.getName(), deletedLogs.get(0));

    }

    @Test
    void extractIdeaModelTest() {
        when(mockIdeaRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(current));

        IdeaServiceModel result = service.extractIdeaModel("1");
        Assertions.assertEquals(current.getName(), result.getName());
        Assertions.assertEquals(current.getPromoter().getUsername(), result.getPromoter());
        Assertions.assertEquals(current.getActivityType().getActivityName(), result.getActivityType());
        Assertions.assertEquals(current.getNeededEquipment().getEquipmentName(), result.getNeededEquipment());
    }


    @Test
    void getDurationOfIdeaTest() {
        when(mockIdeaRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(current));

        int duration = service.getDurationOfIdea("1");
        Assertions.assertEquals(2, duration);

    }

    @Test
    void deleteIdeasOfUserTest() {

        IdeaServiceImpl mockService = mock(IdeaServiceImpl.class);
        mockService.deleteIdeasOfUser(user.getId());
        verify(mockService, times(1)).deleteIdeasOfUser(user.getId());

        when(mockIdeaRepository.findAllByPromoterId(any())).thenReturn(List.of(current));
        List<String> result = service.deleteIdeasOfUser(user.getId());
        Assertions.assertEquals(current.getName(), result.get(0));
    }


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

    private UserEntity getUser() {
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


    private Idea getIdea() {

        Idea idea = new Idea();
        idea
                .setName("123")
                .setSector(Sector.IT)
                .setDescription("1234567890")
                .setNeededEquipment(equipment)
                .setDuration(2)
                .setActivityType(activityType)
                .setPromoter(user);
        return idea;
    }

    private Lab getLab() {
        Lab lab = new Lab();
        lab.setName("Monnet");
        lab.setEquipment(equipment);
        return lab;
    }

}
