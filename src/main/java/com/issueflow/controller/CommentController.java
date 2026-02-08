package com.issueflow.controller;

import com.issueflow.request.CommentRequest;
import com.issueflow.response.CommentResponse;
import com.issueflow.service.CommentService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects/issues")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{issueId}/comments")
    public ResponseEntity<CommentResponse> createComment(@PathVariable Long issueId, @RequestBody @Valid CommentRequest commentRequest){
        CommentResponse comment = commentService.createComment(commentRequest, issueId);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @GetMapping("/{issueId}/comments")
    public ResponseEntity<List<CommentResponse>> getAllComments(@PathVariable Long issueId){
        List<CommentResponse> responses = commentService.getAllComments(issueId);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}
