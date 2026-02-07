package com.issueflow.service;

import com.issueflow.request.CommentRequest;
import com.issueflow.response.CommentResponse;

import java.util.List;

public interface CommentService {
    CommentResponse createComment(CommentRequest commentRequest, Long issueId);

    List<CommentResponse> getAllComments(Long issueId);
}
