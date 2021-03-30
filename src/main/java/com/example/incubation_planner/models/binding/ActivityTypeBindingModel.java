package com.example.incubation_planner.models.binding;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class ActivityTypeBindingModel {
    @NotEmpty
    @Size(min=3)
    private String activityName;


    public String getActivityName() {
        return activityName;
    }

    public ActivityTypeBindingModel setActivityName(String activityName) {
        this.activityName = activityName;
        return this;
    }

}