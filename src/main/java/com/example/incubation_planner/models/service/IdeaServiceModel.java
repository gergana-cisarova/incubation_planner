package com.example.incubation_planner.models.service;

import com.example.incubation_planner.models.entity.enums.Sector;

public class IdeaServiceModel {
    private String id;

    private String name;

    private Sector sector;

    private String description;

    private int duration;

    private String neededEquipment;

    private String activityType;

    private String promoter;

    public String getId() {
        return id;
    }

    public IdeaServiceModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public IdeaServiceModel setName(String name) {
        this.name = name;
        return this;
    }

    public Sector getSector() {
        return sector;
    }

    public IdeaServiceModel setSector(Sector sector) {
        this.sector = sector;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public IdeaServiceModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public IdeaServiceModel setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public String getNeededEquipment() {
        return neededEquipment;
    }

    public IdeaServiceModel setNeededEquipment(String neededEquipment) {
        this.neededEquipment = neededEquipment;
        return this;
    }

    public String getActivityType() {
        return activityType;
    }

    public IdeaServiceModel setActivityType(String activityType) {
        this.activityType = activityType;
        return this;
    }

    public String getPromoter() {
        return promoter;
    }

    public IdeaServiceModel setPromoter(String promoter) {
        this.promoter = promoter;
        return this;
    }
}
