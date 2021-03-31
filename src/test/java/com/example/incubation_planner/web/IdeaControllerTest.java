package com.example.incubation_planner.web;

import com.example.incubation_planner.models.entity.*;
import com.example.incubation_planner.models.entity.enums.Sector;
import com.example.incubation_planner.models.entity.enums.UserType;
import com.example.incubation_planner.repositories.*;
import com.example.incubation_planner.services.CarouselService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class IdeaControllerTest {

    private static final String IDEA_CONTROLLER_PREFIX = "/ideas";
    private String testIdeaId;

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
    private CarouselService carouselService;


    @BeforeEach
    public void setup() {
        this.init();
    }


    @Test
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void shouldReturnValidStatusViewModelAndModel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(IDEA_CONTROLLER_PREFIX + "/details/{id}", testIdeaId))
                .andExpect(status().isOk())
                .andExpect(view().name("idea-details"))
                .andExpect(model().attributeExists("current"));

    }

    @Test
    @WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
    void addIdea() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(IDEA_CONTROLLER_PREFIX + "/add")
                .param("name", "Idea test1")
                .param("sector", "Arts")
                .param("description", "description of the test idea")
                .param("duration", "2")
                .param("neededEquipment", "Computers")
                .param("activityType", "Lecture")
                .param("promoter", "admin")
                .with(csrf()))
                .andExpect(status().is3xxRedirection());

        Assertions.assertEquals(2, ideaRepository.count());

    }


    private void init() {
        ActivityType activityType = new ActivityType();
        activityType.setActivityName("Lecture");
        activityTypeRepository.save(activityType);
        Equipment equipment = new Equipment();
        equipment.setEquipmentName("Computers");
        equipmentRepository.save(equipment);
        Lab lab = new Lab();
        lab.setName("Monnet2");
        lab.setEquipment(equipment);
        List<Project> emptyList = new ArrayList<>();
        lab.setProjects(emptyList);
        labRepository.save(lab);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("admin1")
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
}
