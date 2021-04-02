package com.example.incubation_planner.models.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
public class LogEntity extends BaseEntity {
    @ManyToOne
    private UserEntity user;

    @Column(nullable = false)
    private String action;

    @ManyToOne
    private Project project;

    @ManyToOne
    private Idea idea;


    @Column(nullable = false)
    private LocalDateTime time;

    public LogEntity() {
    }

    public UserEntity getUser() {
        return user;
    }

    public LogEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public String getAction() {
        return action;
    }

    public LogEntity setAction(String action) {
        this.action = action;
        return this;
    }

    public Project getProject() {
        return project;
    }

    public LogEntity setProject(Project project) {
        this.project = project;
        return this;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public LogEntity setTime(LocalDateTime time) {
        this.time = time;
        return this;
    }

    public Idea getIdea() {
        return idea;
    }

    public LogEntity setIdea(Idea idea) {
        this.idea = idea;
        return this;
    }
}
