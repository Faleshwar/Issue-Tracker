package com.issueflow.service;

import com.issueflow.request.UserRoleRequest;
import com.issueflow.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();

    long totalUsers();

    Long getProjectId();

    List<UserResponse> getAllProjectMembers(Long projectId);

    void updateUserRole(UserRoleRequest roleRequest, Long userId);

    UserResponse getMyProfile();

    void deleteUser(Long userId);
}
