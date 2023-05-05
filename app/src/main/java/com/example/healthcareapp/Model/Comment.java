package com.example.healthcareapp.Model;

public class Comment {
    private String id;

    private String postId;
    private String comment;
    private String userId;

    public Comment() {
    }

    public Comment(String id, String postId, String comment, String userId) {
        this.id = id;
        this.postId = postId;
        this.comment = comment;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
