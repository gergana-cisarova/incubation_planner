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

import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class IdeaServiceTest {

    private ModelMapper modelMapper;

    @Mock
    IdeaRepository mockIdeaRepository;
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
    ProjectRepository mockProjectRepository;


    @InjectMocks
    private IdeaServiceImpl serviceToTest;


    private Equipment equipment;
    private Idea current;
    private UserEntity user;
    private ActivityType activityType;
    private IdeaServiceModel ideaServiceModel;
    private IdeaViewModel ideaViewModel;
    private Lab lab;

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

        this.serviceToTest = new IdeaServiceImpl(mockIdeaRepository,
                modelMapper, mockUserRepository, mockActivityTypeRepository,
                mockLogRepository, mockEquipmentRepository);
    }


    @Test
    void testAddedIdea() {

        when(mockActivityTypeRepository.findByActivityName(any())).thenReturn(java.util.Optional.ofNullable(activityType));
        when(mockEquipmentRepository.findByEquipmentName(any())).thenReturn(java.util.Optional.ofNullable(equipment));
        when(mockUserRepository.findByUsername(any())).thenReturn(java.util.Optional.ofNullable(user));
        current.setId("1");
        when(mockIdeaRepository.save(any())).thenReturn(current);
        when(mockIdeaRepository.getOne(any())).thenReturn(current);
        ideaServiceModel
                .setId("1")
                .setName("123")
                .setActivityType("Lecture")
                .setDescription("1234567890")
                .setSector(Sector.IT)
                .setDuration(2)
                .setNeededEquipment("Computers_Multimedia_Printers")
                .setPromoter("pesho");

        serviceToTest.createIdea(ideaServiceModel);

        Assertions.assertEquals(ideaServiceModel.getName(), current.getName());
        Assertions.assertEquals(ideaServiceModel.getDescription(), current.getDescription());
        Assertions.assertEquals(ideaServiceModel.getActivityType(), current.getActivityType().getActivityName());
        Assertions.assertEquals(ideaServiceModel.getPromoter(), current.getPromoter().getUsername());
        Assertions.assertEquals(ideaServiceModel.getNeededEquipment(), current.getNeededEquipment().getEquipmentName());
        Assertions.assertEquals(ideaServiceModel.getDuration(), current.getDuration());

        Assertions.assertEquals(mockIdeaRepository.getOne("1").getName(), current.getName());

    }


    private Equipment getEquipment() {
        Equipment equipment = new Equipment();
        equipment.setEquipmentName("Computers_Multimedia_Printers");
        return equipment;
    }

    public ActivityType getActivityType() {
        ActivityType activityType = new ActivityType();
        activityType.setActivityName("Lecture");
        return activityType;
    }

    private UserEntity getUser() {
        UserEntity user = new UserEntity();
        user.setUsername("pesho")
                .setPassword("123456789")
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
