package com.issueflow.repository;

import com.issueflow.modal.ProjectMember;
import com.issueflow.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    @Query("SELECT pm FROM ProjectMember pm JOIN pm.member m JOIN pm.project p WHERE p.id = :projectId AND m.id = :userId AND pm.projectRole = 'OWNER'")
    Optional<ProjectMember> findByProjectAndUserId(@Param("projectId") Long projectId, @Param("userId") Long userId);

    @Query("SELECT pm FROM ProjectMember pm JOIN pm.member m JOIN pm.project p WHERE p.id =:projectId AND m.id=:userId")
    Optional<ProjectMember> findByProjectByMember(@Param("projectId") Long projectId, @Param("userId") Long userId);

    @Query("SELECT CASE WHEN EXISTS (SELECT pm FROM ProjectMember pm LEFT JOIN pm.member m JOIN pm.project p WHERE p.id =:projectId AND m.id =:userId) THEN true ELSE false END")
    boolean hasMember(@Param("projectId") Long projectId,@Param("userId") Long userId);


    @Query("SELECT pm FROM ProjectMember pm JOIN pm.project p JOIN pm.member m WHERE m.id = :memberId AND m.userRole='MANAGER'")
    Optional<ProjectMember> getProjectIdByMemberId(@Param("memberId") Long memberId);

}
