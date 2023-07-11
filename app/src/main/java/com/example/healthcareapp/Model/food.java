package com.example.healthcareapp.Model;

public class food {
    String idFood, nameFood, caloriesFood, servingFood, imgFood, cabsFood, fatFood, proteinFood;
    public food( ) {

    }
    public food(String imgFood, String idFood, String nameFood, String caloriesFood, String servingFood) {
        this.idFood = idFood;
        this.nameFood = nameFood;
        this.caloriesFood = caloriesFood;
        this.servingFood = servingFood;
        this.imgFood = imgFood;
    }

    public String getCabsFood() {
        return cabsFood;
    }

    public String getFatFood() {
        return fatFood;
    }

    public String getProteinFood() {
        return proteinFood;
    }

    public void setCabsFood(String cabsFood) {
        this.cabsFood = cabsFood;
    }

    public void setFatFood(String fatFood) {
        this.fatFood = fatFood;
    }

    public void setProteinFood(String proteinFood) {
        this.proteinFood = proteinFood;
    }

    public String getImgFood() {
        return imgFood;
    }

    public void setImgFood(String imgFood) {
        this.imgFood = imgFood;
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
