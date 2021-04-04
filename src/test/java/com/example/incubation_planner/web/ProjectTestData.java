package com.example.incubation_planner.web;

import com.example.incubation_planner.models.entity.*;
import com.example.incubation_planner.models.entity.enums.Sector;
import com.example.incubation_planner.models.entity.enums.UserType;
import com.example.incubation_planner.repositories.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

public class ProjectTestData {

    private String projectId;
    private Equipment equipment;
    private ActivityType activityType;
    private Lab lab;
    private String userId;

    private ActivityTypeRepository activityTypeRepository;
    private EquipmentRepository equipmentRepository;
    private ProjectRepository projectRepository;
    private LabRepository labRepository;
    private UserRepository userRepository;
    private LogRepository logRepository;

    public ProjectTestData(ActivityTypeRepository activityTypeRepository, EquipmentRepository equipmentRepository, ProjectRepository projectRepository, LabRepository labRepository, UserRepository userRepository, LogRepository logRepository) {
        this.activityTypeRepository = activityTypeRepository;
        this.equipmentRepository = equipmentRepository;
        this.projectRepository = projectRepository;
        this.labRepository = labRepository;
        this.userRepository = userRepository;
        this.logRepository = logRepository;
    }

    @Transactional
    public void init() {
        activityType = new ActivityType();
        activityType.setActivityName("Lecture");
        activityTypeRepository.save(activityType);
        equipment = new Equipment();
        equipment.setEquipmentName("Computers_Multimedia_Printers");
        equipmentRepository.saveAndFlush(equipment);
        Lab lab = new Lab();
        lab
                .setName("Tesla1")
                .setEquipment(equipment);
        labRepository.saveAndFlush(lab);

        UserEntity user = new UserEntity();
        user.setUsername("pesho")
                .setPassword("123456789")
                .setLastName("Peshov")
                .setEmail("pesho@pesho.bg")
                .setSector(Sector.Arts)
                .setUserType(UserType.Company);
        userRepository.save(user);
        userId = user.getId();

        Project project = new Project();
        project
                .setName("123")
                .setPromoter(user)
                .setActivityType(activityType)
                .setDescription("1234567890")
                .setNeededEquipment(equipment)
                .setSector(Sector.Arts)
                .setLab(lab)
                .setStartDate(LocalDateTime.of(2021, 4, 16, 10, 0))
                .setEndDate(LocalDateTime.of(2021, 4, 17, 10, 0));

        projectRepository.save(project);
        projectId = project.getId();

    }

    void cleanUp() {
        logRepository.deleteAll();
        projectRepository.deleteAll();
        activityTypeRepository.deleteAll();
        userRepository.deleteAll();
        labRepository.deleteAll();
        equipmentRepository.deleteAll();
    }

    public String getProjectId() {
        return projectId;
    }

    public String getUserId() {
        return userId;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public Lab getLab() {
        return lab;
    }

}

