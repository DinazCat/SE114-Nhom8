package com.example.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class QuestionNameAgeActivity extends AppCompatActivity {
    private ConstraintLayout next_btn;
    EditText editTextName, editTextAge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_name_age);

        next_btn=findViewById(R.id.next_btn);
        editTextName = findViewById(R.id.profileName);
        editTextAge = findViewById(R.id.profileAge);
        Intent intent = new Intent(QuestionNameAgeActivity.this, HomeActivity.class);
        intent.putExtra("name",editTextName.getText());

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(QuestionNameAgeActivity.this, QuestionWeightHeightActivity.class);
                intent1.putExtra("name",editTextName.getText());
                intent1.putExtra("age", editTextAge.getText());
                startActivity(intent1);
            }
        });
    }
}