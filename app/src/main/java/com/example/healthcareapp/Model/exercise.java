package com.example.healthcareapp.Model;

public class exercise {
    String idExercise, imgExercise;
    String nameExercise, caloriesBurned;
    String minutePerformed;
    public exercise(){

    }
    public exercise(String imgExercise, String idExercise, String nameExercise, String minute, String caloriesBurnedAMin) {
        this.idExercise = idExercise;
        this.nameExercise = nameExercise;
        this.minutePerformed = minute;
        this.caloriesBurned = caloriesBurnedAMin;
        this.imgExercise= imgExercise;
    }

    public void setCaloriesBurnedAMin(String caloriesBurnedAMin) {
        this.caloriesBurned = caloriesBurnedAMin;
    }

    public void setNameExercise(String nameExercise) {
        this.nameExercise = nameExercise;
    }

    public void setMinutePerformed(String minutePerformed) {
        this.minutePerformed = minutePerformed;
    }

    public String getImgExercise() {
        return imgExercise;
    }

    public String getMinutePerformed() {
        return minutePerformed;
    }

    public String getCaloriesBurnedAMin() {
        return caloriesBurned;
    }

    public String getNameExercise() {
        return nameExercise;
    }

    public String getIdExercise() {
        return idExercise;
    }

    public void setIdExercise(String idExercise) {
        this.idExercise = idExercise;
    }
}
