package com.example.incubation_planner.models.service;

import com.google.gson.annotations.Expose;

public class LabServiceModel {
    private String id;
    @Expose
    private String name;
    @Expose
    private String equipment;

    public String getId() {
        return id;
    }

    public LabServiceModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public LabServiceModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getEquipment() {
        return equipment;
    }

    public LabServiceModel setEquipment(String equipment) {
        this.equipment = equipment;
        return this;
    }
}
