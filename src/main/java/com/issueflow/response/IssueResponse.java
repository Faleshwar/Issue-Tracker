package com.issueflow.response;

import java.time.LocalDateTime;

public class IssueResponse {

    private Long id;

    private String title;

    private String description;

    private String priority;

    private LocalDateTime createdAt;

    private String issueStatus;

    private String reporterName;

    private String projectName;

    private String assignee;

    public IssueResponse(Long id, String title, String description, String priority, LocalDateTime createdAt, String issueStatus, String reporterName, String projectName, String assignee) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.createdAt = createdAt;
        this.issueStatus = issueStatus;
        this.reporterName = reporterName;
        this.projectName = projectName;
        this.assignee = assignee;
    }

    public IssueResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(String issueStatus) {
        this.issueStatus = issueStatus;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }
}
