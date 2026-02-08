package com.issueflow.controller;

import com.issueflow.request.UserRoleRequest;
import com.issueflow.response.ApiResponse;
import com.issueflow.response.UserResponse;
import com.issueflow.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUser(){
        List<UserResponse> list = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/users/count")
    public ResponseEntity<Map<String, Object>> getUserCount(){
        Long cnt = userService.totalUsers();
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("totalUsers",cnt));
    }

    @GetMapping("/projects/{projectId}/members")
    public ResponseEntity<List<UserResponse>> getAllMembers(@PathVariable Long projectId){
        List<UserResponse> responses = userService.getAllProjectMembers(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/manager")
    public ResponseEntity<Map<String, Object>> getMyDetails(){
        Long projectId = userService.getProjectId();
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("projectId", projectId));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SYSTEM_ADMIN')")
    @PatchMapping("/users/{userId}/role")
    public ResponseEntity<ApiResponse> updateRole(@RequestBody @Valid UserRoleRequest roleRequest, @PathVariable Long userId){
        userService.updateUserRole(roleRequest, userId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Role updated successfully"));
    }

    @GetMapping("/users/profile")
    public ResponseEntity<UserResponse> getMyProfile(){
        UserResponse response = userService.getMyProfile();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SYSTEM_ADMIN')")
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("User deleted successfully"));
    }
}
