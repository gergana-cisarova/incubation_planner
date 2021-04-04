package com.example.incubation_planner.web;

import com.example.incubation_planner.models.binding.IdeaAddBindingModel;
import com.example.incubation_planner.models.binding.ProjectAddBindingModel;
import com.example.incubation_planner.models.service.IdeaServiceModel;
import com.example.incubation_planner.models.service.ProjectServiceModel;
import com.example.incubation_planner.models.view.IdeaViewModel;
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
import java.util.List;

@Controller
@RequestMapping("/ideas")
public class IdeaController {
    private final CarouselService carouselService;
    private final ModelMapper modelMapper;
    private final IdeaService ideaService;
    private final ActivityTypeService activityTypeService;
    private final EquipmentService equipmentService;
    private final ProjectService projectService;
    private final LabService labService;
    private final LogService logService;

    public IdeaController(CarouselService carouselService, ModelMapper modelMapper, IdeaService ideaService, ActivityTypeService activityTypeService, EquipmentService equipmentService, ProjectService projectService, LabService labService, LogService logService) {
        this.carouselService = carouselService;
        this.modelMapper = modelMapper;
        this.ideaService = ideaService;
        this.activityTypeService = activityTypeService;
        this.equipmentService = equipmentService;
        this.projectService = projectService;
        this.labService = labService;
        this.logService = logService;
    }

    @ModelAttribute("ideaAddBindingModel")
    public IdeaAddBindingModel createBindingModel() {
        return new IdeaAddBindingModel();
    }

    @ModelAttribute("activityTypes")
    public List<String> activityTypes() {
        return activityTypeService.getAllActivities();
    }

    @ModelAttribute("equipmentTypes")
    public List<String> equipmentTypes() {
        return equipmentService.getAllEquipments();
    }

    @ModelAttribute("projectAddBindingModel")
    public ProjectAddBindingModel projectAddBindingModel() {
        return new ProjectAddBindingModel();
    }

    @ModelAttribute("message")
    public String message() {
        return "";
    }


    @GetMapping("/all")
    public String showAll(Model model) {
        model.addAttribute("ideas", ideaService.getAll());

        return "ideas-all";
    }

    @GetMapping("/add")
    public String addIdea(Model model) {

        model.addAttribute("firstImg", carouselService.firstImage());
        model.addAttribute("secondImg", carouselService.secondImage());
        model.addAttribute("thirdImg", carouselService.thirdImage());

        return "idea-add";
    }

    @PostMapping("/add")
    public String postIdea(@Valid IdeaAddBindingModel ideaAddBindingModel,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal UserDetails principal) {
        if (principal != null) {

            if (bindingResult.hasErrors()) {
                redirectAttributes.addFlashAttribute("ideaAddBindingModel", ideaAddBindingModel);
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.ideaAddBindingModel", bindingResult);
                return "redirect:/ideas/add";
            }

            IdeaServiceModel ideaServiceModel = modelMapper.map(
                    ideaAddBindingModel,
                    IdeaServiceModel.class);

            ideaServiceModel.setPromoter(principal.getUsername());

            ideaService.createIdea(ideaServiceModel);
            redirectAttributes.addFlashAttribute("message", "Your idea was added");
        }
        return "redirect:/ideas/all";
    }

    @GetMapping("/details/{id}")
    public String showDetails(@PathVariable String id,
                              Model model) {

        IdeaViewModel ideaViewModel = ideaService.getIdeaView(id);
        model.addAttribute("current", ideaViewModel);

        return "idea-details";
    }

    @GetMapping("/accept/{id}")
    public String acceptIdea(@PathVariable String id, Model model) {
        IdeaServiceModel ideaServiceModel = ideaService.extractIdeaModel(id);
        model.addAttribute("ideaServiceModel", ideaServiceModel);
        model.addAttribute("labs", labService.findSuitableLabs(ideaServiceModel.getNeededEquipment()));
        model.addAttribute("duration", ideaServiceModel.getDuration());
        model.addAttribute("labsInfo", labService.getSuitableLabsWithProjects(ideaServiceModel.getNeededEquipment()));

        return "project-add";
    }

    @PostMapping("/accept/{id}")
    public String turnIdeaIntoProject(@PathVariable String id,
                                      @Valid ProjectAddBindingModel projectAddBindingModel,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {
        model.addAttribute("duration", ideaService.getDurationOfIdea(id));
        model.addAttribute("id", id);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("projectAddBindingModel", projectAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.projectAddBindingModel", bindingResult);
            return "redirect:/ideas/accept/{id}";
        }

        ProjectServiceModel projectServiceModel = modelMapper.map(
                projectAddBindingModel, ProjectServiceModel.class);
        projectServiceModel.setPromoter(ideaService.extractIdeaModel(id).getPromoter());
        projectService.createProject(projectServiceModel);

        ideaService.markIdeaAsAccepted(id);
        redirectAttributes.addFlashAttribute("message", "You created a project");

        return "redirect:/ideas/all";
    }

    @GetMapping("/delete/{id}")
    public String deleteIdea(@PathVariable String id,
                             RedirectAttributes redirectAttributes
    ) {
        ideaService.deleteIdea(id);
        redirectAttributes.addFlashAttribute("message", "You deleted an idea");

        return "redirect:/ideas/all";
    }
}
