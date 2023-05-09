package com.example.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.healthcareapp.Model.bmiInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class QuestionActivityLevelActivity extends AppCompatActivity {
    Button notVeryActive, active, lightlyActive, veryActive, moderate;
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
        String _weeklyGoal = intent.getStringExtra("weeklyGoal");

        notVeryActive = findViewById(R.id.notVeryActive_btn);
        lightlyActive = findViewById(R.id.lightlyActive_btn);
        active = findViewById(R.id.active_btn);
        veryActive = findViewById(R.id.veryActive_btn);
        moderate = findViewById(R.id.moderate_btn);

        notVeryActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Intent intent = new Intent(QuestionActivityLevelActivity.this, MainActivity.class);

                bmiInfo bmi_info = new bmiInfo();
                bmi_info.userID = uid;
                bmi_info.userName = _name;
                bmi_info.age = _age;
                bmi_info.height = _height;
                bmi_info.weight = _weight;
                bmi_info.sex = _sex;
                bmi_info.goal = _goal;
                bmi_info.weeklyGoal = _weeklyGoal;
                bmi_info.activityLevel = "Not very active";

                FirebaseFirestore firebaseFireStore = FirebaseFirestore.getInstance();
                firebaseFireStore.collection("bmi").document(uid).set(bmi_info);

                startActivity(intent);

            }
        });
        moderate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Intent intent = new Intent(QuestionActivityLevelActivity.this, MainActivity.class);
                bmiInfo bmi_info = new bmiInfo();
                bmi_info.userID = uid;
                bmi_info.userName = _name;
                bmi_info.age = _age;
                bmi_info.height = _height;
                bmi_info.weight = _weight;
                bmi_info.sex = _sex;
                bmi_info.goal = _goal;
                bmi_info.weeklyGoal = _weeklyGoal;
                bmi_info.activityLevel = "Moderate";
                FirebaseFirestore firebaseFireStore = FirebaseFirestore.getInstance();
                firebaseFireStore.collection("bmi").document(uid).set(bmi_info);

                startActivity(intent);

            }
        });
        lightlyActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Intent intent = new Intent(QuestionActivityLevelActivity.this, MainActivity.class);
                bmiInfo bmi_info = new bmiInfo();
                bmi_info.userID = uid;
                bmi_info.userName = _name;
                bmi_info.age = _age;
                bmi_info.height = _height;
                bmi_info.weight = _weight;
                bmi_info.sex = _sex;
                bmi_info.goal = _goal;
                bmi_info.weeklyGoal = _weeklyGoal;
                bmi_info.activityLevel = "Lightly active";
                FirebaseFirestore firebaseFireStore = FirebaseFirestore.getInstance();
                firebaseFireStore.collection("bmi").document(uid).set(bmi_info);

                startActivity(intent);
            }
        });

        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Intent intent = new Intent(QuestionActivityLevelActivity.this, MainActivity.class);
                bmiInfo bmi_info = new bmiInfo();
                bmi_info.userID = uid;
                bmi_info.userName = _name;
                bmi_info.age = _age;
                bmi_info.height = _height;
                bmi_info.weight = _weight;
                bmi_info.sex = _sex;
                bmi_info.goal = _goal;
                bmi_info.weeklyGoal = _weeklyGoal;
                bmi_info.activityLevel = "Active";
                FirebaseFirestore firebaseFireStore = FirebaseFirestore.getInstance();
                firebaseFireStore.collection("bmi").document(uid).set(bmi_info);

                startActivity(intent);
            }
        });
        veryActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Intent intent = new Intent(QuestionActivityLevelActivity.this, MainActivity.class);
                bmiInfo bmi_info = new bmiInfo();
                bmi_info.userID = uid;
                bmi_info.userName = _name;
                bmi_info.age = _age;
                bmi_info.height = _height;
                bmi_info.weight = _weight;
                bmi_info.sex = _sex;
                bmi_info.goal = _goal;
                bmi_info.weeklyGoal = _weeklyGoal;
                bmi_info.activityLevel = "Very active";
                FirebaseFirestore firebaseFireStore = FirebaseFirestore.getInstance();
                firebaseFireStore.collection("bmi").document(uid).set(bmi_info);
                startActivity(intent);
            }
        });
    }
}
