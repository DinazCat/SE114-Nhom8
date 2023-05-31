package com.example.healthcareapp.Model;

public class hashtag {
    private String name;
    private boolean tick = false;
    public hashtag(String name, boolean tick){
        this.name = name;
        this.tick = tick;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public boolean getTick() {
        return tick;
    }

    public void setTick(boolean t) {
        this.tick = t;
    }
}
