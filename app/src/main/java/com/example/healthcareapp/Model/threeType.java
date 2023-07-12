package com.example.healthcareapp.Model;

public class threeType {
    boolean isFood=false;

    public void setFoodTrue() {
        isFood = true;
    }
    public boolean getIsFood(){
        return isFood;
    }

    String idType, nameType, numberType, unitType;
    String cabs, fat, protein;
    public threeType(String idType, String nameType, String numberType, String unitType) {
        this.idType = idType;
        this.nameType = nameType;
        this.numberType = numberType;
        this.unitType = unitType;
    }
    public threeType(String idType, String nameType, String numberType, String unitType,String cabs, String fat, String protein) {
        this.idType = idType;
        this.nameType = nameType;
        this.numberType = numberType;
        this.unitType = unitType;
        this.cabs = cabs;
        this.protein = protein;
        this.fat = fat;
    }

    public String getCabs() {
        return cabs;
    }

    public String getFat() {
        return fat;
    }

    public String getProtein() {
        return protein;
    }

    public void setCabs(String cabs) {
        this.cabs = cabs;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getNameType() {
        return nameType;
    }

    public String getNumberType() {
        return numberType;
    }

    public String getIdType() {
        return idType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public void setNumberType(String numberType) {
        this.numberType = numberType;
    }
}
