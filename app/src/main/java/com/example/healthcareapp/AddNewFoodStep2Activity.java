package com.example.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.healthcareapp.Model.food;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class AddNewFoodStep2Activity extends AppCompatActivity {
    Button saveBtn;
    DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_food_step2);
        Intent i = getIntent();
        String nameFood = i.getStringExtra("nameFood");
        float calories =0f;
        i.getFloatExtra("calories",calories);
        if (nameFood.isEmpty()) Toast.makeText(this, "faILLLL", Toast.LENGTH_SHORT).show();
         saveBtn = findViewById(R.id.saveFood);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Random random = new Random();
                int randomID = random.nextInt(100000);

                food _food = new food();
                _food.setIdFood(String.valueOf(randomID));
                _food.setNameFood(nameFood);
                _food.setCaloriesFood(String.valueOf(calories));
                _food.setServingFood("1 phần");
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                database = FirebaseDatabase.getInstance().getReference("newRecipe");
                database.child(uid).child(String.valueOf(randomID)).setValue(_food);
            }
        });
    }
}