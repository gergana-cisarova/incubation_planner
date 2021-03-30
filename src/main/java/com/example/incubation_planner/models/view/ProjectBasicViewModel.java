package com.example.incubation_planner.models.view;

import com.example.incubation_planner.models.entity.enums.Sector;

public class ProjectBasicViewModel {

    private String id;

    private String name;

    private String sector;

    private String startDate;

    private String activityType;

    private String lab;

    public String getId() {
        return id;
    }

    public ProjectBasicViewModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProjectBasicViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getSector() {
        return sector;
    }

    public ProjectBasicViewModel setSector(String sector) {
        this.sector = sector;
        return this;
    }

    public String getStartDate() {
        return startDate;
    }

    public ProjectBasicViewModel setStartDate(String startDate) {
        this.startDate = startDate;
        return this;
    }

    public String getActivityType() {
        return activityType;
    }

    public ProjectBasicViewModel setActivityType(String activityType) {
        this.activityType = activityType;
        return this;
    }

    public String getLab() {
        return lab;
    }

    public ProjectBasicViewModel setLab(String lab) {
        this.lab = lab;
        return this;
    }
}
