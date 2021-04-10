package com.example.incubation_planner.models.service;

import com.example.incubation_planner.models.entity.enums.Sector;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProjectServiceModel {

    private String id;
    private String name;
    private Sector sector;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String activityType;
    private String neededEquipment;
    private String lab;
    private String promoter;

    public String getId() {
        return id;
    }

    public ProjectServiceModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProjectServiceModel setName(String name) {
        this.name = name;
        return this;
    }

    public Sector getSector() {
        return sector;
    }

    public ProjectServiceModel setSector(Sector sector) {
        this.sector = sector;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProjectServiceModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public ProjectServiceModel setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public ProjectServiceModel setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public String getActivityType() {
        return activityType;
    }

    public ProjectServiceModel setActivityType(String activityType) {
        this.activityType = activityType;
        return this;
    }

    public String getNeededEquipment() {
        return neededEquipment;
    }

    public ProjectServiceModel setNeededEquipment(String neededEquipment) {
        this.neededEquipment = neededEquipment;
        return this;
    }

    public String getLab() {
        return lab;
    }

    public ProjectServiceModel setLab(String lab) {
        this.lab = lab;
        return this;
    }

    public String getPromoter() {
        return promoter;
    }

    public ProjectServiceModel setPromoter(String promoter) {
        this.promoter = promoter;
        return this;
    }
}
