package com.issueflow.repository;

import com.issueflow.modal.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT p FROM Project p JOIN p.projectMembers pm JOIN pm.member m WHERE m.id = :userId AND pm.projectRole = 'OWNER'")
    Set<Project> getAllProjects(@Param("userId") Long userId);

    boolean existsByName(String name);



}
