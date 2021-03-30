package com.example.incubation_planner.models.binding;

import com.example.incubation_planner.models.entity.enums.Sector;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ProjectResultBindingModel {


    @NotEmpty
    @Size(min=50, max = 5000)
    private String description;


    public String getDescription() {
        return description;
    }

    public ProjectResultBindingModel setDescription(String description) {
        this.description = description;
        return this;
    }
}
