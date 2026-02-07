package com.issueflow.response;

public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private String userRole;

    private Boolean isActive;

    public UserResponse(Long id, String name, String email, String userRole, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.userRole = userRole;
        this.isActive = isActive;
    }

    public UserResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
