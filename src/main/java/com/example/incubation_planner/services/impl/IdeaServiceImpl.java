package com.example.incubation_planner.services.impl;

import com.example.incubation_planner.models.entity.Idea;
import com.example.incubation_planner.models.service.IdeaLogServiceModel;
import com.example.incubation_planner.models.service.IdeaServiceModel;
import com.example.incubation_planner.models.view.IdeaViewModel;
import com.example.incubation_planner.repositories.IdeaRepository;
import com.example.incubation_planner.services.ActivityTypeService;
import com.example.incubation_planner.services.EquipmentService;
import com.example.incubation_planner.services.IdeaService;
import com.example.incubation_planner.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IdeaServiceImpl implements IdeaService {

    private final IdeaRepository ideaRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final ActivityTypeService activityTypeService;
    private final EquipmentService equipmentService;

    public IdeaServiceImpl(IdeaRepository ideaRepository, ModelMapper modelMapper, UserService userService, ActivityTypeService activityTypeService, EquipmentService equipmentService) {
        this.ideaRepository = ideaRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.activityTypeService = activityTypeService;
        this.equipmentService = equipmentService;
    }

    @Override
    public void createIdea(IdeaServiceModel ideaServiceModel) {
        Idea idea = modelMapper.map(ideaServiceModel, Idea.class);
        idea.setActivityType(activityTypeService.findByActivityName(ideaServiceModel.getActivityType()));
        idea.setNeededEquipment(equipmentService.findEquipment(ideaServiceModel.getNeededEquipment()));
        idea.setPromoter(userService.findByUsername(ideaServiceModel.getPromoter()));

        ideaRepository.save(idea);
    }

    @Override
    public List<IdeaViewModel> getAll() {
        return ideaRepository
                .findAllByOrderByStatusDesc()
                .stream()
                .map(i -> mapIdea(i))
                .collect(Collectors.toList());
    }

    @Override
    public IdeaServiceModel extractIdeaModel(String id) {
        Idea idea = ideaRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        IdeaServiceModel ideaServiceModel = modelMapper.map(idea, IdeaServiceModel.class);
        ideaServiceModel.setPromoter(idea.getPromoter().getUsername())
                .setActivityType(idea.getActivityType().getActivityName())
                .setNeededEquipment(idea.getNeededEquipment().getEquipmentName());

        return ideaServiceModel;
    }

    @Override
    public void deleteIdea(String id) {
        ideaRepository.deleteById(id);
    }

    @Override
    public void markIdeaAsAccepted(String id) {
        Idea currentIdea = ideaRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        currentIdea.setStatus("Accepted");
        ideaRepository.save(currentIdea);
    }

    @Override
    public int getDurationOfIdea(String id) {
        int duration = 0;
        if (ideaRepository.findById(id).isPresent()) {
            duration = ideaRepository.findById(id).get().getDuration();
        }
        return duration;
    }

    @Override
    public IdeaViewModel getIdeaView(String id) {
        Idea idea = ideaRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        return mapIdea(idea);
    }

    @Override
    public void deleteIdeasOfUser(String id) {
        List<Idea> ideas = ideaRepository.findAllByPromoterId(id);
        ideas.forEach(i -> deleteIdea(i.getId()));
    }

    @Override
    public IdeaLogServiceModel generateIdeaServiceModel(String ideaName) {
        Idea idea = ideaRepository.findByName(ideaName).orElseThrow(IllegalArgumentException::new);
        IdeaLogServiceModel ideaLogServiceModel = modelMapper.map(idea, IdeaLogServiceModel.class);
        ideaLogServiceModel.setPromoter(idea.getPromoter().getUsername())
                .setActivityType(idea.getActivityType().getActivityName())
                .setNeededEquipment(idea.getNeededEquipment().getEquipmentName());

        return ideaLogServiceModel;
    }


    private IdeaViewModel mapIdea(Idea idea) {
        IdeaViewModel ideaViewModel = modelMapper.map(idea, IdeaViewModel.class);
        String firstName = userService.findByUsername(idea.getPromoter().getUsername()).getFirstName();
        String lastName = userService.findByUsername(idea.getPromoter().getUsername()).getLastName();
        ideaViewModel.setPromoter(String.format("%s %s", firstName, lastName))
                .setActivityType(idea.getActivityType().getActivityName())
                .setNeededEquipment(idea.getNeededEquipment().getEquipmentName());
        return ideaViewModel;
    }
}
