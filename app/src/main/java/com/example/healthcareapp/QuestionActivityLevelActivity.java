package com.example.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
                Intent intent = new Intent(QuestionActivityLevelActivity.this, MainActivity2.class);
                intent.putExtra("name",_name);
                intent.putExtra("age",_age);
                intent.putExtra("height",_height);
                intent.putExtra("weight", _weight);
                intent.putExtra("sex",_sex);
                intent.putExtra("goal",_goal);
                intent.putExtra("level","1");
                startActivity(intent);
            }
        });
        lightlyActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionActivityLevelActivity.this, MainActivity2.class);
                intent.putExtra("name",_name);
                intent.putExtra("age",_age);
                intent.putExtra("height",_height);
                intent.putExtra("weight", _weight);
                intent.putExtra("sex",_sex);
                intent.putExtra("goal",_goal);
                intent.putExtra("level","2");
                startActivity(intent);
            }
        });

        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionActivityLevelActivity.this, MainActivity2.class);
                intent.putExtra("name",_name);
                intent.putExtra("age",_age);
                intent.putExtra("height",_height);
                intent.putExtra("weight", _weight);
                intent.putExtra("sex",_sex);
                intent.putExtra("goal",_goal);
                intent.putExtra("level","3");
                startActivity(intent);
            }
        });
        veryActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionActivityLevelActivity.this, MainActivity2.class);
                intent.putExtra("name",_name);
                intent.putExtra("age",_age);
                intent.putExtra("height",_height);
                intent.putExtra("weight", _weight);
                intent.putExtra("sex",_sex);
                intent.putExtra("goal",_goal);
                intent.putExtra("level","4");
                startActivity(intent);
            }
        });
    }
}