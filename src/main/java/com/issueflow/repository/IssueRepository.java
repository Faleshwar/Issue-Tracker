package com.issueflow.repository;

import com.issueflow.modal.Issue;
import com.issueflow.modal.IssueStatus;
import com.issueflow.modal.Priority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    @Query("SELECT s FROM Issue s JOIN FETCH s.project p WHERE s.id = :issueId AND p.id = :projectId")
    Optional<Issue> findIssueByProjectId(@Param("projectId") Long projectId, @Param("issueId") Long issueId);

    @Query("SELECT s FROM Issue s JOIN FETCH s.project p WHERE p.id = :projectId")
    Set<Issue> findAllIssuesByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT COUNT(DISTINCT s) FROM Issue s JOIN s.project p WHERE p.id = :projectId")
    Long countByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT COUNT(DISTINCT s) FROM Issue s JOIN s.project p WHERE p.id = :projectId AND s.issueStatus = 'DONE'")
    Long countSolvedIssueByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT s FROM Issue s JOIN s.assignee as WHERE as.id = :userId")
    Set<Issue> findIssuesByAssigneeId(@Param("userId") Long assigneeId);


    @Query("""
            SELECT i FROM Issue i WHERE (:search IS NULL OR LOWER(i.title) LIKE LOWER(CONCAT('%', :search, '%')) OR
            LOWER(i.description) LIKE LOWER(CONCAT('%', :search, '%')))
            AND (:status IS NULL OR i.issueStatus=:status)
            AND (:priority IS NULL OR i.priority =:priority)
            """)
    Page<Issue> findIssues(@Param("search") String search, @Param("status")IssueStatus status, @Param("priority")Priority priority, Pageable pageable);
}
