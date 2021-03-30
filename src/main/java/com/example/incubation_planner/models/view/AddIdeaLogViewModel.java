package com.example.incubation_planner.models.view;

public class AddIdeaLogViewModel {

    private String id;

    private String user;

    private String action;

    private String idea;

    private String dateTime;

    public String getId() {
        return id;
    }

    public AddIdeaLogViewModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getUser() {
        return user;
    }

    public AddIdeaLogViewModel setUser(String user) {
        this.user = user;
        return this;
    }

    public String getAction() {
        return action;
    }

    public AddIdeaLogViewModel setAction(String action) {
        this.action = action;
        return this;
    }

    public String getIdea() {
        return idea;
    }

    public AddIdeaLogViewModel setIdea(String idea) {
        this.idea = idea;
        return this;
    }

    public String getDateTime() {
        return dateTime;
    }

    public AddIdeaLogViewModel setDateTime(String dateTime) {
        this.dateTime = dateTime;
        return this;
    }
}
