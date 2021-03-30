package com.example.incubation_planner.models.view;

public class JoinProjectLogViewModel {

    private String id;

    private String user;

    private String action;

    private String project;

    private String dateTime;

    public String getId() {
        return id;
    }

    public JoinProjectLogViewModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getUser() {
        return user;
    }

    public JoinProjectLogViewModel setUser(String user) {
        this.user = user;
        return this;
    }

    public String getAction() {
        return action;
    }

    public JoinProjectLogViewModel setAction(String action) {
        this.action = action;
        return this;
    }

    public String getProject() {
        return project;
    }

    public JoinProjectLogViewModel setProject(String project) {
        this.project = project;
        return this;
    }

    public String getDateTime() {
        return dateTime;
    }

    public JoinProjectLogViewModel setDateTime(String dateTime) {
        this.dateTime = dateTime;
        return this;
    }
}
