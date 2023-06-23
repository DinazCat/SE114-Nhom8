package com.example.healthcareapp.Fragments;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcareapp.Adapter.ExpandableListViewAdapter;
import com.example.healthcareapp.AddWaterActivity;
import com.example.healthcareapp.ListInterface.ClickNewFoodItem;
import com.example.healthcareapp.Model.bmiInfo;
import com.example.healthcareapp.Model.exercise;
import com.example.healthcareapp.Model.food;
import com.example.healthcareapp.Model.threeType;
import com.example.healthcareapp.Model.water;
import com.example.healthcareapp.R;
import com.example.healthcareapp.SearchExerciseActivity;
import com.example.healthcareapp.SearchTopTabActivity;
import com.example.healthcareapp.SearchTopTapRecipe;
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

import androidx.fragment.app.FragmentManager;

public class AddFragment extends Fragment {
    ExpandableListView expandableListView;
    ExpandableListViewAdapter listViewAdapter;
    List<String> meals;
    HashMap<String, List<threeType>> threeList;
    Button btAddFood, btAddExercise, btAddRecipe, btAddWater;
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
        String date = DateFormat.format("yyyy-MM-dd", calendar).toString();
        tvExerciseCalories = view.findViewById(R.id.exerciseCalories);
        tvFoodCalories = view.findViewById(R.id.foodCalories);
        tvGoalCalories = view.findViewById(R.id.goalCalories);
        tvRemainingCalories = view.findViewById(R.id.remainingCalories);
        tvDate = view.findViewById(R.id.date);
        tvGoal = view.findViewById(R.id.goalCalories);
        setBaseGoal();
        setList(date);
        showList(date);
        expandableListView = view.findViewById(R.id.expandableLV);
        listViewAdapter = new ExpandableListViewAdapter(view.getContext(), expandableListView.getContext(), meals, threeList, date, new ClickNewFoodItem() {
            @Override
            public void onClickItemNewFoodDelete(food _food) {

            }

            @Override
            public void onClickItemNewFoodAdd(food _food, String date) {

            }

            @Override
            public void onClickItemDelete(threeType th, int groupPosition, int childPosition) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("Delete");
                dialog.setIcon(R.drawable.noti_icon);
                dialog.setMessage("You want to delete??");
                dialog.setCancelable(false);
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        if (meals.get(groupPosition).equals("Breakfast") || meals.get(groupPosition).equals("Lunch") || meals.get(groupPosition).equals("Dinner")
                                || meals.get(groupPosition).equals("Snack") ) {
                            DatabaseReference database = FirebaseDatabase.getInstance().getReference("foodDiary");
                            database.child(uid).child(date).child(meals.get(groupPosition)).child(th.getIdType()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    snapshot.getRef().removeValue();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                        else {
                            if (meals.get(groupPosition).equals("Exercise")) {
                                DatabaseReference database = FirebaseDatabase.getInstance().getReference("exerciseDiary");
                                database.child(uid).child(date).child(th.getIdType()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        snapshot.getRef().removeValue();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                            else {
                                DatabaseReference database = FirebaseDatabase.getInstance().getReference("water");
                                database.child(uid).child(date).child(th.getIdType()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        snapshot.getRef().removeValue();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                            listViewAdapter.notifyDataSetChanged();
                        }

                    }

                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                // Show the Alert Dialog box
                alertDialog.show();
            }

        });
        expandableListView.setAdapter(listViewAdapter);

        //region CHANGE DATE
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
                            ExpandableListViewAdapter listViewAdapter = new ExpandableListViewAdapter(view.getContext(), expandableListView.getContext(), meals, threeList, string, new ClickNewFoodItem() {
                                @Override
                                public void onClickItemNewFoodDelete(food _food) {

                                }

                                @Override
                                public void onClickItemNewFoodAdd(food _food, String date) {

                                }

                                @Override
                                public void onClickItemDelete(threeType th, int groupPosition, int childPosition) {

                                }
                            });
                            expandableListView.setAdapter(listViewAdapter);
                        }
                        else{
                            calendar.set(year, month, dayOfMonth);
                            tvDate.setText(DateFormat.format("dd/MM/yyyy", calendar).toString());
                            String string = DateFormat.format("yyyy-MM-dd", calendar).toString();
                            setBaseGoal();
                            setList(string);
                            showList(string);
                            ExpandableListViewAdapter listViewAdapter = new ExpandableListViewAdapter(view.getContext(), expandableListView.getContext(), meals, threeList, string, new ClickNewFoodItem() {
                                @Override
                                public void onClickItemNewFoodDelete(food _food) {

                                }

                                @Override
                                public void onClickItemNewFoodAdd(food _food, String date) {

                                }

                                @Override
                                public void onClickItemDelete(threeType th, int groupPosition, int childPosition) {

                                }
                            });
                            expandableListView.setAdapter(listViewAdapter);
                        }
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        //endregion
        //Add Food/Exercise/Recipe/Water Button
        btAddFood = view.findViewById(R.id.addFood);
        btAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcherAddFoodAndExercise.launch(new Intent(getContext(), SearchTopTabActivity.class));
            }
        });
        btAddExercise = view.findViewById(R.id.addExercise);
        btAddRecipe = view.findViewById(R.id.addRecipe);
        btAddWater = view.findViewById(R.id.addWater);
        btAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcherAddFoodAndExercise.launch(new Intent(getContext(), SearchExerciseActivity.class));
            }
        });

        btAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcherAddFoodAndExercise.launch(new Intent(getContext(), SearchTopTapRecipe.class));
            }
        });

        btAddWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcherAddFoodAndExercise.launch(new Intent(getContext(), AddWaterActivity.class));;
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
        threeList = new HashMap<String, List<threeType>>();

        List<threeType> breakfast = new ArrayList<>();
        List<threeType> lunch = new ArrayList<>();
        List<threeType> dinner = new ArrayList<>();
        List<threeType> snack = new ArrayList<>();
        List<threeType> exercise = new ArrayList<>();
        List<threeType> water = new ArrayList<>();
        meals.clear();
        meals.add("Breakfast");
        meals.add("Lunch");
        meals.add("Dinner");
        meals.add("Snack");
        meals.add("Water");
        meals.add("Exercise");
        //threeType type = new threeType(in.getIdFood(),in.getNameFood(),in.getCaloriesFood(),"calories");
        //BREAKFAST
        database = FirebaseDatabase.getInstance().getReference("foodDiary");
        database.child(uid).child(date).child("Breakfast").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                breakfast.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    food in = dataSnapshot.getValue(food.class);
                    threeType type = new threeType(in.getIdFood(),in.getNameFood(),in.getCaloriesFood()," calories");
                    breakfast.add(type);
                }
                threeList.put("Breakfast",breakfast);

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
                lunch.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    food in = dataSnapshot.getValue(food.class);
                    threeType type = new threeType(in.getIdFood(),in.getNameFood(),in.getCaloriesFood()," calories");
                    lunch.add(type);
                }
                threeList.put("Lunch",lunch);

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
                dinner.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    food in = dataSnapshot.getValue(food.class);
                    threeType type = new threeType(in.getIdFood(),in.getNameFood(),in.getCaloriesFood()," calories");
                    dinner.add(type);
                }
                threeList.put("Dinner",dinner);

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
                snack.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    food in = dataSnapshot.getValue(food.class);
                    threeType type = new threeType(in.getIdFood(),in.getNameFood(),in.getCaloriesFood()," calories");
                    snack.add(type);
                }
                threeList.put("Snack",snack);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getView().getContext(), "fail", Toast.LENGTH_SHORT).show();
            }
        });

        //EXERCISE
        database = FirebaseDatabase.getInstance().getReference("exerciseDiary");
        database.child(uid).child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                exercise.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    exercise in = dataSnapshot.getValue(exercise.class);
                    threeType type = new threeType(in.getIdExercise(),in.getNameExercise(),in.getCaloriesBurnedAMin()," calories");
                    exercise.add(type);
                }
                threeList.put("Exercise",exercise);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getView().getContext(), "fail", Toast.LENGTH_SHORT).show();
            }
        });

        //WATER
        database = FirebaseDatabase.getInstance().getReference("water");
        database.child(uid).child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                water.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    water in = dataSnapshot.getValue(water.class);
                    threeType type = new threeType(in.getIdwater(),"water",in.getWaterAmount()," ml");
                    water.add(type);
                }
                threeList.put("Water",water);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getView().getContext(), "fail", Toast.LENGTH_SHORT).show();
            }
        });

    }
    //region CHANGE TO ACTIVITY
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
    //endregion
}
