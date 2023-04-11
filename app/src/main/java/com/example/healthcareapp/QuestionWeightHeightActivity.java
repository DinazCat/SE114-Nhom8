package com.example.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class QuestionWeightHeightActivity extends AppCompatActivity {
    private ConstraintLayout next_btn;
    EditText editTextHeight, editTextWeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_weight_height);

        Intent intent = getIntent();
        String _name = intent.getStringExtra("name");
        String _age = intent.getStringExtra("age");

        editTextHeight = findViewById(R.id.profileHeight);
        editTextWeight = findViewById(R.id.profileWeight);
        next_btn = findViewById(R.id.next_btn2);

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(QuestionWeightHeightActivity.this, QuestionSexActivityy.class);
               intent.putExtra("name",_name);
               intent.putExtra("age",_age);
               intent.putExtra("height",editTextHeight.getText());
               intent.putExtra("weight", editTextWeight.getText());
               startActivity(intent);
            }
        });
    }
}