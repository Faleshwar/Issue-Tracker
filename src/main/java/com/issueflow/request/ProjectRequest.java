package com.issueflow.request;

import jakarta.validation.constraints.NotBlank;

public class ProjectRequest {

    @NotBlank(message = "Project name must not be blank")
    private String name;

    @NotBlank(message = "Project description must not be blank")
    private String description;

    public ProjectRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public ProjectRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
