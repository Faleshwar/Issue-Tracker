package com.issueflow.controller;

import com.issueflow.request.ProjectRequest;
import com.issueflow.response.ApiResponse;
import com.issueflow.response.ProjectResponse;
import com.issueflow.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }


    @PostMapping("/projects")
    public ResponseEntity<ApiResponse> createProject(@RequestBody ProjectRequest request){
        projectService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Project created successfully"));
    }

    @GetMapping("/projects/{projectId}")
    public ResponseEntity<ProjectResponse> projectDetails(@PathVariable Long projectId){
        ProjectResponse projectDetails = projectService.getProjectDetails(projectId);
        return ResponseEntity.ok(projectDetails);
    }

    @PutMapping("/projects/{projectId}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long projectId, @RequestBody ProjectRequest request){
        projectService.update(request, projectId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Project updated successfully"));
    }


    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long projectId){
        projectService.delete(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Project deleted successfully"));
    }

    @GetMapping("/projects")
    public ResponseEntity<List<ProjectResponse>> getAllProjects(){
        List<ProjectResponse> list = projectService.getAllProjects();
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }


    @GetMapping("/projects/count")
    public ResponseEntity<Map<String, Object>> getProjectsCount(){
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("totalProjects", projectService.totalProjects()));
    }

    @PatchMapping("/projects/{projectId}/{memberId}")
    public ResponseEntity<ApiResponse> addMembers(@PathVariable Long projectId, @PathVariable Long memberId){
        projectService.addMember(projectId, memberId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Member added successfully"));
    }

    @DeleteMapping("/projects/{projectId}/{memberId}")
    public ResponseEntity<ApiResponse> removeMembers(@PathVariable Long projectId, @PathVariable Long memberId){
        projectService.removeMember(projectId, memberId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Member removed successfully"));
    }


}
