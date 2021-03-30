package com.example.incubation_planner.models.view;

public class IdeaViewModel {


    private String id;

    private String name;

    private String sector;

    private String description;

    private int duration;

    private String neededEquipment;

    private String activityType;

    private String promoter;
    private String status;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public IdeaViewModel setId(String id) {
        this.id = id;
        return this;
    }

    public IdeaViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getSector() {
        return sector;
    }

    public IdeaViewModel setSector(String sector) {
        this.sector = sector;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public IdeaViewModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public IdeaViewModel setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public String getNeededEquipment() {
        return neededEquipment;
    }

    public IdeaViewModel setNeededEquipment(String neededEquipment) {
        this.neededEquipment = neededEquipment;
        return this;
    }

    public String getActivityType() {
        return activityType;
    }

    public IdeaViewModel setActivityType(String activityType) {
        this.activityType = activityType;
        return this;
    }

    public String getPromoter() {
        return promoter;
    }

    public IdeaViewModel setPromoter(String promoter) {
        this.promoter = promoter;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public IdeaViewModel setStatus(String status) {
        this.status = status;
        return this;
    }
}
