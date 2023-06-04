package com.example.healthcareapp.Model;

public class food {
    String idFood, nameFood, caloriesFood, servingFood;
    public food( ) {

    }
    public food(String imgFood, String nameFood, String caloriesFood, String servingFood) {
        this.idFood = imgFood;
        this.nameFood = nameFood;
        this.caloriesFood = caloriesFood;
        this.servingFood = servingFood;
    }

    public String getIdFood() {
        return idFood;
    }

    public void setIdFood(String imgFood) {
        this.idFood = imgFood;
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
