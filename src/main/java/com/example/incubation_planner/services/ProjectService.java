package com.example.incubation_planner.services;

import com.example.incubation_planner.models.service.ProjectResultServiceModel;
import com.example.incubation_planner.models.service.ProjectServiceModel;
import com.example.incubation_planner.models.view.ProjectBasicViewModel;
import com.example.incubation_planner.models.view.ProjectDetailedViewModel;
import com.example.incubation_planner.models.view.ProjectResultViewModel;

import java.util.List;

public interface ProjectService {
    ProjectBasicViewModel createProject(ProjectServiceModel projectServiceModel);

    List<ProjectBasicViewModel> getActiveProjectsOrderedbyStartDate();

    ProjectDetailedViewModel extractProjectModel(String id);

    List<String> deleteProject(String id);

    List<ProjectBasicViewModel> getUserProjectsOrderedByStartDate(String username);

    void archiveProject(String id);

    String findProjectOwnerStr(String id);

    boolean joinProject(String id, String userName);

    boolean checkIfCollaborating(String id, String userName);

    void leaveProject(String id, String userName);

    ProjectServiceModel extractProjectServiceModel(String id);

    String getProjectPromoter(String id);

    void updateProject(String id, ProjectServiceModel projectServiceModel);

    void publishProjectResult(ProjectResultServiceModel projectServiceModel);

    ProjectResultServiceModel extractProjectResultServiceModel(String id);

    List<ProjectResultViewModel> getResults(String it);

    List<ProjectBasicViewModel> getUserCollaborationsOrderedByStartDate(String userName);

    long getDurationInDays(ProjectServiceModel projectAddBindingModel);

    void deleteProjectsOfUser(String id);

    ProjectServiceModel findProjectById(String projectId);
}
