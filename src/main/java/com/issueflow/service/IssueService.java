package com.issueflow.service;

import com.issueflow.request.IssueRequest;
import com.issueflow.response.IssueOverview;
import com.issueflow.response.IssueResponse;

import java.util.List;

public interface IssueService {

    void createIssue(Long projectId, IssueRequest request);

    void updateIssue(Long projectId, Long issueId, IssueRequest request);

    void deleteIssue(Long projectId, Long issueId);

    IssueResponse getIssue(Long projectId, Long issueId);

    List<IssueResponse> getProjectAllissues(Long projectId);

    List<IssueResponse> getAllIssues();

    Long getTotalIssues();

    IssueOverview getOverviews(Long projectId);

    void assignIssue(Long projectId, Long issueId, Long userId);

    List<IssueResponse> getAssignedIssues();

    void changeIssueStatus(Long issueId, String status);

    List<IssueResponse> getIssues(String search,
                                  String status,
                                  String priority,
                                  String sortBy,
                                  String direction,
                                  int page,
                                  int size);
}
