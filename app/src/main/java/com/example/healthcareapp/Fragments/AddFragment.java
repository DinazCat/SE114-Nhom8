package com.example.healthcareapp.Fragments;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcareapp.Adapter.ExpandableListViewAdapter;
import com.example.healthcareapp.Adapter.FoodAdapter;
import com.example.healthcareapp.AddWaterActivity;
import com.example.healthcareapp.Model.bmiInfo;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    TextView tvFoodCalories, tvExerciseCalories, tvGoalCalories, tvRemainingCalories, tvDate, tvGoal;

    public interface FragmentAListener{
        void onInputASent(CharSequence input);
    }
    DatabaseReference database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        Calendar calendar = Calendar.getInstance();
        String string = DateFormat.format("yyyy-MM-dd", calendar).toString();
        tvExerciseCalories = view.findViewById(R.id.exerciseCalories);
        tvFoodCalories = view.findViewById(R.id.foodCalories);
        tvGoalCalories = view.findViewById(R.id.goalCalories);
        tvRemainingCalories = view.findViewById(R.id.remainingCalories);
        tvDate = view.findViewById(R.id.date);
        tvGoal = view.findViewById(R.id.goalCalories);
        setBaseGoal();
        expandableListView = view.findViewById(R.id.expandableLV);
        showList(string);
        listViewAdapter = new ExpandableListViewAdapter(expandableListView.getContext(), meals,foodList);
        expandableListView.setAdapter(listViewAdapter);

        //REMAINING CALORIES

        setList(string);
        //Add Date Calendar
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        if (calendar.get(Calendar.DAY_OF_MONTH) == dayOfMonth && calendar.get(Calendar.MONTH) == month && calendar.get(Calendar.YEAR) == year){
                            tvDate.setText("Today");
                            String string = DateFormat.format("yyyy-MM-dd", calendar).toString();
                            setBaseGoal();
                            setList(string);
                            showList(string);
                            listViewAdapter = new ExpandableListViewAdapter(expandableListView.getContext(), meals,foodList);
                            expandableListView.setAdapter(listViewAdapter);
                        }
                        else{
                            calendar.set(year, month, dayOfMonth);
                            tvDate.setText(DateFormat.format("dd/MM/yyyy", calendar).toString());
                            String string = DateFormat.format("yyyy-MM-dd", calendar).toString();
                            setBaseGoal();
                            setList(string);
                            showList(string);
                            listViewAdapter = new ExpandableListViewAdapter(expandableListView.getContext(), meals,foodList);
                            expandableListView.setAdapter(listViewAdapter);
                        }
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
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


        return view;
    }
    private void setBaseGoal(){
        int calorie =0;
        Query query = FirebaseDatabase.getInstance().getReference("bmiDiary").child(uid).orderByChild("time");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<bmiInfo> bmiInfos = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()){
                    bmiInfo bmiInfo = ds.getValue(bmiInfo.class);
                    bmiInfos.add(bmiInfo);
                }
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                if(tvDate.getText().toString().equals("Today")){
                    Calendar calendar = Calendar.getInstance();
                    Date selectedDate = null;
                    try {
                        selectedDate = df.parse(DateFormat.format("dd/MM/yyyy", calendar).toString());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    ArrayList<bmiInfo> bmiList = new ArrayList<>();
                    for(int i = 0; i < bmiInfos.size(); i++){
                        calendar.setTimeInMillis(bmiInfos.get(i).time);
                        Date date = null;
                        try {
                            date = df.parse(DateFormat.format("dd/MM/yyyy", calendar).toString());

                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        if (date.compareTo(selectedDate) <= 0) {
                            bmiList.add(bmiInfos.get(i));
                        }
                    }
                    if(bmiList.size() <= 0){
                        tvGoal.setText(String.valueOf(bmiInfos.get(0).CaloriesNeedToBurn()));
                    }
                    else{
                        tvGoal.setText(String.valueOf(bmiList.get(bmiList.size()-1).CaloriesNeedToBurn()));
                    }
                }
                else{
                    Calendar calendar = Calendar.getInstance();
                    Date selectedDate = null;
                    try {
                        selectedDate = df.parse(tvDate.getText().toString());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    ArrayList<bmiInfo> bmiList = new ArrayList<>();
                    for(int i = 0; i < bmiInfos.size(); i++){
                        calendar.setTimeInMillis(bmiInfos.get(i).time);
                        Date date = null;
                        try {
                            date = df.parse(DateFormat.format("dd/MM/yyyy", calendar).toString());
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        if (date.compareTo(selectedDate) <= 0) {
                            bmiList.add(bmiInfos.get(i));
                        }
                    }
                    if(bmiList.size() <= 0){
                        tvGoal.setText(String.valueOf(bmiInfos.get(0).CaloriesNeedToBurn()));
                    }
                    else{
                        tvGoal.setText(String.valueOf(bmiList.get(bmiList.size()-1).CaloriesNeedToBurn()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Error:" + error.getMessage());
            }
        });
    }
    private  void setList(String date) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("foodDiary");
        database.child(uid).child(date).addValueEventListener(new ValueEventListener() {
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
                int i = Integer.parseInt(tvGoal.getText().toString());
                tvRemainingCalories.setText(String.valueOf(i-calo));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Tinh exercise calo da thuc hien + tinh remaining calo
        DatabaseReference database1 = FirebaseDatabase.getInstance().getReference("exerciseDiary");
        database1.child(uid).child(date).addValueEventListener(new ValueEventListener() {
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

    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
    private void showList(String date) {
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

        //BREAKFAST
        database = FirebaseDatabase.getInstance().getReference("foodDiary");
        database.child(uid).child(date).child("Breakfast").addValueEventListener(new ValueEventListener() {
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
        database.child(uid).child(date).child("Lunch").addValueEventListener(new ValueEventListener() {
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
        database.child(uid).child(date).child("Dinner").addValueEventListener(new ValueEventListener() {
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
        database.child(uid).child(date).child("Snack").addValueEventListener(new ValueEventListener() {
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
