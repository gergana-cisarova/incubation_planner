package com.example.incubation_planner.models.service;

import com.example.incubation_planner.models.entity.enums.Sector;

public class ProjectResultServiceModel {

    private String id;
    private String name;

    private Sector sector;

    private String description;

    public String getId() {
        return id;
    }

    public ProjectResultServiceModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProjectResultServiceModel setName(String name) {
        this.name = name;
        return this;
    }

    public Sector getSector() {
        return sector;
    }

    public ProjectResultServiceModel setSector(Sector sector) {
        this.sector = sector;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProjectResultServiceModel setDescription(String description) {
        this.description = description;
        return this;
    }
}
