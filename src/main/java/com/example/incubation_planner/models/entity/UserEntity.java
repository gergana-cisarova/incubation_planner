package com.example.incubation_planner.models.entity;

import com.example.incubation_planner.models.entity.enums.Sector;
import com.example.incubation_planner.models.entity.enums.UserType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType;

    @Enumerated(EnumType.STRING)
    private Sector sector;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<UserRoleEntity> roles = new ArrayList<>();

    @OneToMany(mappedBy = "promoter", targetEntity = Project.class, cascade = CascadeType.ALL)
    private Set<Project> ownProjects = new HashSet<>();


    @ManyToMany(mappedBy = "collaborators", targetEntity = Project.class)
    private Set<Project> projects = new HashSet<>();

    public UserEntity() {
    }

    public String getUsername() {
        return username;
    }

    public UserEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserEntity setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserEntity setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserType getUserType() {
        return userType;
    }

    public UserEntity setUserType(UserType userType) {
        this.userType = userType;
        return this;
    }

    public Sector getSector() {
        return sector;
    }

    public UserEntity setSector(Sector sector) {
        this.sector = sector;
        return this;
    }

    public List<UserRoleEntity> getRoles() {
        return roles;
    }

    public UserEntity setRoles(List<UserRoleEntity> roles) {
        this.roles = roles;
        return this;
    }

    public Set<Project> getOwnProjects() {
        return ownProjects;
    }

    public UserEntity setOwnProjects(Set<Project> ownProjects) {
        this.ownProjects = ownProjects;
        return this;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public UserEntity setProjects(Set<Project> projects) {
        this.projects = projects;
        return this;
    }

    public UserEntity addRole(UserRoleEntity roleEntity) {
        this.roles.add(roleEntity);
        return this;
    }

    public void addProject(Project project) {
        this.projects.add(project);
    }
    public void removeProject(Project project) {
        this.projects.remove(project);
    }
}
