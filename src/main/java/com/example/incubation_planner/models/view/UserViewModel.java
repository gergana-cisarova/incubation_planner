package com.example.incubation_planner.models.view;

public class UserViewModel {

    private String id;
    private String username;
    private String fullNameAndEmail;
    private String activeProjects;
    private String archivesProjects;
    private String activeCollabs;
    private String sectorAndType;
    private String roles;

    public String getId() {
        return id;
    }

    public UserViewModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserViewModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getFullNameAndEmail() {
        return fullNameAndEmail;
    }

    public UserViewModel setFullNameAndEmail(String fullNameAndEmail) {
        this.fullNameAndEmail = fullNameAndEmail;
        return this;
    }

    public String getActiveProjects() {
        return activeProjects;
    }

    public UserViewModel setActiveProjects(String activeProjects) {
        this.activeProjects = activeProjects;
        return this;
    }

    public String getArchivesProjects() {
        return archivesProjects;
    }

    public UserViewModel setArchivesProjects(String archivesProjects) {
        this.archivesProjects = archivesProjects;
        return this;
    }

    public String getActiveCollabs() {
        return activeCollabs;
    }

    public UserViewModel setActiveCollabs(String activeCollabs) {
        this.activeCollabs = activeCollabs;
        return this;
    }

    public String getSectorAndType() {
        return sectorAndType;
    }

    public UserViewModel setSectorAndType(String sectorAndType) {
        this.sectorAndType = sectorAndType;
        return this;
    }

    public String getRoles() {
        return roles;
    }

    public UserViewModel setRoles(String roles) {
        this.roles = roles;
        return this;
    }
}
