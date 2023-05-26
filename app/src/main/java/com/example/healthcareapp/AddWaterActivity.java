package com.example.healthcareapp;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.CellIdentity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.healthcareapp.Fragments.AddFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class AddWaterActivity extends AppCompatActivity {
    ImageButton btnBack;
    Button btnSave;
    FirebaseDatabase firebaseDatabase;
    EditText edtWater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_water);
        btnBack = findViewById(R.id.back);
        edtWater = findViewById(R.id.water);
        btnSave = findViewById(R.id.save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddWaterActivity.this, MainActivity.class);
                //you can return AddFragment or MainActivity if you want
                if (edtWater.getText().toString().trim().equals("")){
                    Toast.makeText(AddWaterActivity.this, "Input cannot be blank", Toast.LENGTH_LONG).show();
                }
                else {
                    String uid = FirebaseAuth.getInstance().getUid();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("water");
                    HashMap<Object, String> hashMap = new HashMap<>();
                    hashMap.put("waterAmount", edtWater.getText().toString());
                    hashMap.put("time", DateFormat.format("yyyy/MM/dd", Calendar.getInstance()).toString());
                    String key = databaseReference.push().getKey();
                    databaseReference.child(uid).child(key).setValue(hashMap);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddWaterActivity.this, MainActivity.class);

                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
