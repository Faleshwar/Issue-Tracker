package com.issueflow.service.impl;

import com.issueflow.exception.IssueNotFoundException;
import com.issueflow.modal.Comment;
import com.issueflow.modal.Issue;
import com.issueflow.modal.User;
import com.issueflow.repository.CommentRepository;
import com.issueflow.repository.IssueRepository;
import com.issueflow.repository.UserRepository;
import com.issueflow.request.CommentRequest;
import com.issueflow.response.CommentResponse;
import com.issueflow.security.SecurityUtil;
import com.issueflow.service.CommentService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final IssueRepository issueRepository;

    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, IssueRepository issueRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.issueRepository = issueRepository;
    }

    @Override
    public CommentResponse createComment(CommentRequest commentRequest, Long issueId) {
        User commenter = getCurrentUser();
        Issue issue = issueRepository.findById(issueId).orElseThrow(()->new IssueNotFoundException("Issue not found with id "+issueId));
        Comment comment = new Comment(commenter, issue, commentRequest.getCommentText());
        Comment savedComment = commentRepository.save(comment);
        return convertToCommentResponse(savedComment);
    }

    @Override
    public List<CommentResponse> getAllComments(Long issueId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(()->new IssueNotFoundException("Issue not found with id: "+issueId));
        List<Comment> comments = commentRepository.findCommentsByIssueId(issue.getId());
        return comments.stream().map(this::convertToCommentResponse).toList();
    }

    private CommentResponse convertToCommentResponse(Comment comment){
        return new CommentResponse(comment.getId(), comment.getText(), comment.getCreatedAt(), comment.getUser().getId(), comment.getUser().getName());
    }

    private User getCurrentUser(){
        String email = SecurityUtil.getCurrentUserEmail();
        return userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found with email: "+email));
    }
}
