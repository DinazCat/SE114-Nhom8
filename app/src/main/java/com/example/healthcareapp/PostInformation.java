package com.example.healthcareapp;

import java.util.List;

public class PostInformation {
   public String username;
   public String userid;
   public String post;
   public List<String> postimgs;
  // public String postimgs;
   public int likes;
   public int comments;
   public String userimg;
   public String posttime;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public List<String> getPostimgs() {
        return postimgs;
    }

    public void setPostimgs(List<String> postimgs) {
        this.postimgs = postimgs;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getUserimg() {
        return userimg;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }

    public String getPosttime() {
        return posttime;
    }

    public void setPosttime(String posttime) {
        this.posttime = posttime;
    }
}
