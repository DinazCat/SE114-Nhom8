package com.example.healthcareapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.HashMap;

public class HomeFragment extends Fragment {
    private TextView tv_weeklyGoal;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    CollectionReference bmiRef = FirebaseFirestore.getInstance().collection("bmi");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        tv_weeklyGoal = view.findViewById(R.id.weeklyGoal_tv);
        bmiRef.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        bmiInfo bmi = document.toObject(bmiInfo.class);
                        tv_weeklyGoal.setText(String.valueOf(bmi.CaloriesNeedToBurn()));
                    }
                }
            }
        });

        return view;
    }
}