package com.issueflow.request;

public class UserRoleRequest {

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
