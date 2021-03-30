package com.example.incubation_planner.models.view;

import com.example.incubation_planner.models.entity.enums.Sector;

public class ProjectDetailedViewModel {

    private String id;

    private String name;

    private Sector sector;

    private String description;

    private String duration;

    private String activityType;

    private String neededEquipment;

    private String lab;

    private String promoter;

   private String collaborators;

    public String getId() {
        return id;
    }

    public ProjectDetailedViewModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProjectDetailedViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public Sector getSector() {
        return sector;
    }

    public ProjectDetailedViewModel setSector(Sector sector) {
        this.sector = sector;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProjectDetailedViewModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDuration() {
        return duration;
    }

    public ProjectDetailedViewModel setDuration(String duration) {
        this.duration = duration;
        return this;
    }

    public String getActivityType() {
        return activityType;
    }

    public ProjectDetailedViewModel setActivityType(String activityType) {
        this.activityType = activityType;
        return this;
    }

    public String getNeededEquipment() {
        return neededEquipment;
    }

    public ProjectDetailedViewModel setNeededEquipment(String neededEquipment) {
        this.neededEquipment = neededEquipment;
        return this;
    }

    public String getLab() {
        return lab;
    }

    public ProjectDetailedViewModel setLab(String lab) {
        this.lab = lab;
        return this;
    }

    public String getPromoter() {
        return promoter;
    }

    public ProjectDetailedViewModel setPromoter(String promoter) {
        this.promoter = promoter;
        return this;
    }

    public String getCollaborators() {
        return collaborators;
    }

    public ProjectDetailedViewModel setCollaborators(String collaborators) {
        this.collaborators = collaborators;
        return this;
    }
}
