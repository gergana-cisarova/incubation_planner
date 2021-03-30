package com.example.incubation_planner.models.view;

import com.example.incubation_planner.models.entity.enums.Sector;

public class ProjectResultViewModel {


    private String name;

    private String result;

    public String getName() {
        return name;
    }

    public ProjectResultViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getResult() {
        return result;
    }

    public ProjectResultViewModel setResult(String result) {
        this.result = result;
        return this;
    }
}
