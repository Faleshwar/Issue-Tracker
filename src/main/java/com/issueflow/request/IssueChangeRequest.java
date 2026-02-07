package com.issueflow.request;

public class IssueChangeRequest {

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
