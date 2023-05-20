package com.project.pairprog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "The project needs a name")
    private String name;

    private Integer project_token;

    @ManyToOne
    User owner;

    @ManyToMany
    List<User> collaborators;
    public Project(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Project() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<User> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<User> collaborators) {
        this.collaborators = collaborators;
    }

    public Integer getProject_token() {
        return project_token;
    }

    public void setProject_token(Integer code) {
        this.project_token = code;
    }
}




