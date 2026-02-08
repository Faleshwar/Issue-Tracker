package com.issueflow.request;

import jakarta.validation.constraints.NotBlank;

public class IssueChangeRequest {

    @NotBlank(message = "Issue status must not be blank")
    private String issueStatus;

    public IssueChangeRequest(String issueStatus) {
        this.issueStatus = issueStatus;
    }

    public IssueChangeRequest() {
    }

    public String getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(String issueStatus) {
        this.issueStatus = issueStatus;
    }
}
