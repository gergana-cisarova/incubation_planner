package com.example.incubation_planner.models.service;

import com.google.gson.annotations.Expose;

public class ActivityTypeServiceModel {

    private String id;
    @Expose
    private String activityName;

    public String getId() {
        return id;
    }

    public ActivityTypeServiceModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getActivityName() {
        return activityName;
    }

    public ActivityTypeServiceModel setActivityName(String activityName) {
        this.activityName = activityName;
        return this;
    }
}