package com.issueflow.response;

public class IssueOverview {
    private Long totalIssues;
    private Long solvedIssues;
    private Long totalMembers;

    public IssueOverview() {
    }

    public IssueOverview(Long totalIssues, Long solvedIssues, Long totalMembers) {
        this.totalIssues = totalIssues;
        this.solvedIssues = solvedIssues;
        this.totalMembers = totalMembers;
    }

    public Long getTotalIssues() {
        return totalIssues;
    }

    public void setTotalIssues(Long totalIssues) {
        this.totalIssues = totalIssues;
    }

    public Long getSolvedIssues() {
        return solvedIssues;
    }

    public void setSolvedIssues(Long solvedIssues) {
        this.solvedIssues = solvedIssues;
    }

    public Long getTotalMembers() {
        return totalMembers;
    }

    public void setTotalMembers(Long totalMembers) {
        this.totalMembers = totalMembers;
    }
}
