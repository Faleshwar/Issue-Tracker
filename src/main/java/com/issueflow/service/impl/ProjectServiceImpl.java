package com.issueflow.service.impl;

import com.issueflow.exception.ProjectAlreadyExistException;
import com.issueflow.exception.ProjectMemberException;
import com.issueflow.exception.ProjectNotFoundException;
import com.issueflow.modal.Project;
import com.issueflow.modal.ProjectMember;
import com.issueflow.modal.User;
import com.issueflow.repository.ProjectMemberRepository;
import com.issueflow.repository.ProjectRepository;
import com.issueflow.repository.UserRepository;
import com.issueflow.request.ProjectRequest;
import com.issueflow.response.ProjectResponse;
import com.issueflow.security.SecurityUtil;
import com.issueflow.service.ProjectService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository memberRepository;
    private final AuthorizationService authorizationService;

    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository, ProjectMemberRepository memberRepository, AuthorizationService authorizationService) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.authorizationService = authorizationService;
    }


    @Override
    @Transactional
    public void create(ProjectRequest projectRequest) {
        if(projectRepository.existsByName(projectRequest.getName())){
            throw new ProjectAlreadyExistException("Project is already exist");
        }
        Project project = new Project("ACTIVE", projectRequest.getDescription(), projectRequest.getName());
        String email = SecurityUtil.getCurrentUserEmail();
        User currentUser = userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found with email: "+email));
        if(!authorizationService.canCreateProject(currentUser)){
            throw new AccessDeniedException("Access denied");
        }
        project.getProjectMembers().add(new ProjectMember(currentUser, project, "OWNER"));
        projectRepository.save(project);
    }


    @Override
    @Transactional
    public void update(ProjectRequest projectRequest, Long projectId) {
        User currentUser = getCurrentUser();
        if(!authorizationService.canUpdateProject(currentUser)){
            throw new AccessDeniedException("Access denied");
        }
        ProjectMember projectMember = memberRepository.findByProjectAndUserId(projectId, currentUser.getId()).orElseThrow(()->new ProjectMemberException("Project Member not found with userId: "+currentUser.getId()+" and projectId: "+projectId));
        Project existingProject = projectRepository.findById(projectMember.getProject().getId()).orElseThrow(()->new ProjectNotFoundException("Project not found with id "+projectId));
        existingProject.setName(projectRequest.getName());
        existingProject.setDescription(projectRequest.getDescription());
    }

    @Override
    @Transactional
    public void delete(Long projectId) {
        User currentUser = getCurrentUser();
        if(!authorizationService.canDeleteProject(currentUser)){
            throw new AccessDeniedException("Access denied");
        }
        ProjectMember projectMember = memberRepository.findByProjectAndUserId(projectId, currentUser.getId()).orElseThrow(()->new ProjectMemberException("ProjectMember not found with userId: "+currentUser.getId()+" and projectId: "+projectId));
        Project existingProject = projectRepository.findById(projectMember.getProject().getId()).orElseThrow(()->new ProjectNotFoundException("Project not found with id: "+projectId));
        projectRepository.delete(existingProject);
    }

    @Override
    public ProjectResponse getProjectDetails(Long projectId) {
        User currentUser = getCurrentUser();
        Project existingProject = projectRepository.findById(projectId).orElseThrow(()->new ProjectNotFoundException("Project not found with id: "+projectId));
        Set<ProjectMember> projectMembers = existingProject.getProjectMembers();
        String ownerName = null;
        for(ProjectMember pm: projectMembers){
            if(pm.getProjectRole().equals("OWNER")){
                ownerName = pm.getMember().getName();
            }
        }
        if(!authorizationService.canViewProject(currentUser, existingProject)){
            throw new AccessDeniedException("Access denied");
        }
        return new ProjectResponse(existingProject.getId(), existingProject.getName(), existingProject.getDescription(), existingProject.getStatus(), existingProject.getCreatedAt(), ownerName);
    }

    @Override
    public List<ProjectResponse> getAllProjects() {
        User currentUser = getCurrentUser();
        if(!authorizationService.canViewProject(currentUser)){
            throw new AccessDeniedException("Access Denied");
        }
        List<Project> projects = projectRepository.findAll();
        return projects.stream().map(this::convertToProjectResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addMember(Long projectId, Long memberId) {
        User currentUser = getCurrentUser();
        if(!authorizationService.canAddOrRemoveMembersOnProject(currentUser, projectId)){
            throw new AccessDeniedException("Access denied");
        }

        Project project = projectRepository.findById(projectId).orElseThrow(()->new ProjectNotFoundException("Project not found with id: "+projectId));
        User member = userRepository.findById(memberId).orElseThrow(()->new UsernameNotFoundException("User not found with member id: "+memberId));
        ProjectMember newMember = new ProjectMember(member, project, "MEMBER");
        if(project.getProjectMembers().contains(newMember)){
            throw new ProjectMemberException("User is already project member");
        }
        member.getProjectMembers().add(new ProjectMember(member, project, "MEMBER"));
    }

    @Override
    @Transactional
    public void removeMember(Long projectId, Long memberId) {
        User currentUser = getCurrentUser();
        if(!authorizationService.canAddOrRemoveMembersOnProject(currentUser, projectId)){
            throw new AccessDeniedException("Access denied");
        }
        Project project = projectRepository.findById(projectId).orElseThrow(()->new ProjectNotFoundException("Project not found with id: "+projectId));
        User member = userRepository.findById(memberId).orElseThrow(()->new UsernameNotFoundException("User not found with member id: "+memberId));
        ProjectMember projetMember=new ProjectMember(member, project, "MEMBER");
        if(!project.getProjectMembers().contains(projetMember)){
            throw new ProjectMemberException("User is not a project member");
        }
        member.getProjectMembers().remove(projetMember);
    }

    @Override
    public Long totalProjects() {
        return projectRepository.count();
    }

    private ProjectResponse convertToProjectResponse(Project project){
        return new ProjectResponse(project.getId(), project.getName(), project.getDescription(), project.getStatus(), project.getCreatedAt(), getCurrentUser().getName());
    }

    public User getCurrentUser(){
        String email = SecurityUtil.getCurrentUserEmail();
        return userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found with email: "+email));
    }
}
