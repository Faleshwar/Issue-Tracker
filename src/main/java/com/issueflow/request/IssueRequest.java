package com.issueflow.request;

import jakarta.validation.constraints.NotBlank;

public class IssueRequest {

    @NotBlank(message = "Issue title must not be blank")
    private String title;

    private String description;

    @NotBlank(message = "Issue priority must not be blank")
    private String priority;

    public IssueRequest(String title, String description, String priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public IssueRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
