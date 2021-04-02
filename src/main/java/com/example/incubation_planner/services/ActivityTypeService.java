package com.example.incubation_planner.services;

import com.example.incubation_planner.models.entity.ActivityType;
import com.example.incubation_planner.models.service.ActivityTypeServiceModel;

import java.util.List;

public interface ActivityTypeService {

    void seedActivityTypes();

    void addNewActivity(ActivityTypeServiceModel activityTypeServiceModel);

    List<String> getAllActivities();
}
