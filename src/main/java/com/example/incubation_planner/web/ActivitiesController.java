package com.example.incubation_planner.web;

import com.example.incubation_planner.models.binding.ActivityTypeBindingModel;
import com.example.incubation_planner.models.binding.UserRegistrationBindingModel;
import com.example.incubation_planner.models.service.ActivityTypeServiceModel;
import com.example.incubation_planner.services.ActivityTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/activities")
public class ActivitiesController {
    private final ModelMapper modelMapper;
    private final ActivityTypeService activityTypeService;

    public ActivitiesController(ModelMapper modelMapper, ActivityTypeService activityTypeService) {
        this.modelMapper = modelMapper;
        this.activityTypeService = activityTypeService;
    }

    @ModelAttribute("activityTypeBindingModel")
    public ActivityTypeBindingModel activityTypeBindingModel() {
        return new ActivityTypeBindingModel();
    }

    @GetMapping("/add")
    public String add() {
        return "activity-add";
    }

    @PostMapping("/add")
    public String saveActivity(
            @Valid ActivityTypeBindingModel activityTypeBindingModel,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("activityTypeBindingModel", activityTypeBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.activityTypeBindingModel", bindingResult);
            return "redirect:add";
        }

        ActivityTypeServiceModel activityTypeServiceModel = modelMapper.map(activityTypeBindingModel, ActivityTypeServiceModel.class);
        activityTypeService.addNewActivity(activityTypeServiceModel);
        return "redirect:/home";
    }
}
