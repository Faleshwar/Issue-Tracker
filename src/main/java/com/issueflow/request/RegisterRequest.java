package com.issueflow.request;

public class RegisterRequest {

    private String email;
    private String name;
    private String password;
    private String userRole;

    public RegisterRequest() {

    }

    public RegisterRequest(String password, String name, String email, String userRole) {
        this.password = password;
        this.name = name;
        this.email = email;
        this.userRole = userRole;
    }

    public String getEmail() {
        return email;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
