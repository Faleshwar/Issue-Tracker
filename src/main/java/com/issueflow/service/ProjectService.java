package com.issueflow.service;

import com.issueflow.request.ProjectRequest;
import com.issueflow.response.ProjectResponse;

import java.util.List;

public interface ProjectService {

    void create(ProjectRequest projectRequest);

    void update(ProjectRequest projectRequest, Long projectId);

    void delete(Long projectId);

    ProjectResponse getProjectDetails(Long projectId);

    List<ProjectResponse> getAllProjects();

    void addMember(Long projectId, Long memberId);

    void removeMember(Long projectId, Long memberId);

    Long totalProjects();
}
