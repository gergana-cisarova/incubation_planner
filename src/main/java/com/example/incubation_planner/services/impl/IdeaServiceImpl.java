package com.example.incubation_planner.services.impl;

import com.example.incubation_planner.models.entity.Idea;
import com.example.incubation_planner.models.entity.LogEntity;
import com.example.incubation_planner.models.entity.UserEntity;
import com.example.incubation_planner.models.service.IdeaLogServiceModel;
import com.example.incubation_planner.models.service.IdeaServiceModel;
import com.example.incubation_planner.models.view.IdeaViewModel;
import com.example.incubation_planner.repositories.*;
import com.example.incubation_planner.services.IdeaService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IdeaServiceImpl implements IdeaService {
    private Logger LOGGER = LoggerFactory.getLogger(IdeaServiceImpl.class);

    private final IdeaRepository ideaRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final ActivityTypeRepository activityTypeRepository;
    private final LogRepository logRepository;
    private final EquipmentRepository equipmentRepository;

    public IdeaServiceImpl(IdeaRepository ideaRepository, ModelMapper modelMapper, UserRepository userRepository, ActivityTypeRepository activityTypeRepository, LogRepository logRepository, EquipmentRepository equipmentRepository) {
        this.ideaRepository = ideaRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.logRepository = logRepository;
        this.equipmentRepository = equipmentRepository;
        this.activityTypeRepository = activityTypeRepository;
    }

    @Override
    public IdeaViewModel createIdea(IdeaServiceModel ideaServiceModel) {
        Idea idea = modelMapper.map(ideaServiceModel, Idea.class);
        idea.setActivityType(activityTypeRepository.findByActivityName(ideaServiceModel.getActivityType()).orElseThrow(NullPointerException::new));
        idea.setNeededEquipment(equipmentRepository.findByEquipmentName(ideaServiceModel.getNeededEquipment()).orElseThrow(NullPointerException::new));
        idea.setPromoter(userRepository.findByUsername(ideaServiceModel.getPromoter()).orElseThrow(NullPointerException::new));

        ideaRepository.save(idea);

        IdeaViewModel ideaViewModel = modelMapper.map(idea, IdeaViewModel.class);
        ideaViewModel.setNeededEquipment(idea.getNeededEquipment().getEquipmentName());
        ideaViewModel.setPromoter(idea.getPromoter().getUsername());
        ideaViewModel.setActivityType(idea.getActivityType().getActivityName());
        return ideaViewModel;
    }

    @Override
    @Transactional
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
        ideaServiceModel
                .setPromoter(idea.getPromoter().getUsername())
                .setActivityType(idea.getActivityType().getActivityName())
                .setNeededEquipment(idea.getNeededEquipment().getEquipmentName());

        return ideaServiceModel;
    }

    @Override
    public List<String> deleteIdea(String id) {
        List<LogEntity> logs = logRepository.findByIdea_Id(id);
        List<String> deletedLoggedIdeaNames = new ArrayList<>();
        if (!logs.isEmpty()) {
            logs.forEach(l -> {
                deletedLoggedIdeaNames.add(l.getIdea().getName());
                logRepository.delete(l);
            });
        }
        deletedLoggedIdeaNames.forEach(l -> LOGGER.info("Deleted logs for idea : {}", l));
        ideaRepository.deleteById(id);
        return deletedLoggedIdeaNames;
    }

    @Override
    public boolean markIdeaAsAccepted(String id) {
        Optional<Idea> currentIdea = ideaRepository.findById(id);
        if (currentIdea.isPresent()) {
            Idea idea = currentIdea.get();
            idea.setStatus("Accepted");
            ideaRepository.save(idea);
            return true;
        } else {
            return false;
        }
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
    public List<String> deleteIdeasOfUser(String id) {
        List<Idea> ideas = ideaRepository.findAllByPromoterId(id);
        List<String> deletedIdeasNames = new ArrayList<>();
        ideas.forEach(i -> {
            deletedIdeasNames.add(i.getName());
            deleteIdea(i.getId());
        });
        deletedIdeasNames.forEach(l -> LOGGER.info("Deleted the following ideas {}", String.join(",", deletedIdeasNames)));
        return deletedIdeasNames;
    }

    @Override
    public IdeaLogServiceModel generateIdeaServiceModel(String ideaName) {
        Idea idea = ideaRepository.findByName(ideaName).orElse(null);
        if (idea != null) {
            IdeaLogServiceModel ideaLogServiceModel = modelMapper.map(idea, IdeaLogServiceModel.class);
            ideaLogServiceModel.setPromoter(idea.getPromoter().getUsername())
                    .setActivityType(idea.getActivityType().getActivityName())
                    .setNeededEquipment(idea.getNeededEquipment().getEquipmentName());
            return ideaLogServiceModel;
        }
        return null;
    }


    IdeaViewModel mapIdea(Idea idea) {
        IdeaViewModel ideaViewModel = modelMapper.map(idea, IdeaViewModel.class);
        UserEntity user = userRepository.findByUsername(idea.getPromoter().getUsername()).orElseThrow(NullPointerException::new);
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        ideaViewModel.setPromoter(String.format("%s %s", firstName, lastName))
                .setActivityType(idea.getActivityType().getActivityName())
                .setNeededEquipment(idea.getNeededEquipment().getEquipmentName());
        return ideaViewModel;
    }
}
