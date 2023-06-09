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
    public  static EditText FoodName, Total, Cal, Prep, Cooking;
    RatingBar FoodReview;
    public static String FName, FCal, FTotal,FPrep,FCooking, FRating = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_baiviet, container, false);
        FoodName = view.findViewById(R.id.write);
        Total = view.findViewById(R.id.write1);
        Cal= view.findViewById(R.id.write2);
        Prep = view.findViewById(R.id.write3);
        Cooking = view.findViewById(R.id.write4);
        FoodReview = view.findViewById(R.id.ratingbar);
        if( PostActivity.thaotac.equals("push"))
        {
            FName=""; FCal=""; FTotal = ""; FPrep = "";FCooking=" " ;FRating = "1.0";
            FoodReview.setRating(1.0f);
        }
        else{
            if (PostActivity.thaotac.equals("Share")) {
                FName= PostActivity.re.getNameRecipe();
                FCal=PostActivity.re.getCalorieRecipe();
                FTotal = "";
                FPrep = PostActivity.re.getPrep();
                FCooking= PostActivity.re.getCooking();
                FRating = "1.0";
                FoodReview.setRating(1.0f);

                FoodName.setText(FName);
                Cal.setText(FCal);
                Prep.setText(FPrep);
                Cooking.setText(FCooking);
            }
            else {
                FoodReview.setRating(Float.parseFloat(FRating));
                FoodName.setText(FName);
                Cal.setText(FCal);
                Prep.setText(FPrep);
                Total.setText(FTotal);
                Cooking.setText(FCooking);
            }
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