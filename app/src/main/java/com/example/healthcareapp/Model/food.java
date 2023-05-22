package com.example.healthcareapp.Model;

public class food {
    String imgFood, nameFood, caloriesFood, servingFood;

    public food(String imgFood, String nameFood, String caloriesFood, String servingFood) {
        this.imgFood = imgFood;
        this.nameFood = nameFood;
        this.caloriesFood = caloriesFood;
        this.servingFood = servingFood;
    }

    public String getImgFood() {
        return imgFood;
    }

    public void setImgFood(String imgFood) {
        this.imgFood = imgFood;
    }

    public String getNameFood() {
        return nameFood;
    }

    public void setNameFood(String nameFood) {
        this.nameFood = nameFood;
    }

    public String getCaloriesFood() {
        return caloriesFood;
    }

    public void setCaloriesFood(String caloriesFood) {
        this.caloriesFood = caloriesFood;
    }

    public String getServingFood() {
        return servingFood;
    }

    public void setServingFood(String servingFood) {
        this.servingFood = servingFood;
    }
}
