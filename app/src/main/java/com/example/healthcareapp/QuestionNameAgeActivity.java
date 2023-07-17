package com.example.healthcareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class QuestionNameAgeActivity extends AppCompatActivity {
    private ConstraintLayout next_btn;
    FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();

    EditText editTextName, editTextAge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_name_age);
        next_btn=findViewById(R.id.next_btn);
        editTextName = findViewById(R.id.profileName);
        editTextAge = findViewById(R.id.profileAge);
        editTextName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextName.getText().toString().trim().isEmpty() || editTextAge.getText().toString().trim().isEmpty()) {
                    Toast.makeText(QuestionNameAgeActivity.this, "Input cannot be blank", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (Integer.parseInt(editTextAge.getText().toString()) <= 0) {
                        Toast.makeText(QuestionNameAgeActivity.this, "Age must be a positive integer", Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent1 = new Intent(QuestionNameAgeActivity.this, QuestionWeightHeightActivity.class);
                        intent1.putExtra("name", editTextName.getText().toString());
                        intent1.putExtra("age", String.valueOf(Integer.parseInt(editTextAge.getText().toString())));
                        startActivity(intent1);
                    }
                }
            }
        });
    }
}