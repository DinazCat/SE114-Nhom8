package com.example.healthcareapp.Fragments;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import com.example.healthcareapp.Language;
import com.example.healthcareapp.LanguageUtils;
import com.example.healthcareapp.Model.bmiInfo;
import com.example.healthcareapp.Model.exercise;
import com.example.healthcareapp.Model.food;
import com.example.healthcareapp.NoteActivity;
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
    private TextView tv_analysis, tv_foxsay, tv_date, tv_baseGoal, tv_Water, tv_snack, tv_exercise, tv_breakfast, tv_lunch, tv_dinner, tv_remaining, tv_bmi;
    private
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    CircularProgressIndicator cpi;
    DatabaseReference database, database1;
    int basegoal;
    ImageView imageViewNote, imageViewBack;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //Datetime
        Calendar calendar = Calendar.getInstance();
        String today = DateFormat.format("yyyy-MM-dd", calendar).toString();
        String todayHT = DateFormat.format("dd/MM/yyyy", calendar).toString();
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
        tv_bmi = view.findViewById(R.id.bmi);
        tv_foxsay = view.findViewById(R.id.foxSay);
        tv_analysis = view.findViewById(R.id.analysis);
        imageViewBack = view.findViewById(R.id.back_today);
        imageViewNote = view.findViewById(R.id.note);

        //bmi thay đổi theo ngày khi sửa

        setBaseGoal();
        setWater();
        setFoodAndExercise(today);
        if (tv_date.getText().toString().equals("Hôm nay")) LanguageUtils.setCurrentLanguage(Language.VIETNAMESE);
        imageViewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_date.getText().toString().equals("Today") || tv_date.getText().toString().equals("Hôm nay")) {
                    Intent i = new Intent(getContext(), NoteActivity.class);
                    i.putExtra("date", today);
                    i.putExtra("dateHT", todayHT);
                    launcherActivity.launch(i);
                }
                else {
                    String[] list = tv_date.getText().toString().split("/",0);
                    String date = list[2] + '-' + list[1] + '-' + list[0];Intent i = new Intent(getContext(), NoteActivity.class);
                    i.putExtra("date", date);
                    i.putExtra("dateHT", tv_date.getText().toString());
                    launcherActivity.launch(i);

            }
            }
        });

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
                            if (LanguageUtils.getCurrentLanguage() == Language.ENGLISH) { tv_date.setText("Today");}
                            else {tv_date.setText("Hôm nay");}
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
                            if(imageViewBack.getVisibility()==View.INVISIBLE)
                                imageViewBack.setVisibility(View.VISIBLE);
                            else imageViewBack.setVisibility(View.INVISIBLE);
                            imageViewBack.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (LanguageUtils.getCurrentLanguage() == Language.ENGLISH) { tv_date.setText("Today");}
                                    else {tv_date.setText("Hôm nay");}
                                    Calendar calendar1 = Calendar.getInstance();
                                    String today = DateFormat.format("yyyy-MM-dd", calendar1).toString();
                                    setBaseGoal();
                                    tv_dinner.setText("");
                                    tv_lunch.setText("");
                                    tv_breakfast.setText("");
                                    tv_Water.setText("");
                                    tv_snack.setText("");
                                    tv_exercise.setText("");

                                    setWater();
                                    setFoodAndExercise(today);
                                    imageViewBack.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });


        return view;
    }

    private void setFoodAndExercise(String date) {
        setBaseGoal();
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
                    tv_remaining.setText(String.valueOf(basegoal + calo - Integer.parseInt(tv_remaining.getText().toString()) ));

                    int totalCalo = Integer.parseInt(tv_remaining.getText().toString());

                    if (basegoal!=0)  {
                        int p = (totalCalo*100)/basegoal;
                        cpi.setProgress(100-p);}

                    if (LanguageUtils.getCurrentLanguage()==Language.ENGLISH) {
                        if (totalCalo < 0) tv_analysis.setText("Overconsumption");
                        else if (totalCalo == 0) tv_analysis.setText("Enough");
                    }
                    else {
                        if (totalCalo < 0) tv_analysis.setText("Đã vượt");
                        else if (totalCalo == 0) tv_analysis.setText("Đã dùng đủ");
                        else tv_analysis.setText("Chưa dùng hết");
                    }
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
                if (tv_date.getText().toString().equals("Today") || tv_date.getText().toString().equals("Hôm nay"))
                {
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
                        basegoal = bmiInfos.get(0).CaloriesNeedToBurn();
                        tv_bmi.setText(String.valueOf(bmiInfos.get(0).CalculatorBMI()));
                        if (LanguageUtils.getCurrentLanguage() == Language.ENGLISH) {
                        tv_foxsay.setText(bmiInfos.get(0).foxSayBMIEng()); }
                        else { tv_foxsay.setText(bmiInfos.get(0).foxSayBMIVie());}
                    }
                    else{
                        tv_baseGoal.setText(String.valueOf(bmiList.get(bmiList.size()-1).CaloriesNeedToBurn()));
                        char[] ch = new char[10];
                        String.valueOf(bmiInfos.get(bmiList.size()-1).CalculatorBMI()).getChars(0,4,ch,0);
                        tv_bmi.setText(String.valueOf(ch));
                        basegoal = bmiInfos.get(bmiList.size()-1).CaloriesNeedToBurn();
                        if (LanguageUtils.getCurrentLanguage() == Language.ENGLISH) {
                        tv_foxsay.setText(bmiList.get(bmiList.size()-1).foxSayBMIEng()); }
                        else {tv_foxsay.setText(bmiList.get(bmiList.size()-1).foxSayBMIVie()); }
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
        if(tv_date.getText().toString().equals("Today") || tv_date.getText().toString().equals("Hôm nay")) {
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

    ActivityResultLauncher<Intent> launcherActivity = registerForActivityResult(
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