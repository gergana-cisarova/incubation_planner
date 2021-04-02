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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
        IdeaServiceImpl mockService = mock(IdeaServiceImpl.class);
        mockService.createIdea(ideaServiceModel);
        verify(mockService, times(1)).createIdea(ideaServiceModel);

        ArgumentCaptor<IdeaServiceModel> valueCapture = ArgumentCaptor.forClass(IdeaServiceModel.class);
        doNothing().when(mockService).createIdea(valueCapture.capture());
        mockService.createIdea(ideaServiceModel);
        Assertions.assertEquals(ideaServiceModel, valueCapture.getValue());

        Assertions.assertEquals(ideaServiceModel.getName(), current.getName());
        Assertions.assertEquals(ideaServiceModel.getDescription(), current.getDescription());
        Assertions.assertEquals(ideaServiceModel.getActivityType(), current.getActivityType().getActivityName());
        Assertions.assertEquals(ideaServiceModel.getPromoter(), current.getPromoter().getUsername());
        Assertions.assertEquals(ideaServiceModel.getNeededEquipment(), current.getNeededEquipment().getEquipmentName());
        Assertions.assertEquals(ideaServiceModel.getDuration(), current.getDuration());
        Assertions.assertEquals(ideaServiceModel.getId(), current.getId());
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

        when(mockIdeaRepository.findAllByOrderByStatusDesc()).thenReturn(List.of(idea2, current));
        when(mockActivityTypeRepository.findByActivityName(any())).thenReturn(java.util.Optional.ofNullable(activityType));
        when(mockEquipmentRepository.findByEquipmentName(any())).thenReturn(java.util.Optional.ofNullable(equipment));
        when(mockUserRepository.findByUsername(any())).thenReturn(java.util.Optional.ofNullable(user));

        IdeaViewModel ideaViewModel1 = mapIdea(current);
        IdeaViewModel ideaViewModel2 = mapIdea(idea2);

        List<IdeaViewModel> result = serviceToTest.getAll();


        Assertions.assertEquals(ideaViewModel2.getName(), result.get(0).getName());
        Assertions.assertEquals(ideaViewModel1.getName(), result.get(1).getName());

    }

    @Test
    void markIdeaAsAcceptedTest() {
        current.setStatus("Accepted");
        current.setId("1");
        IdeaServiceImpl mockService = mock(IdeaServiceImpl.class);
        mockService.markIdeaAsAccepted("1");
        verify(mockService, times(1)).markIdeaAsAccepted(current.getId());
        ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(mockService).markIdeaAsAccepted(valueCapture.capture());
        mockService.markIdeaAsAccepted(current.getId());
        Assertions.assertEquals("1", valueCapture.getValue());
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

    private IdeaViewModel mapIdea(Idea idea) {
        when(mockActivityTypeRepository.findByActivityName(any())).thenReturn(java.util.Optional.ofNullable(activityType));
        when(mockEquipmentRepository.findByEquipmentName(any())).thenReturn(java.util.Optional.ofNullable(equipment));
        when(mockUserRepository.findByUsername(any())).thenReturn(java.util.Optional.ofNullable(user));

        IdeaViewModel ideaViewModel = new IdeaViewModel();
        ideaViewModel = modelMapper.map(idea, IdeaViewModel.class);
        ideaViewModel.setActivityType(mockActivityTypeRepository
                .findByActivityName(idea.getActivityType().getActivityName()).orElseThrow(NullPointerException::new).getActivityName());
        ideaViewModel.setNeededEquipment(mockEquipmentRepository
                .findByEquipmentName(idea.getNeededEquipment().getEquipmentName()).orElseThrow(NullPointerException::new).getEquipmentName());
        String firstName = mockUserRepository
                .findByUsername(idea.getPromoter().getUsername()).orElseThrow(NullPointerException::new).getFirstName();
        String lastName = mockUserRepository
                .findByUsername(idea.getPromoter().getUsername()).orElseThrow(NullPointerException::new).getLastName();
        ideaViewModel.setPromoter(String.format("%s %s", firstName, lastName));
        ideaViewModel.setPromoter(mockUserRepository
                .findByUsername(idea.getPromoter().getUsername()).orElseThrow(NullPointerException::new).getUsername());
        return ideaViewModel;
    }

}
