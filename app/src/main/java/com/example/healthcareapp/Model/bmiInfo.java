package com.example.healthcareapp.Model;


import java.text.DecimalFormat;

public class bmiInfo {
    public String userID;
    public String userName;
    public String age;
    public String sex;
    public String height;
    public String weight;
    public String goal;
    public String weeklyGoal;
    public String activityLevel;
    public long time;

    public bmiInfo(){};
    public double CalculatorBMR(){
        double bmi = 0;
        if (sex.equals("Male")){
            bmi = 10 * Integer.parseInt(weight) + 6.25 * Integer.parseInt(height) - 5 * Integer.parseInt(age) + 5;
        }
        else {
            bmi = 10 * Integer.parseInt(weight) + 6.25 * Integer.parseInt(height) - 5 * Integer.parseInt(age) - 161;
        }
        return bmi;
    }
    public float CalculatorBMI() {
        float _bmi = 0f;
        float h = Integer.parseInt(height)*Integer.parseInt(height)/10000f;
        if (h!=0) _bmi = Integer.parseInt(weight)/(h);
        return _bmi;
    }
    public String foxSayBMIEng() {
        String foxSay;
        float _bmi = CalculatorBMI();
        if (_bmi<=18.5f) foxSay = "You're in the underweight range";
        else if (_bmi<=24.9f) foxSay = "You're in the healthy weight range";
             else if (_bmi<=29.9f) foxSay = "You're in the overweight range";
                else foxSay = "You're in the obese range";
        return foxSay;
    }
    public String foxSayBMIVie() {
        String foxSay;
        float _bmi = CalculatorBMI();
        if (_bmi<=18.5f) foxSay = "Bạn đang ở mức gầy";
        else if (_bmi<=24.9f) foxSay = "Bạn đang ở mức bình thường";
        else if (_bmi<=29.9f) foxSay = "Bạn đang ở mức hơi quá cân";
        else foxSay = "Bạn đang ở mức béo phì";
        return foxSay;
    }
    public int CaloriesNeedToBurn(){
        int calories = 0;
        double bmi = CalculatorBMR();
        switch (activityLevel){
            case "Not very active":
                calories = (int) Math.round(bmi * 1.2);
                break;
            case "Lightly active":
                calories = (int) Math.round(bmi * 1.375);
                break;
            case "Moderate":
                calories = (int) Math.round(bmi * 1.55);
                break;
            case "Active":
                calories = (int) Math.round(bmi * 1.725);
                break;
            case "Very active":
                calories = (int) Math.round(bmi * 1.9);
                break;
        }
        switch (weeklyGoal){
            case "0.25":
                if (goal.equals("Lose weight")){
                    calories -= 250;
                }
                else {
                    calories += 250;
                }
                break;
            case "0.5":
                if (goal.equals("Lose weight")){
                    calories -= 500;
                }
                else {
                    calories += 500;
                }
                break;
            case "1":
                if (goal.equals("Lose weight")){
                    calories -= 1000;
                }
                else {
                    calories += 1000;
                }
                break;
        }
        return calories;
    }
}


