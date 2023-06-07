package com.example.healthcareapp.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcareapp.Adapter.ExpandableListViewAdapter;
import com.example.healthcareapp.Adapter.FoodAdapter;
import com.example.healthcareapp.AddWaterActivity;
import com.example.healthcareapp.Model.exercise;
import com.example.healthcareapp.Model.food;
import com.example.healthcareapp.PostActivity;
import com.example.healthcareapp.R;
import com.example.healthcareapp.SearchTopTabActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
public class AddFragment extends Fragment {
    ExpandableListViewAdapter listViewAdapter;
    ExpandableListView expandableListView;
    List<String> meals;
    HashMap<String, List<food>> foodList;
    Button btAddFoodExercise, btAddWater;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FragmentAListener listenter;
    TextView tvFoodCalories, tvExerciseCalories, tvGoalCalories, tvRemainingCalories;

    public interface FragmentAListener{
        void onInputASent(CharSequence input);
    }
    DatabaseReference database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        tvExerciseCalories = view.findViewById(R.id.exerciseCalories);
        tvFoodCalories = view.findViewById(R.id.foodCalories);
        tvGoalCalories = view.findViewById(R.id.goalCalories);
        tvRemainingCalories = view.findViewById(R.id.remainingCalories);

        expandableListView = view.findViewById(R.id.expandableLV);
        showList();
        listViewAdapter = new ExpandableListViewAdapter(expandableListView.getContext(), meals,foodList);
        expandableListView.setAdapter(listViewAdapter);

        //Add Food/Exercise Button
        btAddFoodExercise = view.findViewById(R.id.addFoodExercise);
        btAddFoodExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcherAddFoodAndExercise.launch(new Intent(getContext(), SearchTopTabActivity.class));
            }
        });


        //Add Water Button
        btAddWater = view.findViewById(R.id.addWater);
        btAddWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcherAddWater.launch(new Intent(getContext(), AddWaterActivity.class));
            }
        });

        //REMAINING CALORIES
        Calendar calendar = Calendar.getInstance();
        String today = DateFormat.format("yyyy-MM-dd", calendar).toString();

            //Tinh food calo da su dung
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("foodDiary");
        database.child(uid).child(today).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int calo = 0;
                for (DataSnapshot dataSnapshot : snapshot.child("Breakfast").getChildren()) {
                    food in = dataSnapshot.getValue(food.class);
                    calo += Integer.parseInt(in.getCaloriesFood());
                }
                for (DataSnapshot dataSnapshot : snapshot.child("Dinner").getChildren()) {
                    food in = dataSnapshot.getValue(food.class);
                    calo += Integer.parseInt(in.getCaloriesFood());
                }
                for (DataSnapshot dataSnapshot : snapshot.child("Lunch").getChildren()) {
                    food in = dataSnapshot.getValue(food.class);
                    calo += Integer.parseInt(in.getCaloriesFood());
                }
                for (DataSnapshot dataSnapshot : snapshot.child("Snack").getChildren()) {
                    food in = dataSnapshot.getValue(food.class);
                    calo += Integer.parseInt(in.getCaloriesFood());
                }
                tvFoodCalories.setText(String.valueOf(calo));
                tvRemainingCalories.setText(String.valueOf(1600-calo));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Tinh exercise calo da thuc hien + tinh remaining calo
        DatabaseReference database1 = FirebaseDatabase.getInstance().getReference("exerciseDiary");
        database1.child(uid).child(today).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int calo=0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    exercise in = dataSnapshot.getValue(exercise.class);
                    calo += Integer.parseInt(in.getCaloriesBurnedAMin());
                }
                tvExerciseCalories.setText(String.valueOf(calo));
                tvRemainingCalories.setText(String.valueOf(Integer.parseInt(tvRemainingCalories.getText().toString()) + calo ));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Chua lay dc du lieu goal calorie ***************************
        tvGoalCalories.setText("1600");

        return view;
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
    private void showList() {
        meals = new ArrayList<String>();
        foodList = new HashMap<String, List<food>>();

        List<food> breakfast = new ArrayList<>();
        List<food> lunch = new ArrayList<>();
        List<food> dinner = new ArrayList<>();
        List<food> snack = new ArrayList<>();

        meals.add("Breakfast");
        meals.add("Lunch");
        meals.add("Dinner");
        meals.add("Snack");
        meals.add("Water");
        meals.add("Exercise");

        Calendar calendar = Calendar.getInstance();
        String today = DateFormat.format("yyyy-MM-dd", calendar).toString();

        //BREAKFAST
        database = FirebaseDatabase.getInstance().getReference("foodDiary");
        database.child(uid).child(today).child("Breakfast").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    food in = dataSnapshot.getValue(food.class);
                    breakfast.add(in);
                }
                foodList.put("Breakfast",breakfast);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getView().getContext(), "fail", Toast.LENGTH_SHORT).show();
            }
        });

        //LUNCH
        database = FirebaseDatabase.getInstance().getReference("foodDiary");
        database.child(uid).child(today).child("Lunch").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    food in = dataSnapshot.getValue(food.class);
                    lunch.add(in);
                }
                foodList.put("Lunch",lunch);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getView().getContext(), "fail", Toast.LENGTH_SHORT).show();
            }
        });

        //DINNER
        database = FirebaseDatabase.getInstance().getReference("foodDiary");
        database.child(uid).child(today).child("Dinner").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    food in = dataSnapshot.getValue(food.class);
                    dinner.add(in);
                }
                foodList.put("Dinner",dinner);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getView().getContext(), "fail", Toast.LENGTH_SHORT).show();
            }
        });

        //SNACK
        database = FirebaseDatabase.getInstance().getReference("foodDiary");
        database.child(uid).child(today).child("Snack").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    food in = dataSnapshot.getValue(food.class);
                    snack.add(in);
                }
                foodList.put("Snack",snack);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getView().getContext(), "fail", Toast.LENGTH_SHORT).show();
            }
        });


    }
    ActivityResultLauncher<Intent> launcherAddWater = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                    }
                }
            });
    ActivityResultLauncher<Intent> launcherAddFoodAndExercise = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                    }
                }
            });
}
