package com.issueflow.controller;
import com.issueflow.request.IssueChangeRequest;
import com.issueflow.request.IssueRequest;
import com.issueflow.response.ApiResponse;
import com.issueflow.response.IssueOverview;
import com.issueflow.response.IssueResponse;
import com.issueflow.service.IssueService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/projects")
public class IssueController {

    private final IssueService issueService;



    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @PostMapping("/{projectId}/issues")
    public ResponseEntity<ApiResponse> createIssue(@RequestBody @Valid IssueRequest request, @PathVariable Long projectId){
        issueService.createIssue(projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Issue created successfully"));
    }

    @PutMapping("/{projectId}/issues/{issueId}")
    public ResponseEntity<ApiResponse> updateIssue(@RequestBody @Valid IssueRequest request, @PathVariable Long projectId, @PathVariable Long issueId){
        issueService.updateIssue(projectId, issueId, request);
        return ResponseEntity.ok(new ApiResponse("Issue updated successfully"));
    }

    @DeleteMapping("/{projectId}/issues/{issueId}")
    public ResponseEntity<ApiResponse> deleteIssue(@PathVariable Long projectId, @PathVariable Long issueId){
        issueService.deleteIssue(projectId, issueId);
        return ResponseEntity.ok(new ApiResponse("Issue deleted successfully"));
    }

    @GetMapping("/{projectId}/issues/{issueId}")
    public ResponseEntity<IssueResponse> getIssueDetails(@PathVariable Long projectId, @PathVariable Long issueId){
        IssueResponse response = issueService.getIssue(projectId, issueId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{projectId}/issues")
    public ResponseEntity<List<IssueResponse>> getIssueDetails(@PathVariable Long projectId){
        List<IssueResponse> response = issueService.getProjectAllissues(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/issues/count")
    public ResponseEntity<Map<String, Object>> getTotalIssues(){
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("totalIssues", issueService.getTotalIssues()));
    }

    @GetMapping("/{projectId}/issues/overview")
    public ResponseEntity<IssueOverview> getOverview(@PathVariable("projectId") Long projectId){
        IssueOverview overviews = issueService.getOverviews(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(overviews);
    }

    @PatchMapping("/{projectId}/issues/{issueId}/user/{userId}")
    public ResponseEntity<ApiResponse> assignIssue(@PathVariable("projectId") Long projectId,@PathVariable("issueId") Long issueId, @PathVariable Long userId){
        issueService.assignIssue(projectId, issueId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Issue assigned successfully"));
    }

    @GetMapping("/issues/assigned")
    public ResponseEntity<List<IssueResponse>> getAssignedIssues(){
        List<IssueResponse> responses = issueService.getAssignedIssues();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @PatchMapping("/issues/{issueId}/status")
    public ResponseEntity<ApiResponse> changeIssueStatus(@PathVariable Long issueId, @RequestBody @Valid IssueChangeRequest issueChangeRequest) {
        issueService.changeIssueStatus(issueId, issueChangeRequest.getIssueStatus());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Issue status updated successfully"));
    }

    @GetMapping("/issues/sf")
    public ResponseEntity<?> getIssuesBySearchAndFilter(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String issueStatus,
            @RequestParam(required = false) String priority,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
