package com.example.healthcareapp.Fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.healthcareapp.LoginActivity;
import com.example.healthcareapp.MainActivity;
import com.example.healthcareapp.Model.bmiInfo;
import com.example.healthcareapp.QuestionNameAgeActivity;
import com.example.healthcareapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;

public class HomeFragment extends Fragment {
    private TextView tv_weeklyGoal, tv_date, tv_calorieBudget;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    CollectionReference bmiRef = FirebaseFirestore.getInstance().collection("bmi");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //tv_weeklyGoal = view.findViewById(R.id.weeklyGoal_tv);
        tv_date = view.findViewById(R.id.date);
        tv_calorieBudget = view.findViewById(R.id.calorieBudget);
        bmiRef.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        bmiInfo bmi = document.toObject(bmiInfo.class);
                        //tv_weeklyGoal.setText(String.valueOf(bmi.CaloriesNeedToBurn()));
                        tv_calorieBudget.setText(String.valueOf(bmi.CaloriesNeedToBurn()));
                    }
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
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        if (calendar.get(Calendar.DAY_OF_MONTH) == dayOfMonth && calendar.get(Calendar.MONTH) == month && calendar.get(Calendar.YEAR) == year)
                            tv_date.setText("Today");
                        else
                            tv_date.setText(selectedDate);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        return view;
    }

}