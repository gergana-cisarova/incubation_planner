package com.example.incubation_planner.services;

import com.example.incubation_planner.models.service.IdeaServiceModel;
import com.example.incubation_planner.models.view.IdeaViewModel;

import java.util.List;

public interface IdeaService {
    void createIdea(IdeaServiceModel ideaServiceModel);

    List<IdeaViewModel> getAll();

    IdeaServiceModel extractIdeaModel(String id);

    void deleteIdea(String id);

    void markIdeaAsAccepted(String id);

    int getDurationOfIdea(String id);

    IdeaViewModel getIdeaView(String id);

    void deleteIdeasOfUser(String id);

    IdeaServiceModel generateIdeaServiceModel(String ideaName);
}
