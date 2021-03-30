package com.example.incubation_planner.models.service;

import com.example.incubation_planner.models.entity.enums.Sector;
import com.example.incubation_planner.models.entity.enums.UserType;

public class UserRegistrationServiceModel {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private UserType userType;
    private Sector sector;

    public String getId() {
        return id;
    }

    public UserRegistrationServiceModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserRegistrationServiceModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserRegistrationServiceModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserRegistrationServiceModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserRegistrationServiceModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserRegistrationServiceModel setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserType getUserType() {
        return userType;
    }

    public UserRegistrationServiceModel setUserType(UserType userType) {
        this.userType = userType;
        return this;
    }

    public Sector getSector() {
        return sector;
    }

    public UserRegistrationServiceModel setSector(Sector sector) {
        this.sector = sector;
        return this;
    }
}
