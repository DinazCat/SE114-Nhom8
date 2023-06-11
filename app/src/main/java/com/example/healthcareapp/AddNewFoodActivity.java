package com.example.healthcareapp;

import static java.lang.Float.parseFloat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.healthcareapp.Adapter.IngredientAdapterAdd;
import com.example.healthcareapp.ListInterface.ClickIngredientItem;
import com.example.healthcareapp.Model.ingredient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddNewFoodActivity extends AppCompatActivity {
    Button btAddIngredient;
    EditText etNameIngredient;
    DatabaseReference database;
    RecyclerView recyclerView;
    IngredientAdapterAdd ingredientAdapterAdd;
    Button btnBack;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    ArrayList<ingredient> ingredientArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_food);
        btAddIngredient = findViewById(R.id.addIngredient);
        etNameIngredient = findViewById(R.id.addIngredientName);

        btAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddNewFoodActivity.this, AddIngredientsActivity.class);
                if (etNameIngredient.getText().toString().trim().isEmpty())
                    Toast.makeText(AddNewFoodActivity.this, "Enter Food Name", Toast.LENGTH_SHORT).show();
                else {
                    i.putExtra("nameFood", etNameIngredient.getText().toString().trim());
                    startActivity(i);
                }
            }
        });

    }
}