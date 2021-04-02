package com.example.incubation_planner.services.impl;

import com.example.incubation_planner.models.entity.Idea;
import com.example.incubation_planner.models.entity.LogEntity;
import com.example.incubation_planner.models.entity.Project;
import com.example.incubation_planner.models.entity.UserEntity;
import com.example.incubation_planner.models.service.IdeaLogServiceModel;
import com.example.incubation_planner.models.service.IdeaServiceModel;
import com.example.incubation_planner.models.service.ProjectServiceModel;
import com.example.incubation_planner.models.view.AddIdeaLogViewModel;
import com.example.incubation_planner.models.view.JoinProjectLogViewModel;
import com.example.incubation_planner.repositories.LogRepository;
import com.example.incubation_planner.services.IdeaService;
import com.example.incubation_planner.services.LogService;
import com.example.incubation_planner.services.ProjectService;
import com.example.incubation_planner.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LogServiceImpl implements LogService {
    private final LogRepository logRepository;
    private final UserService userService;
    private final ProjectService projectService;
    private final IdeaService ideaService;
    private final ModelMapper modelMapper;
    private Logger LOGGER = LoggerFactory.getLogger(LogServiceImpl.class);


    public LogServiceImpl(LogRepository logRepository, UserService userService, ProjectService projectService, IdeaService ideaService, ModelMapper modelMapper) {
        this.logRepository = logRepository;
        this.userService = userService;
        this.projectService = projectService;
        this.ideaService = ideaService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void createProjectJoinLog(String action, String projectId) {

        ProjectServiceModel project = projectService.findProjectById(projectId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity userEntity = userService.findByUsername(username);

        LogEntity logEntity = new LogEntity()
                .setProject(modelMapper.map(project, Project.class))
                .setAction(action)
                .setTime(LocalDateTime.now())
                .setUser(userEntity);
        logRepository.save(logEntity);
    }


    @Override
    public void createIdeaAddLog(String action, String ideaName) {
        IdeaLogServiceModel idea = ideaService.generateIdeaServiceModel(ideaName);

if (idea!=null) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    UserEntity userEntity = userService.findByUsername(username);
    LogEntity logEntity = new LogEntity()
            .setIdea(modelMapper.map(idea, Idea.class))
            .setAction(action)
            .setTime(LocalDateTime.now())
            .setUser(userEntity);
    logRepository.save(logEntity);
}

    }

    @Override
    public List<AddIdeaLogViewModel> findAllIdeaAddLogs() {
        return logRepository
                .findAllByIdeaNotNullOrderByTimeDesc()
                .stream()
                .map(l -> {
                    AddIdeaLogViewModel addIdeaLogViewModel = modelMapper.map(l, AddIdeaLogViewModel.class);
                    addIdeaLogViewModel
                            .setIdea(l.getIdea().getName())
                            .setUser(l.getUser().getUsername())
                            .setDateTime(String.format("%02d %s %s (%02d:%02d)",
                                    l.getTime().getDayOfMonth(), l.getTime().getMonth(),
                                    l.getTime().getYear(), l.getTime().getHour(), l.getTime().getMinute()));
                    return addIdeaLogViewModel;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<JoinProjectLogViewModel> findAllJoinProjectLogs() {
        return logRepository
                .findAllByProjectNotNullOrderByTimeDesc()
                .stream()
                .map(l -> {
                    JoinProjectLogViewModel joinProjectLogViewModel = modelMapper.map(l, JoinProjectLogViewModel.class);
                    joinProjectLogViewModel
                            .setProject(l.getProject().getName())
                            .setUser(l.getUser().getUsername())
                            .setDateTime(String.format("%02d %s %s (%02d:%02d)",
                                    l.getTime().getDayOfMonth(), l.getTime().getMonth(),
                                    l.getTime().getYear(), l.getTime().getHour(), l.getTime().getMinute()));
                    return joinProjectLogViewModel;
                })
                .collect(Collectors.toList());
    }

    @Scheduled(cron = "0 0/10 20 * * FRI")
    public void deleteLogs() {
        LOGGER.info("Deleting logs...");
        logRepository.deleteAll();
    }

    public Map<Integer, Integer> getStatsJoinProjectActivity() {
        Map<Integer, Integer> activityMap = new HashMap<>();
        logRepository.findAllByProjectNotNullOrderByTimeDesc()
                .forEach(l -> {
                    int dayOfWeek = l.getTime().getDayOfWeek().getValue();
                    activityMap.putIfAbsent(dayOfWeek, 0);
                    activityMap.put(dayOfWeek, activityMap.get(dayOfWeek) + 1);
                });
        return activityMap;
    }


    public Map<Integer, Integer> getStatsIdeasCreated() {
        Map<Integer, Integer> activityMap = new HashMap<>();
        logRepository.findAllByIdeaNotNullOrderByTimeDesc()
                .forEach(l -> {
                    int dayOfWeek = l.getTime().getDayOfWeek().getValue();
                    activityMap.putIfAbsent(dayOfWeek, 0);
                    activityMap.put(dayOfWeek, activityMap.get(dayOfWeek) + 1);
                });
        return activityMap;
    }
}
