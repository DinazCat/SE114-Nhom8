package com.example.healthcareapp.Model;

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

