package com.example.incubation_planner.web;

import com.example.incubation_planner.models.view.ProjectResultViewModel;
import com.example.incubation_planner.services.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/results/")
public class ResultsController {

    private final ModelMapper modelMapper;
    private final ProjectService projectService;

    public ResultsController(ModelMapper modelMapper, ProjectService projectService) {
        this.modelMapper = modelMapper;
        this.projectService = projectService;
    }

    @GetMapping("/it")
    public String resultsIT(Model model) {
        List<ProjectResultViewModel> projectResultViewModels = projectService.getResults("IT");
        model.addAttribute("results", projectResultViewModels);
        return "results-it";
    }

    @GetMapping("/arts")
    public String resultsArts(Model model) {
        List<ProjectResultViewModel> projectResultViewModels = projectService.getResults("Arts");
        model.addAttribute("results", projectResultViewModels);
        return "results-art";
    }
    @GetMapping("/production")
    public String resultsProduction(Model model) {
        List<ProjectResultViewModel> projectResultViewModels = projectService.getResults("Production");
        model.addAttribute("results", projectResultViewModels);
        return "results-production";
    }
    @GetMapping("/education")
    public String resultsEducation(Model model) {
        List<ProjectResultViewModel> projectResultViewModels = projectService.getResults("Education");
        model.addAttribute("results", projectResultViewModels);
        return "results-edu";
    }
}
