package com.example.incubation_planner.web;

import com.example.incubation_planner.models.entity.*;
import com.example.incubation_planner.models.entity.enums.EquipmentName;
import com.example.incubation_planner.models.entity.enums.LabName;
import com.example.incubation_planner.models.entity.enums.Sector;
import com.example.incubation_planner.models.entity.enums.UserType;
import com.example.incubation_planner.repositories.*;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

public class IdeaTestData {

    private String testIdeaId;
    private Equipment equipment;

    private IdeaRepository ideaRepository;
    private ActivityTypeRepository activityTypeRepository;
    private EquipmentRepository equipmentRepository;
    private LabRepository labRepository;
    private UserRepository userRepository;
    private LogRepository logRepository;
    private ProjectRepository projectRepository;

    public IdeaTestData(IdeaRepository ideaRepository, ActivityTypeRepository activityTypeRepository, EquipmentRepository equipmentRepository, LabRepository labRepository, UserRepository userRepository, LogRepository logRepository, ProjectRepository projectRepository) {
        this.ideaRepository = ideaRepository;
        this.activityTypeRepository = activityTypeRepository;
        this.equipmentRepository = equipmentRepository;
        this.labRepository = labRepository;
        this.userRepository = userRepository;
        this.logRepository = logRepository;
        this.projectRepository = projectRepository;
    }

    @Transactional
    public void init() {
        ActivityType activityType = new ActivityType();
        activityType.setActivityName("Lecture");
        activityTypeRepository.save(activityType);
        equipment = new Equipment();
        equipment.setEquipmentName("Computers_Multimedia_Printers");
        equipmentRepository.save(equipment);


        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("pesho")
                .setPassword("123456789")
                .setLastName("Peshov")
                .setEmail("pesho@pesho.bg")
                .setSector(Sector.Arts)
                .setUserType(UserType.Company);
        userRepository.save(userEntity);

        Idea idea = new Idea();
        idea.setName("Idea test")
                .setPromoter(userEntity)
                .setActivityType(activityType)
                .setDescription("description of the test idea")
                .setNeededEquipment(equipment)
                .setDuration(2)
                .setSector(Sector.Arts)
                .setStatus("pending");
        ideaRepository.save(idea);
        testIdeaId = idea.getId();

    }

    void cleanUp() {
        logRepository.deleteAll();
        ideaRepository.deleteAll();
        projectRepository.deleteAll();
        activityTypeRepository.deleteAll();
        userRepository.deleteAll();
        labRepository.deleteAll();
        equipmentRepository.deleteAll();
    }

    public String getTestIdeaId() {
        return testIdeaId;
    }

    public Equipment getEquipment() {
        return equipment;
    }
}

