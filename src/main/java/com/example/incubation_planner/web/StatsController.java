package com.example.incubation_planner.web;

import com.example.incubation_planner.services.LogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/statistics")
public class StatsController {
    private final LogService logService;

    public StatsController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping
    public String stats(Model model) {
        model.addAttribute("jointProjectLogs", logService.findAllJoinProjectLogs());
        model.addAttribute("addIdeaLogs", logService.findAllIdeaAddLogs());
        model.addAttribute("activityIdeas", logService.getStatsIdeasCreated());
        model.addAttribute("activityProjects", logService.getStatsJoinProjectActivity());

        return "stats";
    }
}


