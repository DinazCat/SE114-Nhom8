package com.example.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
                if (editTextHeight.getText().toString().trim().isEmpty() || editTextWeight.getText().toString().trim().isEmpty()) {
                    Toast.makeText(QuestionWeightHeightActivity.this, "Input cannot be blank", Toast.LENGTH_SHORT).show();

                }
                else{
                    if(Integer.parseInt(editTextHeight.getText().toString()) <= 0
                            || Integer.parseInt(editTextWeight.getText().toString()) <= 0){
                        Toast.makeText(QuestionWeightHeightActivity.this, "Weight and height must be positive integer", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent intent = new Intent(QuestionWeightHeightActivity.this, QuestionSexActivity.class);
                        intent.putExtra("name", _name);
                        intent.putExtra("age", _age);
                        intent.putExtra("height", String.valueOf(Integer.parseInt(editTextHeight.getText().toString())));
                        intent.putExtra("weight", String.valueOf(Integer.parseInt(editTextWeight.getText().toString())));
                        startActivity(intent);
                    }
                }
            }
        });
    }
}