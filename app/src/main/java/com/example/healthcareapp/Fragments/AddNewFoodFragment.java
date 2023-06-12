package com.example.healthcareapp.Fragments;

import static java.lang.Float.parseFloat;

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
import android.widget.EditText;
import android.widget.Toast;

import com.example.healthcareapp.AddIngredientsActivity;
import com.example.healthcareapp.AddNewFoodActivity;
import com.example.healthcareapp.Model.food;
import com.example.healthcareapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class AddNewFoodFragment extends Fragment {

    Button btSaveFood;
    EditText etNameNewFood, etCalorieNewFood, etServingNewFood;
    DatabaseReference database;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_food, container, false);
        btSaveFood = view.findViewById(R.id.saveFood);
        etCalorieNewFood = view.findViewById(R.id.addFoodCalorie);
        etNameNewFood = view.findViewById(R.id.addFoodName);
        etServingNewFood = view.findViewById(R.id.addServing);
        btSaveFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCalorieNewFood.getText().toString().trim().isEmpty() || etNameNewFood.getText().toString().trim().isEmpty() || etServingNewFood.getText().toString().trim().isEmpty()) {
                    Toast.makeText(view.getContext(), "Please Enter Name, Calorie and Serving", Toast.LENGTH_SHORT).show();
                } else {
                    database = FirebaseDatabase.getInstance().getReference("foods");
                    Random random = new Random();
                    int randomID = random.nextInt(1000000);

                    food _food = new food();
                    _food.setIdFood(String.valueOf(randomID));
                    _food.setNameFood(etNameNewFood.getText().toString().trim());
                    _food.setCaloriesFood(etCalorieNewFood.getText().toString().trim());
                    _food.setServingFood(etServingNewFood.getText().toString().trim());

                    database.child(_food.getIdFood()).setValue(_food);
                    Toast.makeText(view.getContext(), "Add New Food Success", Toast.LENGTH_SHORT).show();

                }
            }
        });
        return view;
    }


}