package com.example.healthcareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcareapp.Adapter.IngredientAdapterAdd;
import com.example.healthcareapp.Model.ingredient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddIngredientsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference database ;
    IngredientAdapterAdd ingredientAdapterAdd;

    ArrayList<ingredient> ingredientArrayList;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredients);

        ingredientArrayList = new ArrayList<>();
        database = FirebaseDatabase.getInstance().getReference("ingredients");

        recyclerView = findViewById(R.id.recyclerviewSearchIngredient);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        ingredientArrayList = new ArrayList<>();
        ingredientAdapterAdd = new IngredientAdapterAdd(this, ingredientArrayList);
        recyclerView.setAdapter(ingredientAdapterAdd);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ingredient in = dataSnapshot.getValue(ingredient.class);
                    ingredientArrayList.add(in);
                }
                ingredientAdapterAdd.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddIngredientsActivity.this, "fail",Toast.LENGTH_SHORT).show();
            }
        });


    }
}