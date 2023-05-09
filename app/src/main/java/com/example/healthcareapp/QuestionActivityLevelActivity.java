package com.example.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class QuestionActivityLevelActivity extends AppCompatActivity {
    Button notVeryActive, active, lightlyActive, veryActive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_level);

        Intent intent = getIntent();
        String _name = intent.getStringExtra("name");
        String _age = intent.getStringExtra("age");
        String _height = intent.getStringExtra("height");
        String _weight = intent.getStringExtra("weight");
        String _sex = intent.getStringExtra("sex");
        String _goal = intent.getStringExtra("goal");

        notVeryActive = findViewById(R.id.notVeryActive_btn);
        lightlyActive = findViewById(R.id.lightlyActive_btn);
        active = findViewById(R.id.active_btn);
        veryActive = findViewById(R.id.veryActive_btn);

        notVeryActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Intent intent = new Intent(QuestionActivityLevelActivity.this, MainActivity.class);
                HashMap<Object,String> bmiHashMap = new HashMap<>();
                bmiHashMap.put("userID", uid);
                bmiHashMap.put("userName", _name);
                bmiHashMap.put("age", _age);
                bmiHashMap.put("height", _height);
                bmiHashMap.put("weight", _weight);
                bmiHashMap.put("sex", _sex);
                bmiHashMap.put("goal", _goal);
                bmiHashMap.put("goalKg", "0");
                bmiHashMap.put("activityLevel", "Not very active");
                FirebaseFirestore firebaseFireStore = FirebaseFirestore.getInstance();
                firebaseFireStore.collection("bmi").document(uid).set(bmiHashMap);
                startActivity(intent);
            }
        });
        lightlyActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Intent intent = new Intent(QuestionActivityLevelActivity.this, MainActivity.class);
                HashMap<Object,String> bmiHashMap = new HashMap<>();
                bmiHashMap.put("userID", uid);
                bmiHashMap.put("userName", _name);
                bmiHashMap.put("age", _age);
                bmiHashMap.put("height", _height);
                bmiHashMap.put("weight", _weight);
                bmiHashMap.put("sex", _sex);
                bmiHashMap.put("goal", _goal);
                bmiHashMap.put("goalKg", "0");
                bmiHashMap.put("activityLevel", "Lightly active");
                FirebaseFirestore firebaseFireStore = FirebaseFirestore.getInstance();
                firebaseFireStore.collection("bmi").document(uid).set(bmiHashMap);

                startActivity(intent);
            }
        });

        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Intent intent = new Intent(QuestionActivityLevelActivity.this, MainActivity.class);
                HashMap<Object,String> bmiHashMap = new HashMap<>();
                bmiHashMap.put("userID", uid);
                bmiHashMap.put("userName", _name);
                bmiHashMap.put("age", _age);
                bmiHashMap.put("height", _height);
                bmiHashMap.put("weight", _weight);
                bmiHashMap.put("sex", _sex);
                bmiHashMap.put("goal", _goal);
                bmiHashMap.put("goalKg", "0");
                bmiHashMap.put("activityLevel", "Active");

                FirebaseFirestore firebaseFireStore = FirebaseFirestore.getInstance();
                firebaseFireStore.collection("bmi").document(uid).set(bmiHashMap);

                startActivity(intent);
            }
        });
        veryActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Intent intent = new Intent(QuestionActivityLevelActivity.this, MainActivity.class);
                HashMap<Object,String> bmiHashMap = new HashMap<>();
                bmiHashMap.put("userID", uid);
                bmiHashMap.put("userName", _name);
                bmiHashMap.put("age", _age);
                bmiHashMap.put("height", _height);
                bmiHashMap.put("weight", _weight);
                bmiHashMap.put("sex", _sex);
                bmiHashMap.put("goal", _goal);
                bmiHashMap.put("goalKg", "0");
                bmiHashMap.put("activityLevel", "Very Active");

                FirebaseFirestore firebaseFireStore = FirebaseFirestore.getInstance();
                firebaseFireStore.collection("bmi").document(uid).set(bmiHashMap);

                startActivity(intent);
            }
        });
    }
}
