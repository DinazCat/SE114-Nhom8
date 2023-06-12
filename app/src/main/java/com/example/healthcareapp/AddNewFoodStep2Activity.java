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
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcareapp.Adapter.IngredientAdapterAdd;
import com.example.healthcareapp.Adapter.IngredientAddedAdapter;
import com.example.healthcareapp.ListInterface.ClickIngredientItem;
import com.example.healthcareapp.Model.food;
import com.example.healthcareapp.Model.ingredient;
import com.example.healthcareapp.Model.recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class AddNewFoodStep2Activity extends AppCompatActivity {
    Button saveBtn;
    DatabaseReference database, database1;
    ArrayList<ingredient> ingredientArrayList;
    TextView tvNameFood;
    float caloriesRecipe = 0f;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    RecyclerView recyclerView;
    IngredientAddedAdapter ingredientAddedAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_food_step2);
        tvNameFood = findViewById(R.id.addNameFood);

        Intent i = getIntent();

        String idRecipe = i.getStringExtra("idFood");
        String nameRecipe = i.getStringExtra("nameFood");


        //RECYCLERVIEW
        recyclerView = findViewById(R.id.recyclerviewIng);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        ingredientArrayList = new ArrayList<>();
        ingredientAddedAdapter = new IngredientAddedAdapter(ingredientArrayList, new ClickIngredientItem() {
            @Override
            public void onClickItemIngredient(ingredient in) {
                database = FirebaseDatabase.getInstance().getReference("newFoods");
                database.child(uid).child(String.valueOf(idRecipe)).child(nameRecipe).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot Snapshot: snapshot.getChildren()) {
                            Snapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Toast.makeText(AddNewFoodStep2Activity.this, "Remove Success", Toast.LENGTH_SHORT).show();
                }
            });
        //ADD LIST TO RECYCLERVIEW
        recyclerView.setAdapter(ingredientAddedAdapter);
        database1 = FirebaseDatabase.getInstance().getReference("newFoods");
        database1.child(uid).child(idRecipe).child(nameRecipe).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ingredient in = dataSnapshot.getValue(ingredient.class);
                    caloriesRecipe += parseFloat(in.getCalorieIngredient()) * parseFloat(in.getQuantity());
                    ingredientArrayList.add(in);
                }
                ingredientAddedAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddNewFoodStep2Activity.this, "fail",Toast.LENGTH_SHORT).show();
            }
        });

        tvNameFood.setText(nameRecipe);
        if (nameRecipe.isEmpty()) Toast.makeText(this, "Add Fail", Toast.LENGTH_SHORT).show();
         saveBtn = findViewById(R.id.saveFood);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipe re = new recipe();
                re.setIdRecipe(idRecipe);
                re.setNameRecipe(nameRecipe);
                re.setCalorieRecipe(String.valueOf(caloriesRecipe));
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                database = FirebaseDatabase.getInstance().getReference("newRecipe");
                database.child(uid).child(idRecipe).setValue(re);
                Toast.makeText(AddNewFoodStep2Activity.this, "Add Success", Toast.LENGTH_SHORT).show();
            }
        });
    }
}