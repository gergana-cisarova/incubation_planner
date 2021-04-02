package com.example.incubation_planner.services.impl;

import com.example.incubation_planner.models.entity.ActivityType;
import com.example.incubation_planner.models.service.ActivityTypeServiceModel;
import com.example.incubation_planner.repositories.ActivityTypeRepository;
import com.example.incubation_planner.services.ActivityTypeService;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityTypeServiceImpl implements ActivityTypeService {
    private final Resource activityTypeFile;
    private final Gson gson;
    private final ActivityTypeRepository activityTypeRepository;
    private final ModelMapper modelMapper;

    public ActivityTypeServiceImpl(
            @Value("classpath:init/activityType.json") Resource activityTypeFile,
            Gson gson,
            ActivityTypeRepository activityTypeRepository,
            ModelMapper modelMapper
    ) {
        this.activityTypeFile = activityTypeFile;
        this.gson = gson;
        this.activityTypeRepository = activityTypeRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public void seedActivityTypes() {
        if (activityTypeRepository.count() == 0) {
            try {
                ActivityTypeServiceModel[] activityTypeServiceModels = gson.fromJson(Files.readString(Path.of(activityTypeFile.getURI())), ActivityTypeServiceModel[].class);
                Arrays.stream(activityTypeServiceModels)
                        .forEach(m -> {
                            ActivityType current = modelMapper.map(m, ActivityType.class);
                            activityTypeRepository.save(current);
                        });

            } catch (IOException e) {
                throw new IllegalStateException("Cannot seed Activity Types");
            }

        }
    }

    @Override
    public void addNewActivity(ActivityTypeServiceModel activityTypeServiceModel) {
        activityTypeRepository.save(modelMapper.map(activityTypeServiceModel, ActivityType.class));
    }


    @Override
    public List<String> getAllActivities() {
        return activityTypeRepository.findAll().stream().map(ActivityType::getActivityName).collect(Collectors.toList());
    }
}
