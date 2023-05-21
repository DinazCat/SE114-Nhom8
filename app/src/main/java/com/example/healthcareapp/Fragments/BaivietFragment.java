package com.example.healthcareapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;

import com.example.healthcareapp.PostActivity;
import com.example.healthcareapp.R;

public class BaivietFragment extends Fragment {
public  static EditText FoodName, Ingredient, Making, Summary;
RatingBar FoodReview;
public static String FName, FIngredient, FMaking, FSummary, FRating;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_baiviet, container, false);
        FoodName = view.findViewById(R.id.write);
        Ingredient = view.findViewById(R.id.write1);
        Making= view.findViewById(R.id.write2);
        Summary = view.findViewById(R.id.write3);
        FoodReview = view.findViewById(R.id.ratingbar);
        if( PostActivity.thaotac.equals("push"))
        {
            FName=""; FIngredient=""; FMaking = ""; FSummary = ""; FRating = "";
            FoodReview.setRating(1.0f);
        }
        else{
            FoodReview.setRating(Float.parseFloat(FRating));
            FoodName.setText(FName);
            Ingredient.setText(FIngredient);
            Making.setText(FMaking);
            Summary.setText(FSummary);
        }

//        FName = FoodName.getText().toString();
//        FIngredient = Ingredient.getText().toString();
//        FMaking = Making.getText().toString();
//        FSummary = Summary.getText().toString();


        // FoodReview.setRating(0.0f);
        FoodReview.setStepSize(1.0f);
        FoodReview.setOnRatingBarChangeListener((ratingBar, v, b) -> FRating = String.valueOf(v));
        System.out.println("sao"+FRating);
        // Inflate the layout for this fragment
        return view;
    }
}