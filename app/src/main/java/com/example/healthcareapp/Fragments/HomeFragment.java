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
import android.widget.TextView;

import com.example.healthcareapp.Model.bmiInfo;
import com.example.healthcareapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {
    private TextView tv_date, tv_baseGoal, tv_Water;
    private
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //tv_weeklyGoal = view.findViewById(R.id.weeklyGoal_tv);
        tv_date = view.findViewById(R.id.date);
        tv_baseGoal = view.findViewById(R.id.baseGoal);
        tv_Water = view.findViewById(R.id.water);

        //bmi thay đổi theo ngày khi sửa
        setBaseGoal();
        setWater();

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
                            setBaseGoal();
                            setWater();
                        }
                        else{
                            calendar.set(year, month, dayOfMonth);
                            tv_date.setText(DateFormat.format("dd/MM/yyyy", calendar).toString());
                            setBaseGoal();
                            setWater();
                        }
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        return view;
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