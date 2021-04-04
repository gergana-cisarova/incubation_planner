package com.example.incubation_planner.web;

import com.example.incubation_planner.models.entity.Equipment;
import com.example.incubation_planner.models.entity.Lab;
import com.example.incubation_planner.repositories.*;
import com.example.incubation_planner.services.CarouselService;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class IdeaControllerTest {

    private static final String IDEA_CONTROLLER_PREFIX = "/ideas";
    private String testIdeaId;
    private Equipment equipment;
    private IdeaTestData ideaTestData;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private IdeaRepository ideaRepository;
    @Autowired
    private ActivityTypeRepository activityTypeRepository;
    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private LabRepository labRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private CarouselService carouselService;


    @BeforeEach
    public void setup() {
        ideaTestData = new IdeaTestData(
                ideaRepository,
                activityTypeRepository,
                equipmentRepository,
                labRepository,
                userRepository,
                logRepository,
                projectRepository
        );
        ideaTestData.init();
        testIdeaId = ideaTestData.getTestIdeaId();
        equipment = ideaTestData.getEquipment();
    }

    @AfterEach
    public void tearDown() {
        ideaTestData.cleanUp();
    }

    @Test
    void IdeasAllShouldReturnValidStatusViewModelAndModel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(IDEA_CONTROLLER_PREFIX + "/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("ideas-all"))
                .andExpect(model().attributeExists("ideas"));
    }


    @Test
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void IdeaDetailsShouldReturnValidStatusViewModelAndModel() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(IDEA_CONTROLLER_PREFIX + "/details/{id}", testIdeaId))
                .andExpect(status().isOk())
                .andExpect(view().name("idea-details"))
                .andExpect(model().attributeExists("current"));

    }

    @Test
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void AddShouldReturnValidStatusViewModelAndModel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(IDEA_CONTROLLER_PREFIX + "/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("idea-add"))
                .andExpect(model().attributeExists("firstImg"))
                .andExpect(model().attributeExists("secondImg"))
                .andExpect(model().attributeExists("thirdImg"));
    }

    @Test
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void AcceptIdeaPostValidInput() throws Exception {
        Lab lab = new Lab();
        lab.setEquipment(equipment).setName("Monnet2");
        labRepository.saveAndFlush(lab);

        mockMvc.perform(MockMvcRequestBuilders.post(IDEA_CONTROLLER_PREFIX + "/accept/{id}", testIdeaId)
                .param("name", "123")
                .param("sector", "IT")
                .param("description", "1234567890")
                .param("neededEquipment", "Computers_Multimedia_Printers")
                .param("activityType", "Lecture")
                .param("startDate", "2021-05-03T10:00")
                .param("endDate", "2021-05-04T10:00")
                .param("lab", "Monnet2")
                .param("promoter", "admin")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "You created a project"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/ideas/all"));

        Assertions.assertEquals(1, projectRepository.count());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void addIdeaInvalidInput() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(IDEA_CONTROLLER_PREFIX + "/add")
                .param("name", "12")
                .param("sector", "")
                .param("description", "123456789")
                .param("duration", "0")
                .param("neededEquipment", "")
                .param("activityType", "")
                .param("promoter", "admin")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("org.springframework.validation.BindingResult.ideaAddBindingModel"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/ideas/add"));
        Assertions.assertEquals(0, logRepository.count());
    }


    @Test
    @Transactional
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void AcceptIdeaShouldReturnValidStatusViewModelAndModel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(IDEA_CONTROLLER_PREFIX + "/accept/{id}", testIdeaId))
                .andExpect(status().isOk())
                .andExpect(view().name("project-add"))
                .andExpect(model().attributeExists("ideaServiceModel", "labs", "duration", "labsInfo"));
    }


    @Test
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void IdeaAcceptPostInvalidInput() throws Exception {
        Lab lab = new Lab();
        lab.setName("Ideation1");
        lab.setEquipment(equipment);
        labRepository.save(lab);

        String redirectUrl = String.format("/ideas/accept/%s", testIdeaId);

        mockMvc.perform(MockMvcRequestBuilders.post(IDEA_CONTROLLER_PREFIX + "/accept/{id}", testIdeaId)
                .param("name", "12")
                .param("sector", "")
                .param("description", "123456789")
                .param("neededEquipment", "")
                .param("activityType", "")
                .param("startDate", "")
                .param("endDate", "")
                .param("lab", "")
                .param("promoter", "admin")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("org.springframework.validation.BindingResult.projectAddBindingModel"))
                .andExpect(MockMvcResultMatchers.redirectedUrl(redirectUrl));

        Assertions.assertEquals(0, projectRepository.count());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void IdeaAcceptPostInvalidDatesOnly() throws Exception {
        Lab lab = new Lab();
        lab.setName("Ideation1");
        lab.setEquipment(equipment);
        labRepository.save(lab);

        String redirectUrl = String.format("/ideas/accept/%s", testIdeaId);

        mockMvc.perform(MockMvcRequestBuilders.post(IDEA_CONTROLLER_PREFIX + "/accept/{id}", testIdeaId)
                .param("name", "123")
                .param("sector", "IT")
                .param("description", "1234567890")
                .param("neededEquipment", "Computers_Multimedia_Printers")
                .param("activityType", "Masterclass")
                .param("startDate", "2021-04-04T10:00")
                .param("endDate", "2021-04-04T09:59")
                .param("lab", "Ideation")
                .param("promoter", "admin")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("org.springframework.validation.BindingResult.projectAddBindingModel"))
                .andExpect(MockMvcResultMatchers.redirectedUrl(redirectUrl));

        Assertions.assertEquals(0, projectRepository.count());
    }


    @Test
    @WithMockUser(value = "pesho", roles = {"USER", "ADMIN"})

    void addIdeaValidInput() throws Exception {
        Lab lab = new Lab();
        lab.setName("Ideation1");
        lab.setEquipment(equipment);
        labRepository.save(lab);

        mockMvc.perform(MockMvcRequestBuilders.post(IDEA_CONTROLLER_PREFIX + "/add")
                .param("name", "123")
                .param("sector", "Arts")
                .param("description", "1234567890")
                .param("duration", "1")
                .param("neededEquipment", "Computers_Multimedia_Printers")
                .param("activityType", "Lecture")
                .param("promoter", "pesho")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "Your idea was added"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/ideas/all"));

        Assertions.assertEquals(2, ideaRepository.count());
        Assertions.assertEquals(1, logRepository.count());

    }

    @Test
    @Transactional
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void DeleteIdeaReturnsValidStatus() throws Exception {
        String url = "http://localhost:8080/ideas/delete/{testIdeaId}";
        mockMvc.perform(MockMvcRequestBuilders.get(url, testIdeaId))
                .andExpect(status().isFound());

        Assertions.assertEquals(0, ideaRepository.count());
    }

}
