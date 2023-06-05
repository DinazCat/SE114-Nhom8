package com.example.healthcareapp;

import static java.lang.Float.parseFloat;
import static java.lang.System.in;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcareapp.Adapter.IngredientAdapterAdd;
import com.example.healthcareapp.ListInterface.ClickIngredientItem;
import com.example.healthcareapp.Model.food;
import com.example.healthcareapp.Model.ingredient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.CDATASection;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class AddIngredientsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    IngredientAdapterAdd ingredientAdapterAdd;

    ArrayList<ingredient> ingredientArrayList;
    ArrayList<ingredient> listIngredientNewFood;

    DatabaseReference database, database1, databaseReadFood;
    EditText editTextName ;
    EditText editTextQuantity;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    Button saveBtn;
    float calories = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredients);

        ingredientArrayList = new ArrayList<>();
        editTextName = findViewById(R.id.addIngredientName);
        editTextQuantity = findViewById(R.id.addIngredientQuantity);
        saveBtn = findViewById(R.id.saveFood);
        //RECYCLERVIEW
        recyclerView = findViewById(R.id.recyclerviewSearchIngredient);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        ingredientArrayList = new ArrayList<>();
        ingredientAdapterAdd = new IngredientAdapterAdd(ingredientArrayList, new ClickIngredientItem() {
            @Override
            public void onClickItemIngredient(ingredient in) {
                database = FirebaseDatabase.getInstance().getReference("newFoods");
                in.setQuantity(editTextQuantity.getText().toString());
                calories += parseFloat(in.getCalorieIngredient()) * parseFloat(editTextQuantity.getText().toString());
                database.child(uid).child(String.valueOf(editTextName.getText())).child(in.getIdIngredient()).setValue(in);
            }
        });
        //ADD LIST TO RECYCLERVIEW
        recyclerView.setAdapter(ingredientAdapterAdd);
        database1 = FirebaseDatabase.getInstance().getReference("ingredients");
        database1.addValueEventListener(new ValueEventListener() {
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

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int randomID = random.nextInt(100000);

                food _food = new food();
                _food.setIdFood(String.valueOf(randomID));
                _food.setNameFood(editTextName.getText().toString());
                _food.setCaloriesFood(String.valueOf(calories));
                _food.setServingFood("1 phần");
                database = FirebaseDatabase.getInstance().getReference("foods");
                database.child(String.valueOf(randomID)).setValue(_food);
            }
        });
    }
}