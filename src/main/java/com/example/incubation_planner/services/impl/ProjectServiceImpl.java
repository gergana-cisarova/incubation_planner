package com.example.incubation_planner.services.impl;

import com.example.incubation_planner.models.entity.*;
import com.example.incubation_planner.models.entity.enums.Sector;
import com.example.incubation_planner.models.service.ProjectResultServiceModel;
import com.example.incubation_planner.models.service.ProjectServiceModel;
import com.example.incubation_planner.models.view.ProjectBasicViewModel;
import com.example.incubation_planner.models.view.ProjectDetailedViewModel;
import com.example.incubation_planner.models.view.ProjectResultViewModel;
import com.example.incubation_planner.repositories.ActivityTypeRepository;
import com.example.incubation_planner.repositories.EquipmentRepository;
import com.example.incubation_planner.repositories.LogRepository;
import com.example.incubation_planner.repositories.ProjectRepository;
import com.example.incubation_planner.services.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final LogRepository logRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final ActivityTypeRepository activityTypeRepository;
    private final LabService labService;
    private final EquipmentRepository equipmentRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, LogRepository logRepository, ModelMapper modelMapper, UserService userService, ActivityTypeRepository activityTypeRepository, LabService labService, EquipmentRepository equipmentRepository) {
        this.projectRepository = projectRepository;
        this.logRepository = logRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.activityTypeRepository = activityTypeRepository;
        this.labService = labService;
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public void createProject(ProjectServiceModel projectServiceModel) {

        Project project = modelMapper.map(projectServiceModel, Project.class);
        project.setPromoter(userService.findByUsername(projectServiceModel.getPromoter()))
                .setActivityType(activityTypeRepository.findByActivityName(projectServiceModel.getActivityType()).orElseThrow(NullPointerException::new))
                .setLab(labService.findLab(projectServiceModel.getLab()))
                .setNeededEquipment(equipmentRepository.findByEquipmentName(projectServiceModel.getNeededEquipment()).orElseThrow(NullPointerException::new));

        projectRepository.save(project);
    }

    @Override
    public List<ProjectBasicViewModel> getActiveProjectsOrderedbyStartDate() {
        return projectRepository
                .findAllByActiveTrueOrderByStartDateAsc()
                .stream()
                .map(p -> mapProject(p))
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDetailedViewModel extractProjectModel(String id) {
        Project project = projectRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        ProjectDetailedViewModel projectViewModel = modelMapper.map(project, ProjectDetailedViewModel.class);
        String firstName = userService.findByUsername(project.getPromoter().getUsername()).getFirstName();
        String lastName = userService.findByUsername(project.getPromoter().getUsername()).getLastName();
        projectViewModel.setPromoter(String.format("%s %s", firstName, lastName))
                .setActivityType(project.getActivityType().getActivityName())
                .setLab(project.getLab().getName())
                .setNeededEquipment(project.getNeededEquipment().getEquipmentName());

        String duration = String.format("%02d %s %s (%02d:%02d) - %02d %s %s (%02d:%02d) <br />",
                project.getStartDate().getDayOfMonth(), project.getStartDate().getMonth(), project.getStartDate().getYear(), project.getStartDate().getHour(), project.getStartDate().getMinute(),
                project.getEndDate().getDayOfMonth(), project.getEndDate().getMonth(), project.getEndDate().getYear(), project.getEndDate().getHour(), project.getEndDate().getMinute());
        projectViewModel.setDuration(duration);

        StringBuilder sb = new StringBuilder();
        project.getCollaborators().forEach(c -> {
            sb.append(String.format("%s %s<br />", c.getFirstName(), c.getLastName()));
        });
        String collaborators = sb.toString();
        projectViewModel.setCollaborators(collaborators);
        return projectViewModel;

    }

    @Override
    public void deleteProject(String id) {
       List<LogEntity> logs = logRepository.findByProject_Id(id);
       if (!logs.isEmpty()) {
           logs.forEach(l -> logRepository.delete(l));
       }
        projectRepository.deleteById(id);

    }

    @Override
    public List<ProjectBasicViewModel> getUserProjectsOrderedByStartDate(String username) {
        UserEntity user = userService.findByUsername(username);
        return projectRepository
                .findAllByActiveAndPromoterOrderByStartDate(true, user)
                .stream()
                .map(p -> mapProject(p))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectBasicViewModel> getUserCollaborationsOrderedByStartDate(String userName) {
        return userService
                .getProjectsByUser(userName)
                .stream()
                .sorted((a, b) -> a.getStartDate().compareTo(b.getStartDate()))
                .map(p -> mapProject(p))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProjectsOfUser(String id) {

        projectRepository.findAllByPromoterId(id).forEach(p -> deleteProject(p.getId()));
    }

    @Override
    public ProjectServiceModel findProjectById(String projectId) {
        Project project = projectRepository.getOne(projectId);
        return modelMapper.map(project, ProjectServiceModel.class);
    }

    @Override
    public void archiveProject(String id) {
        Project project = projectRepository.getOne(id);
        project.setActive(false);
        projectRepository.save(project);
    }

    @Override
    public String findProjectOwnerStr(String id) {
        return this.projectRepository.getOne(id).getPromoter().getUsername();
    }

    @Override
    public boolean joinProject(String id, String userName) {
        try {
            UserEntity user = userService.findByUsername(userName);
            Project project = projectRepository.findById(id).orElseThrow(IllegalArgumentException::new);
            user.addProject(project);
            project.addCollaborator(user);
            userService.updateUser(user);
            projectRepository.save(project);
            return true;
        } catch (Exception ex) {
            throw new IllegalArgumentException("User could not join project");
        }

    }

    @Override
    public boolean checkIfCollaborating(String id, String userName) {
        UserEntity user = userService.findByUsername(userName);
        Project project = projectRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        return user.getProjects().contains(project);
    }

    @Override
    public void leaveProject(String id, String userName) {
        try {
            UserEntity user = userService.findByUsername(userName);
            Project project = projectRepository.findById(id).orElseThrow(IllegalArgumentException::new);
            user.removeProject(project);
            project.removeCollaborator(user);
            userService.updateUser(user);
            projectRepository.save(project);
        } catch (Exception ex) {
            throw new IllegalArgumentException("User could not leave project");
        }
    }

    @Override
    public ProjectServiceModel extractProjectServiceModel(String id) {
        Project project = projectRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        ProjectServiceModel projectServiceModel = modelMapper.map(project, ProjectServiceModel.class)
                .setActivityType(project.getActivityType().getActivityName())
                .setLab(project.getLab().getName())
                .setNeededEquipment(project.getNeededEquipment().getEquipmentName());

        return projectServiceModel;
    }

    @Override
    public String getProjectPromoter(String id) {
        return projectRepository.getOne(id).getPromoter().getUsername();
    }

    @Override
    public void updateProject(String id, ProjectServiceModel projectServiceModel) {
        Project project = projectRepository.getOne(id)
                .setName(projectServiceModel.getName())
                .setDescription(projectServiceModel.getDescription());
        ActivityType currentActivity = activityTypeRepository.findByActivityName(projectServiceModel.getActivityType()).orElseThrow(NullPointerException::new);
        project.setActivityType(currentActivity)
                .setSector(projectServiceModel.getSector())
                .setStartDate(projectServiceModel.getStartDate())
                .setEndDate(projectServiceModel.getEndDate());
        Equipment currentEquipment = equipmentRepository.findByEquipmentName(projectServiceModel.getNeededEquipment()).orElseThrow(NullPointerException::new);
        project.setNeededEquipment(currentEquipment);
        Lab currentLab = labService.findLab(projectServiceModel.getLab());
        project.setLab(currentLab);
        projectRepository.save(project);
    }

    @Override
    public void publishProjectResult(ProjectResultServiceModel projectServiceModel) {
        Project project = projectRepository.getOne(projectServiceModel.getId());
        project.setResult(projectServiceModel.getDescription());
        projectRepository.save(project);
    }

    @Override
    public ProjectResultServiceModel extractProjectResultServiceModel(String id) {
        Project project = projectRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        return modelMapper.map(project, ProjectResultServiceModel.class);
    }

    @Override
    public List<ProjectResultViewModel> getResults(String param) {
        Sector sector = Sector.valueOf(param);
        return projectRepository.findAllResultsBySector(sector)
                .stream()
                .map(p -> modelMapper.map(p, ProjectResultViewModel.class))
                .collect(Collectors.toList());
    }


    private ProjectBasicViewModel mapProject(Project p) {
        ProjectBasicViewModel projectViewModel = modelMapper.map(p, ProjectBasicViewModel.class);
        projectViewModel.setActivityType(p.getActivityType().getActivityName())
                .setLab(p.getLab().getName());

        String startDate = String.format("%02d %s %s (%02d:%02d)",
                p.getStartDate().getDayOfMonth(), p.getStartDate().getMonth(),
                p.getStartDate().getYear(), p.getStartDate().getHour(),
                p.getStartDate().getMinute());

        projectViewModel.setStartDate(startDate);

        return projectViewModel;
    }

}

