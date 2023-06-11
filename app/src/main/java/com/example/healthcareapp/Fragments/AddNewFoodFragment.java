package com.example.healthcareapp.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.healthcareapp.AddIngredientsActivity;
import com.example.healthcareapp.AddNewFoodActivity;
import com.example.healthcareapp.R;

public class AddNewFoodFragment extends Fragment {

    Button btAddFood;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_food, container, false);
        btAddFood = view.findViewById(R.id.addFood);
        btAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcherAddIngredient.launch(new Intent(getContext(), AddNewFoodActivity.class));
            }
        });
        return view;
    }

    ActivityResultLauncher<Intent> launcherAddIngredient = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                    }
                }
            });
}