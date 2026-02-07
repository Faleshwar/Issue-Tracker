package com.issueflow.request;

public class CommentRequest {

    private String commentText;

    public CommentRequest( String commentText) {
        this.commentText = commentText;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }


}
