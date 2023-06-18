package com.example.healthcareapp.Fragments;

import static java.lang.Float.parseFloat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.healthcareapp.Adapter.ExerciseAdapter;
import com.example.healthcareapp.Adapter.NewFoodAdapter;
import com.example.healthcareapp.AddIngredientsActivity;
import com.example.healthcareapp.AddNewFoodActivity;
import com.example.healthcareapp.ListInterface.ClickExerciseItem;
import com.example.healthcareapp.ListInterface.ClickNewFoodItem;
import com.example.healthcareapp.Model.exercise;
import com.example.healthcareapp.Model.food;
import com.example.healthcareapp.Model.recipe;
import com.example.healthcareapp.Model.threeType;
import com.example.healthcareapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class AddNewFoodFragment extends Fragment {
    RecyclerView recyclerViewNewFood;
    List<food> newFoodList;
    NewFoodAdapter newFoodAdapter;
    Button btSaveFood;
    EditText etNameNewFood, etCalorieNewFood, etServingNewFood;
    DatabaseReference database, database1;
    private Spinner spn;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_food, container, false);
        btSaveFood = view.findViewById(R.id.saveFood);
        Calendar calendar = Calendar.getInstance();
        String today = DateFormat.format("yyyy-MM-dd", calendar).toString();
        etCalorieNewFood = view.findViewById(R.id.addFoodCalorie);
        etNameNewFood = view.findViewById(R.id.addFoodName);
        etServingNewFood = view.findViewById(R.id.addServing);


        //region RecyleViewFood
        recyclerViewNewFood = view.findViewById(R.id.recyclerviewNewFood);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerViewNewFood.getContext());
        recyclerViewNewFood.setLayoutManager(linearLayoutManager);
        newFoodList = new ArrayList<>();
        newFoodAdapter = new NewFoodAdapter(newFoodList);

        recyclerViewNewFood.setAdapter(newFoodAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(recyclerViewNewFood.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewNewFood.addItemDecoration(itemDecoration);

        database = FirebaseDatabase.getInstance().getReference("newFoodUserAdd");
        database.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newFoodList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    food _food = dataSnapshot.getValue(food.class);
                    newFoodList.add(_food);
                }
                newFoodAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //endregion

        //lưu dữ liệu xuống 2 tree food và newfooduseradded
        btSaveFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCalorieNewFood.getText().toString().trim().isEmpty() || etNameNewFood.getText().toString().trim().isEmpty() || etServingNewFood.getText().toString().trim().isEmpty()) {
                    Toast.makeText(view.getContext(), "Please Enter Name, Calorie and Serving", Toast.LENGTH_SHORT).show();
                } else {

                    Random random = new Random();
                    int randomID = random.nextInt(1000000);

                    food _food = new food();
                    _food.setIdFood(String.valueOf(randomID));
                    _food.setNameFood(etNameNewFood.getText().toString().trim());
                    _food.setCaloriesFood(etCalorieNewFood.getText().toString().trim());
                    _food.setServingFood(etServingNewFood.getText().toString().trim());

                    database = FirebaseDatabase.getInstance().getReference("newFoodUserAdd");
                    database.child(uid).child(_food.getIdFood()).setValue(_food);

                    database1 = FirebaseDatabase.getInstance().getReference("foods");
                    database1.child(_food.getIdFood()).setValue(_food);

                    Toast.makeText(view.getContext(), "Add New Food Success", Toast.LENGTH_SHORT).show();

                    newFoodAdapter.notifyDataSetChanged();
                }
            }
        });
        return view;
    }


}