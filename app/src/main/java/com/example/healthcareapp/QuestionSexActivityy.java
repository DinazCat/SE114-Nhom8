package com.example.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class QuestionSexActivityy extends AppCompatActivity {
    Button malebtn, femalebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_sex_activityy);

        Intent intent = getIntent();
        String _name = intent.getStringExtra("name");
        String _age = intent.getStringExtra("age");
        String _height = intent.getStringExtra("height");
        String _weight = intent.getStringExtra("weight");

        malebtn = findViewById(R.id.male_btn);
        femalebtn = findViewById(R.id.female_btn);

        malebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionSexActivityy.this, QuestionGoalActivity.class);
                intent.putExtra("name",_name);
                intent.putExtra("age",_age);
                intent.putExtra("height",_height);
                intent.putExtra("weight", _weight);
                intent.putExtra("sex","1");
                startActivity(intent);
            }
        });

        femalebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionSexActivityy.this, QuestionGoalActivity.class);
                intent.putExtra("name",_name);
                intent.putExtra("age",_age);
                intent.putExtra("height",_height);
                intent.putExtra("weight", _weight);
                intent.putExtra("sex","2");
                startActivity(intent);
            }
        });
    }
}