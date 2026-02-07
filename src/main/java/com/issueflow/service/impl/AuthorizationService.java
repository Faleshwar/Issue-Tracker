package com.issueflow.service.impl;

import com.issueflow.modal.Issue;
import com.issueflow.modal.Project;
import com.issueflow.modal.User;
import com.issueflow.modal.UserRole;
import com.issueflow.repository.ProjectMemberRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final ProjectMemberRepository projectMemberRepository;

    public AuthorizationService(ProjectMemberRepository projectMemberRepository) {
        this.projectMemberRepository = projectMemberRepository;
    }

    public boolean canViewProject(User user, Project project){
        if(user.getUserRole().equals(UserRole.ADMIN)){
            return true;
        }
        return projectMemberRepository.hasMember(project.getId(), user.getId());
    }

    public boolean canViewProject(User user){
        return isAdmin(user);
    }

    public boolean canCreateProject(User user){
        return isAdmin(user);
    }

    public boolean canUpdateProject(User user){
        return isAdmin(user);
    }

    public boolean canAddOrRemoveMembersOnProject(User user, Long projectId){
        return isAdmin(user) || (isManager(user) && projectMemberRepository.hasMember(projectId, user.getId()));
    }

    public boolean canDeleteProject(User user){
        return isAdmin(user);
    }



    public boolean canViewIssue(User user, Issue issue){
        if(isAdmin(user)){
            return true;
        }else if(isDeveloper(user) && issue.getAssignee() != null && issue.getAssignee().getId().equals(user.getId())){
            return true;
        }else if(isManager(user) && projectMemberRepository.hasMember(issue.getProject().getId(), user.getId())){
            return true;
        }
        return false;
    }

    public boolean canWriteIssue(User user, Long projectId){
        return isAdmin(user) || (isManager(user) && projectMemberRepository.hasMember(projectId, user.getId()));
    }

    public boolean canViewIssue(User user, Long projectId){
        return isAdmin(user)
                || (isManager(user)
                && projectMemberRepository.hasMember(projectId, user.getId()));
    }

    public boolean canViewIssue(User user){
        return isAdmin(user);
    }

    public boolean canUpdateIssueStatus(User user, Issue issue){
        return isDeveloper(user) && issue.getAssignee() != null && issue.getAssignee().getId().equals(user.getId());
    }



    private boolean isAdmin(User user){
        return user.getUserRole().equals(UserRole.ADMIN) || user.getUserRole().equals(UserRole.SYSTEM_ADMIN);
    }

    private boolean isManager(User user){
        return user.getUserRole().equals(UserRole.MANAGER);
    }

    private boolean isDeveloper(User user){
        return user.getUserRole().equals(UserRole.DEVELOPER);
    }

    public boolean canViewProjectMember(User currentUser, Long projectId) {
        return isAdmin(currentUser) || (isManager(currentUser) && projectMemberRepository.hasMember(projectId, currentUser.getId()));
    }

    public boolean canViewUsers(User currentUser) {
        return isAdmin(currentUser);
    }

    public boolean canAssignIssue(User user, Long projectId){
        return isAdmin(user) || (isManager(user) && projectMemberRepository.hasMember(projectId, user.getId()));
    }

    public boolean canUpdateRole(User user){
        return isAdmin(user);
    }

    public boolean canViewAssignedIssues(User user){
        return isDeveloper(user);
    }
}
