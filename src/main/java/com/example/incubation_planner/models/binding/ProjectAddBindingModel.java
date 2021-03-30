package com.example.incubation_planner.models.binding;

import com.example.incubation_planner.models.entity.enums.Sector;
import com.example.incubation_planner.models.validator.FieldMatch;
import com.example.incubation_planner.models.validator.ValidDates;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
@ValidDates(
        first = "startDate",
        second = "endDate"
)
public class ProjectAddBindingModel {

    @NotEmpty
    @Size(min=3, max = 250)
    private String name;

    @NotNull
    private Sector sector;

    @NotEmpty
    @Size(min=10, max = 1500)
    private String description;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @FutureOrPresent(message = "The date cannot be in the past")
    private LocalDateTime startDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @FutureOrPresent(message = "The date cannot be in the past")
    private LocalDateTime endDate;

    @NotEmpty
    private String activityType;

    @NotEmpty
    private String neededEquipment;

    @NotEmpty
    private String lab;


    public String getName() {
        return name;
    }

    public ProjectAddBindingModel setName(String name) {
        this.name = name;
        return this;
    }

    public Sector getSector() {
        return sector;
    }

    public ProjectAddBindingModel setSector(Sector sector) {
        this.sector = sector;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProjectAddBindingModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public ProjectAddBindingModel setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public ProjectAddBindingModel setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public String getActivityType() {
        return activityType;
    }

    public ProjectAddBindingModel setActivityType(String activityType) {
        this.activityType = activityType;
        return this;
    }

    public String getNeededEquipment() {
        return neededEquipment;
    }

    public ProjectAddBindingModel setNeededEquipment(String neededEquipment) {
        this.neededEquipment = neededEquipment;
        return this;
    }

    public String getLab() {
        return lab;
    }

    public ProjectAddBindingModel setLab(String lab) {
        this.lab = lab;
        return this;
    }


}
