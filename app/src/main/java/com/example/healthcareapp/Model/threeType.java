package com.example.healthcareapp.Model;

public class threeType {
    String idType, nameType, numberType, unitType;
    public threeType(String idType, String nameType, String numberType, String unitType) {
        this.idType = idType;
        this.nameType = nameType;
        this.numberType = numberType;
        this.unitType = unitType;
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
