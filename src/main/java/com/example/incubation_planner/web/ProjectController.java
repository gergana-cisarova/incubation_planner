package com.example.incubation_planner.web;

import com.example.incubation_planner.models.binding.ProjectAddBindingModel;
import com.example.incubation_planner.models.binding.ProjectResultBindingModel;
import com.example.incubation_planner.models.service.ProjectResultServiceModel;
import com.example.incubation_planner.models.service.ProjectServiceModel;
import com.example.incubation_planner.models.view.ProjectDetailedViewModel;
import com.example.incubation_planner.services.*;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    private final ModelMapper modelMapper;
    private final ActivityTypeService activityTypeService;
    private final EquipmentService equipmentService;
    private final ProjectService projectService;
    private final LabService labService;
    private final UserService userService;
    private final LogService logService;

    public ProjectController(ModelMapper modelMapper, ActivityTypeService activityTypeService, EquipmentService equipmentService, ProjectService projectService, LabService labService, UserService userService, LogService logService) {
        this.modelMapper = modelMapper;
        this.activityTypeService = activityTypeService;
        this.equipmentService = equipmentService;
        this.projectService = projectService;
        this.labService = labService;
        this.userService = userService;
        this.logService = logService;
    }

    @ModelAttribute("projectAddBindingModel")
    public ProjectAddBindingModel projectAddBindingModel() {
        return new ProjectAddBindingModel();
    }

    @ModelAttribute("projectResultBindingModel")
    public ProjectResultBindingModel projectResultBindingModel() {
        return new ProjectResultBindingModel();
    }

    @ModelAttribute("activityTypes")
    public List<String> activityTypes() {
        return activityTypeService.getAllActivities();
    }

    @ModelAttribute("equipmentTypes")
    public List<String> equipmentTypes() {
        return equipmentService.getAllEquipments();
    }

    @ModelAttribute("message")
    public String message() {
        return "";
    }

    @GetMapping("/all")
    public String showAll(Model model) {
        return "projects-all1";
    }

    @GetMapping("/owned")
    public String showOwnProjects(Model model,
                                  @AuthenticationPrincipal UserDetails principal) {
        if (principal != null) {
            String firstName = (userService.findByUsername(principal.getUsername())).getFirstName();
            String lastName = (userService.findByUsername(principal.getUsername())).getLastName();
            String userName = (userService.findByUsername(principal.getUsername())).getUsername();
            String fullName = String.format("%s %s", firstName, lastName);
            model.addAttribute("fullName", fullName);
            model.addAttribute("projectsOfUser", projectService.getUserProjectsOrderedByStartDate(userName));
        }
        return "projects-own";
    }

    @GetMapping("/details/{id}")
    public String showDetails(@PathVariable String id,
                              Model model,
                              @AuthenticationPrincipal UserDetails principal) {
        if (principal != null) {
            String userName = principal.getUsername();
            String owner = projectService.findProjectOwnerStr(id);
            boolean isOwner = userName.equals(owner);
            model.addAttribute("isCollaborating", projectService.checkIfCollaborating(id, userName));
            ProjectDetailedViewModel projectViewModel = projectService.extractProjectModel(id);
            model.addAttribute("current", projectViewModel);
            model.addAttribute("isOwner", isOwner);
        }
        return "project-details";
    }

    @GetMapping("/archive/{id}")
    public String archiveProject(@PathVariable String id,
                                 RedirectAttributes redirectAttributes) {
        projectService.archiveProject(id);
        redirectAttributes.addFlashAttribute("message", "You archived a project");

        return "redirect:/projects/all";
    }

    @GetMapping("/delete/{id}")
    public String deleteProject(@PathVariable String id,
                                RedirectAttributes redirectAttributes) {
        projectService.deleteProject(id);
        redirectAttributes.addFlashAttribute("message", "You deleted a project");

        return "redirect:/projects/all";
    }

    @GetMapping("/join/{id}")
    public String joinProject(@PathVariable String id,
                              Model model,
                              RedirectAttributes redirectAttributes,
                              @AuthenticationPrincipal UserDetails principal) {
        if (principal != null) {
            String userName = principal.getUsername();
            projectService.joinProject(id, userName);

            redirectAttributes.addFlashAttribute("message", "You are now a collaborator in the project");
        }
        return "redirect:/projects/details/{id}";
    }

    @GetMapping("/leave/{id}")
    public String leaveProject(@PathVariable String id,
                               Model model,
                               RedirectAttributes redirectAttributes,
                               @AuthenticationPrincipal UserDetails principal) {
        if (principal != null) {
            String userName = principal.getUsername();
            projectService.leaveProject(id, userName);
            model.addAttribute("id", id);
            redirectAttributes.addFlashAttribute("message", "You are no longer a collaborator in the project");
        }
        return "redirect:/projects/details/{id}";

    }

    @GetMapping("/update/{id}")
    public String updateProject(@PathVariable String id,
                                Model model) {
        ProjectServiceModel currentData = projectService.extractProjectServiceModel(id);
        long durationInDays = projectService.getDurationInDays(currentData);
        model.addAttribute("current", currentData);
//        model.addAttribute("labs", labService.getAllLabs());
        model.addAttribute("duration", durationInDays);
        model.addAttribute("labsInfo", labService.getAllLabsWithProjects());

        return "project-update";
    }


    @PostMapping("/update/{id}")
    public String updateProjectPost(@PathVariable String id,
                                    @Valid ProjectAddBindingModel projectAddBindingModel,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {
        model.addAttribute("id", id);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("projectAddBindingModel", projectAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.projectAddBindingModel", bindingResult);
            return "redirect:/projects/update/{id}";
        }

        ProjectServiceModel projectServiceModel = modelMapper.map(
                projectAddBindingModel, ProjectServiceModel.class);
        projectServiceModel.setPromoter(projectService.getProjectPromoter(id));
        projectService.updateProject(id, projectServiceModel);

        redirectAttributes.addFlashAttribute("message", "You updated the project");
        return "redirect:/projects/details/{id}";
    }

    @GetMapping("/publish/{id}")
    public String publishProject(@PathVariable String id,
                                 Model model) {
        ProjectResultServiceModel projectResultServiceModel = projectService.extractProjectResultServiceModel(id);
        model.addAttribute("current", projectResultServiceModel);
        model.addAttribute("id", id);
        return "project-publish";
    }


    @PostMapping("/publish/{id}")
    public String publishProjectPost(@PathVariable String id,
                                     @Valid ProjectResultBindingModel projectResultBindingModel,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("projectResultBindingModel", projectResultBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.projectResultBindingModel", bindingResult);
            return "redirect:/projects/publish/{id}";
        }

        ProjectResultServiceModel projectResultServiceModel = modelMapper.map(
                projectResultBindingModel, ProjectResultServiceModel.class);
        projectResultServiceModel.setId(id);
        projectService.publishProjectResult(projectResultServiceModel);

        redirectAttributes.addFlashAttribute("message", "You published the results from the project");
        return "redirect:/projects/details/{id}";
    }



}
