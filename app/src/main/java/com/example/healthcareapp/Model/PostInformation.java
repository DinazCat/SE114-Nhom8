package com.example.healthcareapp.Model;

import java.util.Comparator;
import java.util.List;

public class PostInformation {
    public String id;
   public String username;
   public String userid;
   public String postFoodName;
   public String postFoodRating;
    public String postFoodIngredient;
    public String postFoodMaking;
    public String postFoodSummary;
   public List<String> postimgs;
   public String userimg;
   public String posttime;
   public List<String> likes;
   public List<String> comments;
    public static Comparator<PostInformation> sortByLikesDescending = new Comparator<PostInformation>() {
        @Override
        public int compare(PostInformation post1, PostInformation post2) {
            return Integer.compare(post2.likes.size(), post1.likes.size());
        }
    };

//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getUserid() {
//        return userid;
//    }
//
//    public void setUserid(String userid) {
//        this.userid = userid;
//    }
//
//
//    public List<String> getPostimgs() {
//        return postimgs;
//    }
//
//    public void setPostimgs(List<String> postimgs) {
//        this.postimgs = postimgs;
//    }
//
//    public int getLikes() {
//        return likes;
//    }
//
//    public void setLikes(int likes) {
//        this.likes = likes;
//    }
//
//    public int getComments() {
//        return comments;
//    }
//
//    public void setComments(int comments) {
//        this.comments = comments;
//    }
//
//    public String getUserimg() {
//        return userimg;
//    }
//
//    public void setUserimg(String userimg) {
//        this.userimg = userimg;
//    }
//
//    public String getPosttime() {
//        return posttime;
//    }
//
//    public void setPosttime(String posttime) {
//        this.posttime = posttime;
//    }
}
