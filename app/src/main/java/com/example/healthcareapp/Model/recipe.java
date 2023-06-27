package com.example.healthcareapp.Model;

public class recipe {
    String idRecipe, nameRecipe, Prep, Cooking;
    String calorieRecipe;

    public void setCooking(String cooking) {
        Cooking = cooking;
    }

    public void setPrep(String prep) {
        Prep = prep;
    }

    public String getCooking() {
        return Cooking;
    }

    public String getPrep() {
        return Prep;
    }

    public void setIdRecipe(String idRecipe) {
        this.idRecipe = idRecipe;
    }

    public String getIdRecipe() {
        return idRecipe;
    }

    public void setCalorieRecipe(String calorieRecipe) {
        this.calorieRecipe = calorieRecipe;
    }

    public void setNameRecipe(String nameRecipe) {
        this.nameRecipe = nameRecipe;
    }

    public String getCalorieRecipe() {
        return calorieRecipe;
    }

    public String getNameRecipe() {
        return nameRecipe;
    }
}
