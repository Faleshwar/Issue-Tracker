package com.issueflow.service.impl;

import com.issueflow.exception.ConflictException;
import com.issueflow.exception.ProjectNotFoundException;
import com.issueflow.modal.ProjectMember;
import com.issueflow.modal.User;
import com.issueflow.modal.UserRole;
import com.issueflow.repository.ProjectMemberRepository;
import com.issueflow.repository.UserRepository;
import com.issueflow.request.UserRoleRequest;
import com.issueflow.response.UserResponse;
import com.issueflow.security.SecurityUtil;
import com.issueflow.service.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final AuthorizationService authorizationService;

    public UserServiceImpl(UserRepository userRepository, ProjectMemberRepository projectMemberRepository, AuthorizationService authorizationService) {
        this.userRepository = userRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.authorizationService = authorizationService;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        User currentUser = getCurrentUser();
        if(!authorizationService.canViewUsers(currentUser)){
            throw new AccessDeniedException("Access denied");
        }
        List<User> users;
        if(currentUser.getUserRole().equals(UserRole.SYSTEM_ADMIN)){
            users = userRepository.findAll();
        }else{
            users = userRepository.findUsersExceptSysAdmin();
        }
        return users.stream().map(this::convertToUserResponse).toList();
    }

    @Override
    public long totalUsers() {
        if(!authorizationService.canViewUsers(getCurrentUser())){
            throw new AccessDeniedException("Access denied");
        }
        return userRepository.count();
    }

    @Override
    public List<UserResponse> getAllProjectMembers(Long projectId) {

        if(!authorizationService.canViewProjectMember(getCurrentUser(), projectId)){
            throw new AccessDeniedException("Access denied");
        }
        Set<User> users = userRepository.findProjectMembers(projectId);
        return users.stream().map(this::convertToUserResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateUserRole(UserRoleRequest roleRequest, Long userId) {
        User curentUser = getCurrentUser();
        if(!authorizationService.canUpdateRole(curentUser)){
            throw new AccessDeniedException("Access denied");
        }
        UserRole role = UserRole.valueOf(roleRequest.getUserRole());
        User user = userRepository.findById(userId).orElseThrow(()->new UsernameNotFoundException("User not found with id: "+userId));
        if(user.getId().equals(curentUser.getId())){
            throw new ConflictException("User cannot update own role");
        }
        user.setUserRole(role);
    }

    @Override
    public UserResponse getMyProfile() {
        User currentUser = getCurrentUser();
        return convertToUserResponse(currentUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User currentUser = getCurrentUser();
        if(userId.equals(currentUser.getId())){
           throw  new ConflictException("User cannot delete himself");
        }
        User user = userRepository.findById(userId).orElseThrow(()->new UsernameNotFoundException("User not found with id: "+userId));
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public Long getProjectId(){
        User currentUser = getCurrentUser();
       ProjectMember projectMember= projectMemberRepository.getProjectIdByMemberId(currentUser.getId()).orElseThrow(()->new ProjectNotFoundException("Project not found with member id: "+currentUser.getId()));
       Long id = projectMember.getProject().getId();
       return id;
    }

    private UserResponse convertToUserResponse(User user){
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getUserRole().name(), user.getActive());
    }

    private User getCurrentUser(){
        String email = SecurityUtil.getCurrentUserEmail();
        return userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found with email: "+email));
    }
}
