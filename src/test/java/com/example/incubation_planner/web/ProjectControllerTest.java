package com.example.incubation_planner.web;

import com.example.incubation_planner.models.entity.*;
import com.example.incubation_planner.models.entity.enums.Sector;
import com.example.incubation_planner.models.entity.enums.UserType;
import com.example.incubation_planner.repositories.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class ProjectControllerTest {

    private static final String PROJECT_CONTROLLER_PREFIX = "/projects";
    Equipment equipment;
    Lab lab;
    String projectId;
    ProjectTestData testData;

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ActivityTypeRepository activityTypeRepository;
    @Autowired
    EquipmentRepository equipmentRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    LabRepository labRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LogRepository logRepository;

    @BeforeEach
    public void setup() {
        testData = new ProjectTestData(
                activityTypeRepository,
                equipmentRepository,
                projectRepository,
                labRepository,
                userRepository,
                logRepository

        );
        testData.init();
        projectId = testData.getProjectId();
        equipment = testData.getEquipment();
        lab = testData.getLab();

    }

    @AfterEach
    public void tearDown() {
        testData.cleanUp();
    }

    @Test
    void ProjectsAllShouldReturnValidStatusViewModelAndModel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PROJECT_CONTROLLER_PREFIX + "/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("projects-all1"));
    }

    @Test
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void ProjectsOwnShouldReturnValidStatusViewModelAndModel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PROJECT_CONTROLLER_PREFIX + "/owned"))
                .andExpect(status().isOk())
                .andExpect(view().name("projects-own"))
                .andExpect(model().attributeExists("fullName"))
                .andExpect(model().attributeExists("projectsOfUser"));
    }

    @Test
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void IdeaDetailsShouldReturnValidStatusViewModelAndModel() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(PROJECT_CONTROLLER_PREFIX + "/details/{id}", projectId))
                .andExpect(status().isOk())
                .andExpect(view().name("project-details"))
                .andExpect(model().attributeExists("isCollaborating"))
                .andExpect(model().attributeExists("current"))
                .andExpect(model().attributeExists("isOwner"));

    }
}
