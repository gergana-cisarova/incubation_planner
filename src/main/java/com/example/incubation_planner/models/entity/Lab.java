package com.example.incubation_planner.models.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "labs")
public class Lab extends BaseEntity {

    @Column(name = "lab_name", unique=true, nullable = false)
    private String name;

    @ManyToOne
    @NotNull
    private Equipment equipment;

    @OneToMany(mappedBy = "lab", targetEntity = Project.class)
    private List<Project> projects;

    public Lab() {
        this.projects = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Lab setName(String name) {
        this.name = name;
        return this;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public Lab setEquipment(Equipment equipment) {
        this.equipment = equipment;
        return this;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public Lab setProjects(List<Project> projects) {
        this.projects = projects;
        return this;
    }
}
