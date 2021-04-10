package com.example.incubation_planner.models.entity;

import com.example.incubation_planner.models.entity.enums.Sector;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Sector sector;

    @Column(columnDefinition = "Text", nullable = false)
    private String description;

    @Column(name = "starting_date")
    @NotNull
    private LocalDate startDate;

    @Column(name = "end_date")
    @NotNull
    private LocalDate endDate;

    @ManyToOne
    @NotNull
    private ActivityType activityType;

    @ManyToOne
    @NotNull
    private Equipment neededEquipment;

    @ManyToOne
    @JoinColumn(name = "lab_id", referencedColumnName = "id")
    @NotNull
    private Lab lab;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "result", columnDefinition = "Text")
    private String result;

    @ManyToOne
    private UserEntity  promoter;

    @ManyToMany
    @JoinTable(name = "projects_users",
            joinColumns = @JoinColumn(name="project_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="user_id", referencedColumnName="id"))
    private Set<UserEntity> collaborators = new HashSet<>();

    public Project() {
    }

    public String getName() {
        return name;
    }

    public Project setName(String name) {
        this.name = name;
        return this;
    }

    public Sector getSector() {
        return sector;
    }

    public Project setSector(Sector sector) {
        this.sector = sector;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Project setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Project setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Project setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public Project setActivityType(ActivityType activityType) {
        this.activityType = activityType;
        return this;
    }

    public Equipment getNeededEquipment() {
        return neededEquipment;
    }

    public Project setNeededEquipment(Equipment neededEquipment) {
        this.neededEquipment = neededEquipment;
        return this;
    }

    public Lab getLab() {
        return lab;
    }

    public Project setLab(Lab lab) {
        this.lab = lab;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public Project setActive(boolean active) {
        this.active = active;
        return this;
    }

    public UserEntity getPromoter() {
        return promoter;
    }

    public Project setPromoter(UserEntity promoter) {
        this.promoter = promoter;
        return this;
    }

    public Set<UserEntity> getCollaborators() {
        return collaborators;
    }

    public Project setCollaborators(Set<UserEntity> collaborators) {
        this.collaborators = collaborators;
        return this;
    }

    public String getResult() {
        return result;
    }

    public Project setResult(String result) {
        this.result = result;
        return this;
    }
    public void addCollaborator(UserEntity user) {
        this.collaborators.add(user);
    }
    public void removeCollaborator(UserEntity user) {
        this.collaborators.remove(user);
    }

}
