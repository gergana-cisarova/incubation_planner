package com.example.incubation_planner.services;

import com.example.incubation_planner.models.service.IdeaLogServiceModel;
import com.example.incubation_planner.models.service.IdeaServiceModel;
import com.example.incubation_planner.models.view.IdeaViewModel;

import java.util.List;

public interface IdeaService {

    IdeaViewModel createIdea(IdeaServiceModel ideaServiceModel);

    List<IdeaViewModel> getAll();

    IdeaServiceModel extractIdeaModel(String id);

    List<String> deleteIdea(String id);

    boolean markIdeaAsAccepted(String id);

    int getDurationOfIdea(String id);

    IdeaViewModel getIdeaView(String id);

    List<String> deleteIdeasOfUser(String id);

    IdeaLogServiceModel generateIdeaServiceModel(String ideaName);
}
