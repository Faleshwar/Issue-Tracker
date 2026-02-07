package com.issueflow.repository;

import com.issueflow.modal.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c JOIN c.issue s WHERE s.id = :issueId ORDER BY c.createdAt")
    List<Comment> findCommentsByIssueId(@Param("issueId") Long issueId);
}
