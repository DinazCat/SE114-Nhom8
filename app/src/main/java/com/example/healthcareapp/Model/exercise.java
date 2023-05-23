package com.example.healthcareapp.Model;

public class exercise {

    String nameExercise, caloriesBurnedAMin;
    String minutePerformed;

    public exercise(String nameExercise, String minute, String caloriesBurnedAMin) {
        this.nameExercise = nameExercise;
        this.minutePerformed = minute;
        this.caloriesBurnedAMin = caloriesBurnedAMin;
    }

    public void setCaloriesBurnedAMin(String caloriesBurnedAMin) {
        this.caloriesBurnedAMin = caloriesBurnedAMin;
    }

    public void setNameExercise(String nameExercise) {
        this.nameExercise = nameExercise;
    }

    public void setMinutePerformed(String minutePerformed) {
        this.minutePerformed = minutePerformed;
    }

    public String getMinutePerformed() {
        return minutePerformed;
    }

    public String getCaloriesBurnedAMin() {
        return caloriesBurnedAMin;
    }

    public String getNameExercise() {
        return nameExercise;
    }
}
