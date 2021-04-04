package com.example.incubation_planner.web;

import com.example.incubation_planner.models.entity.Equipment;
import com.example.incubation_planner.models.entity.Lab;
import com.example.incubation_planner.models.entity.Project;
import com.example.incubation_planner.models.entity.UserEntity;
import com.example.incubation_planner.repositories.*;
import com.example.incubation_planner.services.ProjectService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;

import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class ProjectControllerTest {

    private static final String PROJECT_CONTROLLER_PREFIX = "/projects";
    Equipment equipment;
    Lab lab;
    String projectId;
    String userId;
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
    ProjectService projectService;
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
        userId = testData.getUserId();
        equipment = testData.getEquipment();
        lab = testData.getLab();
    }

    @AfterEach
    public void tearDown() {
        testData.cleanUp();
    }

    @Test
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void ProjectsAllShouldReturnValidStatusViewModelAndModel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PROJECT_CONTROLLER_PREFIX + "/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("projects-all1"));
    }

    @Test
    @WithMockUser(value = "pesho", roles = {"USER", "ADMIN"})
    void ProjectsOwnShouldReturnValidStatusViewModelAndModel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PROJECT_CONTROLLER_PREFIX + "/owned"))
                .andExpect(status().isOk())
                .andExpect(view().name("projects-own"))
                .andExpect(model().attributeExists("fullName"))
                .andExpect(model().attributeExists("projectsOfUser"));
    }

    @Test
    @Transactional
    @WithMockUser(value = "pesho", roles = {"USER", "ADMIN"})
    void ProjectDetailsShouldReturnValidStatusViewModelAndModel() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(PROJECT_CONTROLLER_PREFIX + "/details/{id}", projectId))
                .andExpect(status().isOk())
                .andExpect(view().name("project-details"))
                .andExpect(model().attributeExists("isCollaborating"))
                .andExpect(model().attributeExists("current"))
                .andExpect(model().attributeExists("isOwner"));
    }

    @Test
    @Transactional
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void ArchiveProjectShouldReturnValidStatusAndShowProjectAsInactive() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(PROJECT_CONTROLLER_PREFIX + "/archive/{id}", projectId))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "You archived a project"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/projects/all"));
        Assertions.assertFalse(projectRepository.getOne(projectId).isActive());
    }

    @Test
    @Transactional
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void DeleteProjectReturnsValidStatus() throws Exception {
        String url = "http://localhost:8080/projects/delete/{projectId}";
        mockMvc.perform(MockMvcRequestBuilders.get(url, projectId))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/projects/all"));

        Assertions.assertEquals(0, projectRepository.count());

    }

    @Test
    @Transactional
    @WithMockUser(value = "pesho", roles = {"USER", "ADMIN"})
    void JoinProjectAddsUserToCollaboratorsAndProjectToUserCollabs() throws Exception {
        String url = "http://localhost:8080/projects/join/{projectId}";
        String redirectUrl = String.format("/projects/details/%s", projectId);

        mockMvc.perform(MockMvcRequestBuilders.get(url, projectId))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "You are now a collaborator in the project"))
                .andExpect(MockMvcResultMatchers.redirectedUrl(redirectUrl));

        UserEntity user = userRepository.getOne(userId);
        Project project = projectRepository.getOne(projectId);
        Assertions.assertTrue(projectRepository.getOne(projectId).getCollaborators().contains(user));
        Assertions.assertTrue(userRepository.getOne(userId).getProjects().contains(project));
    }

    @Test
    @Transactional
    @WithMockUser(value = "pesho", roles = {"USER", "ADMIN"})
    void LeaveProjectRemovesUserFromCollaboratorsAndProjectFromUserCollabs() throws Exception {
        String url = "http://localhost:8080/projects/leave/{projectId}";
        String redirectUrl = String.format("/projects/details/%s", projectId);

        mockMvc.perform(MockMvcRequestBuilders.get(url, projectId))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "You are no longer a collaborator in the project"))
                .andExpect(MockMvcResultMatchers.redirectedUrl(redirectUrl));

        UserEntity user = userRepository.getOne(userId);
        Project project = projectRepository.getOne(projectId);
        Assertions.assertFalse(projectRepository.getOne(projectId).getCollaborators().contains(user));
        Assertions.assertFalse(userRepository.getOne(userId).getProjects().contains(project));
    }

    @Test
    @Transactional
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void UpdateProjectReturnsValidStatusViewModelAndModel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PROJECT_CONTROLLER_PREFIX + "/update/{id}", projectId))
                .andExpect(status().isOk())
                .andExpect(view().name("project-update"))
                .andExpect(model().attributeExists("current", "labsInfo"))
                .andExpect(model().attribute("duration", 1L));
    }

    @Test
    @Transactional
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void UpdateProjectPostUpdatesProjectSuccessfullyIfValidData() throws Exception {
        String redirectUrl = String.format("/projects/details/%s", projectId);

        mockMvc.perform(MockMvcRequestBuilders.post(PROJECT_CONTROLLER_PREFIX + "/update/{id}", projectId)
                .param("name", "12345")
                .param("sector", "IT")
                .param("description", "123456789012")
                .param("neededEquipment", "Computers_Multimedia_Printers")
                .param("activityType", "Lecture")
                .param("startDate", "2021-05-05T10:00")
                .param("endDate", "2021-05-06T10:00")
                .param("lab", "Tesla1")
                .param("promoter", "pesho")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "You updated the project"))
                .andExpect(MockMvcResultMatchers.redirectedUrl(redirectUrl));

        Project project = projectRepository.getOne(projectId);
        Assertions.assertEquals(1, projectRepository.count());
        Assertions.assertEquals("12345", project.getName());
        Assertions.assertEquals("123456789012", project.getDescription());
        Assertions.assertEquals(LocalDateTime.of(2021, 5, 5, 10, 0), project.getStartDate());
        Assertions.assertEquals(LocalDateTime.of(2021, 5, 6, 10, 0), project.getEndDate());

    }

    @Test
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void UpdateProjectRedirectsIfInvalidData() throws Exception {
        String redirectUrl = String.format("/projects/update/%s", projectId);

        mockMvc.perform(MockMvcRequestBuilders.post(PROJECT_CONTROLLER_PREFIX + "/update/{id}", projectId)
                .param("name", "1")
                .param("sector", "IT")
                .param("description", "123456789")
                .param("neededEquipment", "Computers_Multimedia_Printers")
                .param("activityType", "Lecture")
                .param("startDate", "2021-05-05T10:00")
                .param("endDate", "2021-05-03T10:00")
                .param("lab", "Tesla1")
                .param("promoter", "pesho")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("projectAddBindingModel"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("org.springframework.validation.BindingResult.projectAddBindingModel"))
                .andExpect(MockMvcResultMatchers.redirectedUrl(redirectUrl));
    }

    @Test
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void PublishResultsShouldReturnValidStatusViewModelAndModel() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(PROJECT_CONTROLLER_PREFIX + "/publish/{id}", projectId))
                .andExpect(status().isOk())
                .andExpect(view().name("project-publish"))
                .andExpect(model().attribute("id", projectId))
                .andExpect(model().attributeExists("current"));
    }

    @Test
    @Transactional
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void PublishResultsPerformsSuccessfullyWithValidInput() throws Exception {
        String redirectUrl = String.format("/projects/details/%s", projectId);

        mockMvc.perform(MockMvcRequestBuilders.post(PROJECT_CONTROLLER_PREFIX + "/publish/{id}", projectId)
                .param("description", "aD011960678529315557020119606785293155570201196067852931555702")
                .param("sector", "IT")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "You published the results from the project"))
                .andExpect(MockMvcResultMatchers.redirectedUrl(redirectUrl));
        Assertions.assertEquals("aD011960678529315557020119606785293155570201196067852931555702", projectRepository.getOne(projectId).getResult());
    }

    @Test
    @Transactional
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void PublishResultsRedfirectsWithInvalidInput() throws Exception {
        String redirectUrl = String.format("/projects/publish/%s", projectId);

        mockMvc.perform(MockMvcRequestBuilders.post(PROJECT_CONTROLLER_PREFIX + "/publish/{id}", projectId)
                .param("description", "aD0119606785293155570201196067852931555702011")
                .param("sector", "IT")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("projectResultBindingModel"))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("org.springframework.validation.BindingResult.projectResultBindingModel"))
                .andExpect(MockMvcResultMatchers.redirectedUrl(redirectUrl));
        Assertions.assertNull(projectRepository.getOne(projectId).getResult());
    }


}
