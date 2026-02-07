package com.issueflow.service.impl;

import com.issueflow.exception.IssueNotFoundException;
import com.issueflow.exception.ProjectNotFoundException;
import com.issueflow.modal.*;
import com.issueflow.repository.IssueRepository;
import com.issueflow.repository.ProjectMemberRepository;
import com.issueflow.repository.ProjectRepository;
import com.issueflow.repository.UserRepository;
import com.issueflow.request.IssueRequest;
import com.issueflow.response.IssueOverview;
import com.issueflow.response.IssueResponse;
import com.issueflow.security.SecurityUtil;
import com.issueflow.service.IssueService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final AuthorizationService authorizationService;

    public IssueServiceImpl(IssueRepository issueRepository, UserRepository userRepository, ProjectMemberRepository projectMemberRepository, ProjectRepository projectRepository, AuthorizationService authorizationService) {
        this.issueRepository = issueRepository;
        this.userRepository = userRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.projectRepository = projectRepository;
        this.authorizationService = authorizationService;
    }

    @Override
    @Transactional
    public void createIssue(Long projectId, IssueRequest request) {
        User reporter = getCurrentUser();
        Project project = projectRepository.findById(projectId).orElseThrow(()->new ProjectNotFoundException("Project not found with id: "+projectId));
        Issue issue = new Issue(reporter, project, IssueStatus.TO_DO, Priority.valueOf(request.getPriority()),request.getDescription(), request.getTitle());
        issueRepository.save(issue);
    }

    @Override
    @Transactional
    public void updateIssue(Long projectId, Long issueId, IssueRequest request) {
        User currentUser = getCurrentUser();
        if(!authorizationService.canWriteIssue(currentUser, projectId)){
            throw new AccessDeniedException("Access denied");
        }
        Issue existingIssue = issueRepository.findIssueByProjectId(projectId, issueId).orElseThrow(()->new IssueNotFoundException("Issue not found with id: "+issueId+" and project id: "+projectId));
        existingIssue.setTitle(request.getTitle());
        existingIssue.setDescription(request.getDescription());
        existingIssue.setPriority(Priority.valueOf(request.getPriority()));
    }


    @Override
    @Transactional
    public void deleteIssue(Long projectId, Long issueId) {
        User user = getCurrentUser();
        if(!authorizationService.canWriteIssue(user, projectId)){
            throw new AccessDeniedException("Access denied");
        }
        Issue existingIssue = issueRepository.findIssueByProjectId(projectId, issueId).orElseThrow(()->new IssueNotFoundException("Issue not found with id: "+issueId+" and project id: "+projectId));
        issueRepository.delete(existingIssue);
    }



    @Override
    public IssueResponse getIssue(Long projectId, Long issueId) {
        User currentUser = getCurrentUser();
        Issue issue = issueRepository.findIssueByProjectId(projectId, issueId).orElseThrow(()->new IssueNotFoundException("Issue not found with issue id: "+issueId+" and project id: "+projectId));
        if(!authorizationService.canViewIssue(currentUser, issue)){
            throw new AccessDeniedException("Access Denied");
        }
        return convertToIssueResponse(issue);
    }


    @Override
    public List<IssueResponse> getProjectAllissues(Long projectId) {
        User currentUser = getCurrentUser();
        if(!authorizationService.canViewIssue(currentUser, projectId)){
            throw new AccessDeniedException("Access denied");
        }
        projectRepository.findById(projectId).orElseThrow(()->new ProjectNotFoundException("Project not found id: "+projectId));
        Set<Issue> issues = issueRepository.findAllIssuesByProjectId(projectId);
        return issues.stream().map(this::convertToIssueResponse).collect(Collectors.toList());
    }


    @Override
    public List<IssueResponse> getAllIssues() {
        if(!authorizationService.canViewIssue(getCurrentUser())){
            throw new AccessDeniedException("Access denied");
        }
        List<Issue> issues = issueRepository.findAll();
        return issues.stream().map(this::convertToIssueResponse).collect(Collectors.toList());
    }

    @Override
    public Long getTotalIssues() {
        return issueRepository.count();
    }

    @Override
    public IssueOverview getOverviews(Long projectId) {
        Long totalIssues = issueRepository.countByProjectId(projectId);
        Long solvedIssues = issueRepository.countSolvedIssueByProjectId(projectId);
        Project project = projectRepository.findById(projectId).orElseThrow(()->new ProjectNotFoundException("Project not found with id: "+projectId));
        Long totalProjectMembers = (long)project.getProjectMembers().size();
        return new IssueOverview(totalIssues, solvedIssues, totalProjectMembers);
    }

    @Override
    public void assignIssue(Long projectId, Long issueId, Long userId) {
        if(!authorizationService.canAssignIssue(getCurrentUser(), projectId)){
            throw new AccessDeniedException("Access denied");
        }
        Issue issue = issueRepository.findIssueByProjectId(projectId, issueId).orElseThrow(()->new IssueNotFoundException("Issue not found with project id: "+issueId+" and issue id: "+issueId));
        if(issue.getAssignee() != null){
            throw new IssueNotFoundException("Issue is already assigned");
        }
        User user = userRepository.findById(userId).orElseThrow(()->new UsernameNotFoundException("User not found with id: "+userId));
        issue.setAssignee(user);
        issueRepository.save(issue);
    }

    @Override
    public List<IssueResponse> getAssignedIssues() {
        if(!authorizationService.canViewAssignedIssues(getCurrentUser())){
            throw new AccessDeniedException("Access denied");
        }
        Set<Issue> issues = issueRepository.findIssuesByAssigneeId(getCurrentUser().getId());
        return issues.stream().map(this::convertToIssueResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void changeIssueStatus(Long issueId, String status) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new IssueNotFoundException("Issue not found with issue id: " + issueId));
        IssueStatus issueStatus = IssueStatus.valueOf(status);
        issue.setIssueStatus(issueStatus);
    }

    @Override
    public List<IssueResponse> getIssues(String search, String status, String priority, String sortBy, String direction, int page, int size) {
        Sort sort = direction.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(page, size, sort);
        IssueStatus issueStatus = IssueStatus.valueOf(status);
        Priority issuePriority = Priority.valueOf(priority);

        Page<Issue> issues = issueRepository.findIssues(search, issueStatus, issuePriority, pageable);

        return List.of();
    }


    private IssueResponse convertToIssueResponse(Issue issue){
        return new IssueResponse(issue.getId(), issue.getTitle(),
                issue.getDescription(), issue.getPriority().name(),
                issue.getCreatedAt(), issue.getIssueStatus().name(),
                issue.getReporter().getName(), issue.getProject().getName(),
                issue.getAssignee()==null?null:issue.getAssignee().getName());
    }

    private User getCurrentUser(){
        String email = SecurityUtil.getCurrentUserEmail();
        return userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found with email: "+email));
    }
}
