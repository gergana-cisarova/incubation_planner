package com.example.incubation_planner.services;

import com.example.incubation_planner.models.view.AddIdeaLogViewModel;
import com.example.incubation_planner.models.view.JoinProjectLogViewModel;

import java.util.List;
import java.util.Map;

public interface LogService {

    void createProjectJoinLog(String action, String projectId);

    List<JoinProjectLogViewModel> findAllJoinProjectLogs();

    void createIdeaAddLog(String action, String ideaId);

    List<AddIdeaLogViewModel>  findAllIdeaAddLogs();

    Map<Integer, Integer> getStatsIdeasCreated();

    Map<Integer, Integer> getStatsJoinProjectActivity();


}
