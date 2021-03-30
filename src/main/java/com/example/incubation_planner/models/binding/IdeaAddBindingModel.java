package com.example.incubation_planner.models.binding;

import com.example.incubation_planner.models.entity.enums.Sector;

import javax.validation.constraints.*;

public class IdeaAddBindingModel {

    @NotEmpty
    @Size(min=3, max = 250)
    private String name;

    @NotNull
    private Sector sector;

    @NotEmpty
    @Size(min=10, max = 1500)
    private String description;

    @Max(value = 5)
    @Min(value = 1)
    private int duration;

    @NotEmpty
    private String neededEquipment;

    @NotEmpty
    private String activityType;


    public String getName() {
        return name;
    }

    public IdeaAddBindingModel setName(String name) {
        this.name = name;
        return this;
    }

    public Sector getSector() {
        return sector;
    }

    public IdeaAddBindingModel setSector(Sector sector) {
        this.sector = sector;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public IdeaAddBindingModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public IdeaAddBindingModel setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public String getNeededEquipment() {
        return neededEquipment;
    }

    public IdeaAddBindingModel setNeededEquipment(String neededEquipment) {
        this.neededEquipment = neededEquipment;
        return this;
    }

    public String getActivityType() {
        return activityType;
    }

    public IdeaAddBindingModel setActivityType(String activityType) {
        this.activityType = activityType;
        return this;
    }
}
