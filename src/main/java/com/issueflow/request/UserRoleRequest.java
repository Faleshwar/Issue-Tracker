package com.issueflow.request;

import jakarta.validation.constraints.NotBlank;

public class UserRoleRequest {

    @NotBlank(message = "User role must not be blank")
    private String userRole;

    public UserRoleRequest(String userRole) {
        this.userRole = userRole;
    }

    public UserRoleRequest() {
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
