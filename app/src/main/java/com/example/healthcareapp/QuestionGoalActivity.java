package com.example.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class QuestionGoalActivity extends AppCompatActivity {
    Button gainWeight, maintainWeight, loseWeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_goal);

        Intent intent = getIntent();
        String _name = intent.getStringExtra("name");
        String _age = intent.getStringExtra("age");
        String _height = intent.getStringExtra("height");
        String _weight = intent.getStringExtra("weight");
        String _sex = intent.getStringExtra("sex");


        gainWeight = findViewById(R.id.gainWeight_btn);
        loseWeight = findViewById(R.id.loseWeight_btn);
        maintainWeight = findViewById(R.id.maintainWeight_btn);

        loseWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionGoalActivity.this, QuestionActivityLevelActivity.class);
                intent.putExtra("name",_name);
                intent.putExtra("age",_age);
                intent.putExtra("height",_height);
                intent.putExtra("weight", _weight);
                intent.putExtra("sex",_sex);
                intent.putExtra("goal","1");
                startActivity(intent);
            }
        });
        maintainWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionGoalActivity.this, QuestionActivityLevelActivity.class);
                intent.putExtra("name",_name);
                intent.putExtra("age",_age);
                intent.putExtra("height",_height);
                intent.putExtra("weight", _weight);
                intent.putExtra("sex",_sex);
                intent.putExtra("goal","2");
                startActivity(intent);
            }
        });

         gainWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionGoalActivity.this, QuestionActivityLevelActivity.class);
                intent.putExtra("name",_name);
                intent.putExtra("age",_age);
                intent.putExtra("height",_height);
                intent.putExtra("weight", _weight);
                intent.putExtra("sex",_sex);
                intent.putExtra("goal","3");
                startActivity(intent);
            }
        });
    }
}