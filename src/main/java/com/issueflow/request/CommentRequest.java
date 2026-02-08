package com.issueflow.request;

import jakarta.validation.constraints.NotBlank;

public class CommentRequest {

    @NotBlank(message = "Comment text must not be blank")
    private String commentText;

    public CommentRequest( String commentText) {
        this.commentText = commentText;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText( String commentText) {
        this.commentText = commentText;
    }

}
