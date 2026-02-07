package com.issueflow.modal;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "project_members", uniqueConstraints =
@UniqueConstraint(columnNames = {"member_id", "project_id"}))
public class ProjectMember{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private User member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    private String projectRole;

    public User getMember() {
        return member;
    }

    public void setMember(User member) {
        this.member = member;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getProjectRole() {
        return projectRole;
    }

    public void setProjectRole(String projectRole) {
        this.projectRole = projectRole;
    }

    public ProjectMember(User member, Project project, String projectRole) {
        this.member = member;
        this.project = project;
        this.projectRole = projectRole;
    }

    public ProjectMember() {
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ProjectMember that)) return false;
        return Objects.equals(member, that.member) && Objects.equals(project, that.project);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, project);
    }
}