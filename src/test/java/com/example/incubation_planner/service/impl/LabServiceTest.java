package com.example.incubation_planner.service.impl;

import com.example.incubation_planner.models.entity.*;
import com.example.incubation_planner.models.entity.enums.Sector;
import com.example.incubation_planner.repositories.EquipmentRepository;
import com.example.incubation_planner.repositories.LabRepository;
import com.example.incubation_planner.services.EquipmentService;
import com.example.incubation_planner.services.LabService;
import com.example.incubation_planner.services.impl.EquipmentServiceImpl;
import com.example.incubation_planner.services.impl.LabServiceImpl;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class LabServiceTest {


    LabService service;
    ModelMapper modelMapper;

    @Value("classpath:init/labs.json")
    Resource labsRes;
    @Value("classpath:init/equipment.json")
    Resource equipmentRes;
    @Autowired
    Gson gson;
    @Mock
    LabRepository mockLabRepository;
    @Mock
    EquipmentRepository mockEquipmentRepository;

    EquipmentService equipmentService;
    Equipment firstEquipment;
    Equipment secondEquipment;
    Lab firstLab;
    Lab secondLab;
    Lab thirdLab;
    Project firstProject;
    Project secondProject;
    Project thirdProject;
    Project oldProject;
    Project fourthProject;
    @Mock
    Lab mockLab;

    @BeforeEach
    public void setUp() {
        this.modelMapper = new ModelMapper();
        firstEquipment = this.getFirstEquipment();
        secondEquipment = this.getSecondEquipment();
        firstLab = this.getFirstLab();
        secondLab = this.getSecondLab();
        thirdLab = this.getThirdLab();
        firstProject = this.getProjects().get(0);
        secondProject = this.getProjects().get(1);
        thirdProject = this.getProjects().get(3);
        fourthProject = this.getProjects().get(4);
        oldProject = this.getProjects().get(2);
        equipmentService = new EquipmentServiceImpl(equipmentRes, gson, mockEquipmentRepository, modelMapper);
        this.service = new LabServiceImpl(labsRes, gson, mockLabRepository, equipmentService, modelMapper);
    }


    @Test
    void testGetAllLabs() {
        when(mockLabRepository.findAll()).thenReturn(List.of(firstLab, secondLab));
        List<String> result = service.getAllLabs();
        Assertions.assertEquals("Carnegie1", result.get(0));
        Assertions.assertEquals("Tesla", result.get(1));
    }

    @Test
    void testGetSuitableLabs() {
        when(mockLabRepository.findAllByEquipment_EquipmentName("Innovation Space")).thenReturn(List.of(secondLab));
        List<String> result = service.findSuitableLabs("Innovation Space");
        Assertions.assertEquals("Tesla", result.get(0));
    }

    @Test
    void testFindLab() {
        when(mockLabRepository.findByName("Tesla")).thenReturn(Optional.ofNullable(secondLab));
        Lab result = service.findLab("Tesla");
        Assertions.assertEquals("Tesla", result.getName());
    }

    @Test
    void testGetSuitableLabsWithProjects() {
        secondLab.setProjects(List.of(firstProject, secondProject, oldProject));
        thirdLab.setProjects(List.of(thirdProject));
        when(mockLabRepository.findAllByEquipment_EquipmentName("Innovation Space")).thenReturn(List.of(secondLab, thirdLab));

        Map<String, String> result = service.getSuitableLabsWithProjects("Innovation Space");

        Assertions.assertTrue(result.keySet().contains("Tesla2"));
        Assertions.assertTrue(result.keySet().contains("Tesla"));
        Assertions.assertEquals("16 MAY 2023 (10:00) - 17 MAY 2023 (10:00) <br />20 MAY 2023 (10:00) - 22 MAY 2023 (10:00) <br />", result.get("Tesla"));
        Assertions.assertEquals("24 APRIL 2023 (10:00) - 26 APRIL 2023 (10:00) <br />", result.get("Tesla2"));
    }

    @Test
    void testGetAllLabsWithProjects() {
        firstLab.setProjects(List.of(fourthProject));
        secondLab.setProjects(List.of(firstProject, secondProject, oldProject));
        thirdLab.setProjects(List.of(thirdProject));
        when(mockLabRepository.findAll()).thenReturn(List.of(firstLab, secondLab, thirdLab));

        Map<String, String> result = service.getAllLabsWithProjects();

        Assertions.assertTrue(result.keySet().contains("Tesla2"));
        Assertions.assertTrue(result.keySet().contains("Tesla"));
        Assertions.assertTrue(result.keySet().contains("Carnegie1"));
        Assertions.assertEquals("16 MAY 2023 (10:00) - 17 MAY 2023 (10:00) <br />20 MAY 2023 (10:00) - 22 MAY 2023 (10:00) <br />", result.get("Tesla"));
        Assertions.assertEquals("24 APRIL 2023 (10:00) - 26 APRIL 2023 (10:00) <br />", result.get("Tesla2"));
        Assertions.assertEquals("25 APRIL 2023 (10:00) - 29 APRIL 2023 (10:00) <br />", result.get("Carnegie1"));
    }

    //    Initialization methods
    private Equipment getFirstEquipment() {
        Equipment equipment = new Equipment();
        equipment.setEquipmentName("Computers_Multimedia_Printers");
        return equipment;
    }

    private Equipment getSecondEquipment() {
        Equipment equipment = new Equipment();
        equipment.setEquipmentName("Innovation space");
        return equipment;
    }

    private Lab getFirstLab() {
        Lab firstLab = new Lab();
        firstLab.setName("Carnegie1");
        firstLab.setEquipment(firstEquipment);
        return firstLab;
    }

    private Lab getSecondLab() {
        Lab secondLab = new Lab();
        secondLab.setName("Tesla");
        secondLab.setEquipment(secondEquipment);
        return secondLab;
    }

    private Lab getThirdLab() {
        Lab thirdLab = new Lab();
        thirdLab.setName("Tesla2");
        thirdLab.setEquipment(secondEquipment);
        return thirdLab;
    }

    private List<Project> getProjects() {
        ActivityType activityType = new ActivityType();
        activityType.setActivityName("Lecture");

        Project first = new Project();
        first
                .setName("123")
                .setSector(Sector.IT)
                .setDescription("1234567890")
                .setNeededEquipment(secondEquipment)
                .setLab(secondLab)
                .setActivityType(activityType)
                .setActive(true)
                .setStartDate(LocalDateTime.of(2023, 5, 16, 10, 0))
                .setEndDate(LocalDateTime.of(2023, 5, 17, 10, 0));

        Project second = new Project();
        second
                .setName("456")
                .setSector(Sector.IT)
                .setDescription("1234567890")
                .setNeededEquipment(secondEquipment)
                .setLab(secondLab)
                .setActivityType(activityType)
                .setStartDate(LocalDateTime.of(2023, 5, 20, 10, 0))
                .setEndDate(LocalDateTime.of(2023, 5, 22, 10, 0));
        Project old = new Project();
        old
                .setName("789")
                .setSector(Sector.IT)
                .setDescription("1234567890")
                .setNeededEquipment(secondEquipment)
                .setLab(secondLab)
                .setActivityType(activityType)
                .setStartDate(LocalDateTime.of(2021, 3, 20, 10, 0))
                .setEndDate(LocalDateTime.of(2021, 3, 22, 10, 0));

        Project third = new Project();
        third
                .setName("012")
                .setSector(Sector.IT)
                .setDescription("1234567890")
                .setNeededEquipment(secondEquipment)
                .setLab(thirdLab)
                .setActivityType(activityType)
                .setStartDate(LocalDateTime.of(2023, 4, 24, 10, 0))
                .setEndDate(LocalDateTime.of(2023, 4, 26, 10, 0));

        Project fourth = new Project();
        fourth
                .setName("111")
                .setSector(Sector.IT)
                .setDescription("1234567890")
                .setNeededEquipment(firstEquipment)
                .setLab(thirdLab)
                .setActivityType(activityType)
                .setStartDate(LocalDateTime.of(2023, 4, 25, 10, 0))
                .setEndDate(LocalDateTime.of(2023, 4, 29, 10, 0));
        return List.of(first, second, old, third, fourth);
    }

}

