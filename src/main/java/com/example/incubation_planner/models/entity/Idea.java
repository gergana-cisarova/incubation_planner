package com.example.incubation_planner.models.entity;

import com.example.incubation_planner.models.entity.enums.EquipmentName;
import com.example.incubation_planner.models.entity.enums.Sector;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ideas")
public class Idea extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Sector sector;

    @Column(columnDefinition = "Text", nullable = false)
    private String description;

    @Column(nullable = false)
    private int duration;

    @Column
    private String status = "Pending";

    @ManyToOne
    @NotNull
    private Equipment neededEquipment;

    @ManyToOne
    @NotNull
    private ActivityType activityType;

    @ManyToOne
    @NotNull
    private UserEntity promoter;

    public String getName() {
        return name;
    }

    public Idea setName(String name) {
        this.name = name;
        return this;
    }

    public Sector getSector() {
        return sector;
    }

    public Idea setSector(Sector sector) {
        this.sector = sector;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Idea setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public Idea setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public Equipment getNeededEquipment() {
        return neededEquipment;
    }

    public Idea setNeededEquipment(Equipment neededEquipment) {
        this.neededEquipment = neededEquipment;
        return this;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public Idea setActivityType(ActivityType activityType) {
        this.activityType = activityType;
        return this;
    }

    public UserEntity getPromoter() {
        return promoter;
    }

    public Idea setPromoter(UserEntity promoter) {
        this.promoter = promoter;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Idea setStatus(String status) {
        this.status = status;
        return this;
    }
}
