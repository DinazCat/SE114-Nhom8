package com.example.healthcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class QuestionGoalWeightActivity extends AppCompatActivity {
    Button btn_025perweek, btn_05perweek, btn1perweek;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_goal_weight);
        Intent intent = getIntent();
        String _name = intent.getStringExtra("name");
        String _age = intent.getStringExtra("age");
        String _height = intent.getStringExtra("height");
        String _weight = intent.getStringExtra("weight");
        String _sex = intent.getStringExtra("sex");
        String _goal = intent.getStringExtra("goal");
        btn_025perweek = findViewById(R.id.btn0_25perweek);
        btn_05perweek = findViewById(R.id.btn0_5perweek);
        btn1perweek = findViewById(R.id.btn1perweek);
        if (_goal.equals("Gain weight")){
            btn_025perweek.setText("Gain 0.25 kg per week");
            btn_05perweek.setText("Gain 0.5 kg per week");
            btn1perweek.setText("Gain 1 kg per week");
        }
        btn_025perweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionGoalWeightActivity.this, QuestionActivityLevelActivity.class);
                intent.putExtra("name",_name);
                intent.putExtra("age",_age);
                intent.putExtra("height",_height);
                intent.putExtra("weight", _weight);
                intent.putExtra("sex",_sex);
                intent.putExtra("goal",_goal);
                intent.putExtra("weeklyGoal", "0.25");
                startActivity(intent);
            }
        });
        btn_05perweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionGoalWeightActivity.this, QuestionActivityLevelActivity.class);
                intent.putExtra("name",_name);
                intent.putExtra("age",_age);
                intent.putExtra("height",_height);
                intent.putExtra("weight", _weight);
                intent.putExtra("sex",_sex);
                intent.putExtra("goal",_goal);
                intent.putExtra("weeklyGoal", "0.5");
                startActivity(intent);
            }
        });

        btn1perweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionGoalWeightActivity.this, QuestionActivityLevelActivity.class);
                intent.putExtra("name",_name);
                intent.putExtra("age",_age);
                intent.putExtra("height",_height);
                intent.putExtra("weight", _weight);
                intent.putExtra("sex",_sex);
                intent.putExtra("goal",_goal);
                intent.putExtra("weeklyGoal", "1");
                startActivity(intent);
            }
        });
    }
}
