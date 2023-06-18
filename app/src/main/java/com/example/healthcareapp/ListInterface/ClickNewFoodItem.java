package com.example.healthcareapp.ListInterface;

import com.example.healthcareapp.Model.food;
import com.example.healthcareapp.Model.threeType;

public interface ClickNewFoodItem {
    void onClickItemNewFoodDelete (food _food);
    void onClickItemNewFoodAdd (food _food, String date);
    void onClickItemDelete (threeType th, int groupPosition, int childPosition);
}
