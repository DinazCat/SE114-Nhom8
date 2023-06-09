package com.example.healthcareapp.Fragments;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcareapp.Model.bmiInfo;
import com.example.healthcareapp.Model.exercise;
import com.example.healthcareapp.Model.food;
import com.example.healthcareapp.R;
import com.google.android.material.progressindicator.CircularProgressIndicator;
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

public class HomeFragment extends Fragment {
    private TextView tv_date, tv_baseGoal, tv_Water, tv_snack, tv_exercise, tv_breakfast, tv_lunch, tv_dinner, tv_remaining;
    private
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    CircularProgressIndicator cpi;
    DatabaseReference database, database1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //tv_weeklyGoal = view.findViewById(R.id.weeklyGoal_tv);
        tv_date = view.findViewById(R.id.date);
        tv_baseGoal = view.findViewById(R.id.baseGoal);
        tv_Water = view.findViewById(R.id.water);
        tv_exercise = view.findViewById(R.id.exercise);
        tv_breakfast = view.findViewById(R.id.breakfast);
        tv_lunch = view.findViewById(R.id.lunch);
        tv_dinner = view.findViewById(R.id.dinner);
        tv_snack = view.findViewById(R.id.snacks);
        tv_remaining = view.findViewById(R.id.remaining);
        cpi = view.findViewById(R.id.circularProgressIndicator);
        //bmi thay đổi theo ngày khi sửa
        Calendar calendar = Calendar.getInstance();
        String today = DateFormat.format("yyyy-MM-dd", calendar).toString();
        setBaseGoal();
        setWater();
        setFoodAndExercise(today);

        tv_date.setOnClickListener(new View.OnClickListener() {
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
                            tv_date.setText("Today");
                            String today = DateFormat.format("yyyy-MM-dd", calendar).toString();
                            setBaseGoal();
                            setWater();
                            setFoodAndExercise(today);

                        }
                        else{
                            calendar.set(year, month, dayOfMonth);
                            tv_date.setText(DateFormat.format("dd/MM/yyyy", calendar).toString());
                            String string = DateFormat.format("yyyy-MM-dd", calendar).toString();
                            setBaseGoal();
                            setWater();
                            setFoodAndExercise(string);

                        }
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        return view;
    }

    private void setFoodAndExercise(String date) {
            database = FirebaseDatabase.getInstance().getReference("foodDiary");
            database.child(uid).child(date).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int total=0;
                    int calo=0;
                    for (DataSnapshot dataSnapshot : snapshot.child("Breakfast").getChildren()) {
                        food in = dataSnapshot.getValue(food.class);
                        calo += Integer.parseInt(in.getCaloriesFood());
                        total += Integer.parseInt(in.getCaloriesFood());
                    }
                    tv_breakfast.setText(String.valueOf(calo));
                    calo=0;
                    for (DataSnapshot dataSnapshot : snapshot.child("Dinner").getChildren()) {
                        food in = dataSnapshot.getValue(food.class);
                        calo += Integer.parseInt(in.getCaloriesFood());
                        total += Integer.parseInt(in.getCaloriesFood());
                    }
                    tv_lunch.setText(String.valueOf(calo));
                    calo=0;
                    for (DataSnapshot dataSnapshot : snapshot.child("Lunch").getChildren()) {
                        food in = dataSnapshot.getValue(food.class);
                        calo += Integer.parseInt(in.getCaloriesFood());
                        total += Integer.parseInt(in.getCaloriesFood());
                    }
                    tv_dinner.setText(String.valueOf(calo));
                    calo=0;
                    for (DataSnapshot dataSnapshot : snapshot.child("Snack").getChildren()) {
                        food in = dataSnapshot.getValue(food.class);
                        calo += Integer.parseInt(in.getCaloriesFood());
                        total += Integer.parseInt(in.getCaloriesFood());
                    }
                    tv_snack.setText(String.valueOf(calo));
                    tv_remaining.setText(String.valueOf(total));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            database1 = FirebaseDatabase.getInstance().getReference("exerciseDiary");
            database1.child(uid).child(date).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int calo=0;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        exercise in = dataSnapshot.getValue(exercise.class);
                        calo += Integer.parseInt(in.getCaloriesBurnedAMin());
                    }
                    tv_exercise.setText(String.valueOf(calo));
                    tv_remaining.setText(String.valueOf(Integer.parseInt(tv_baseGoal.getText().toString()) + calo - Integer.parseInt(tv_remaining.getText().toString()) ));
                    int goalCalo = Integer.parseInt(tv_baseGoal.getText().toString());
                    int totalCalo = Integer.parseInt(tv_remaining.getText().toString());
                    int p = (totalCalo*100)/goalCalo;
                    if (goalCalo!=0)  cpi.setProgress(100-p);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }
    private void setBaseGoal(){
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
                if(tv_date.getText().toString().equals("Today")){
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
                        tv_baseGoal.setText(String.valueOf(bmiInfos.get(0).CaloriesNeedToBurn()));
                    }
                    else{
                        tv_baseGoal.setText(String.valueOf(bmiList.get(bmiList.size()-1).CaloriesNeedToBurn()));
                    }
                }
                else{
                    Calendar calendar = Calendar.getInstance();
                    Date selectedDate = null;
                    try {
                        selectedDate = df.parse(tv_date.getText().toString());
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
                        tv_baseGoal.setText(String.valueOf(bmiInfos.get(0).CaloriesNeedToBurn()));
                    }
                    else{
                        tv_baseGoal.setText(String.valueOf(bmiList.get(bmiList.size()-1).CaloriesNeedToBurn()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Error:" + error.getMessage());
            }
        });
    }
    private void setWater(){
        String date;
        if(tv_date.getText().toString().equals("Today")) {
            date = (DateFormat.format("yyyy/MM/dd", Calendar.getInstance()).toString());
        }
        else {
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            Date selectedDate = null;
            try {
                selectedDate = new SimpleDateFormat("dd/MM/yyyy").parse(tv_date.getText().toString());
                date = df.format(selectedDate).toString();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        Query query = FirebaseDatabase.getInstance().getReference("water").child(uid).orderByChild("time").equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long sumWaterAmount = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String waterAmount = ds.child("waterAmount").getValue().toString();
                    sumWaterAmount += Long.parseLong(waterAmount);

                }
                tv_Water.setText(String.valueOf(sumWaterAmount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Error:" + error.getMessage());
            }
        });
    }
}