package com.example.incubation_planner.models.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "activity_types")
public class ActivityType extends BaseEntity {

    @Column(name = "activity_name", unique = true, nullable = false)
    private String activityName;


    public ActivityType() {
    }

    public String getActivityName() {
        return activityName;
    }

    public ActivityType setActivityName(String activityName) {
        this.activityName = activityName;
        return this;
    }


}
