package com.example.healthcareapp.Model;

public class User {
    private String id;
    private String name;
    private String email;
    private String img;
    private String about;
    private String phone;
    private String city;
    private String country;

    private String token;

    public User(){

    }

    public User(String id, String name, String email, String img, String about, String phone, String city, String country, String token) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.img = img;
        this.about = about;
        this.phone = phone;
        this.city = city;
        this.country = country;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
