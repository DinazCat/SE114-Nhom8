package com.example.healthcareapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcareapp.Model.bmiInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChangeGoalActivity extends AppCompatActivity {
    String uid = FirebaseAuth.getInstance().getUid();
    TextView tvWeeklyGoal;
    ImageButton btnBack;
    Spinner spnGoal, spnWeeklyGoal, spnActivityLevel;
    String age, height, weight, sex;
    Context context;
    Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_goal);
        context = this;
        btnBack = findViewById(R.id.back);
        spnGoal = findViewById(R.id.goal);
        spnActivityLevel = findViewById(R.id.activityLevel);
        spnWeeklyGoal = findViewById(R.id.spn_weeklyGoal);
        tvWeeklyGoal = findViewById(R.id.tv_weeklyGoal);
        btnSave = findViewById(R.id.save);
        showLastGoal();
        ArrayAdapter<CharSequence> adapterGoal = ArrayAdapter.createFromResource(this, R.array.goals, android.R.layout.simple_spinner_item);
        adapterGoal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGoal.setAdapter(adapterGoal);

        ArrayAdapter<CharSequence> adapterActivityLevel = ArrayAdapter.createFromResource(this, R.array.activityLevels, android.R.layout.simple_spinner_item);
        adapterActivityLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnActivityLevel.setAdapter(adapterActivityLevel);
        spnGoal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<CharSequence> adapterWeeklyGoal;
                switch(spnGoal.getSelectedItemPosition()){
                    case 0:
                        tvWeeklyGoal.setVisibility(View.GONE);
                        spnWeeklyGoal.setVisibility(View.GONE);
                        break;
                    case 1:
                        tvWeeklyGoal.setVisibility(View.VISIBLE);
                        spnWeeklyGoal.setVisibility(View.VISIBLE);
                        adapterWeeklyGoal = ArrayAdapter.createFromResource(context, R.array.gain_weeklyGoals, android.R.layout.simple_spinner_item);
                        adapterWeeklyGoal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnWeeklyGoal.setAdapter(adapterWeeklyGoal);
                        break;
                    case 2:
                        tvWeeklyGoal.setVisibility(View.VISIBLE);
                        spnWeeklyGoal.setVisibility(View.VISIBLE);
                        adapterWeeklyGoal = ArrayAdapter.createFromResource(context, R.array.lose_weeklyGoals, android.R.layout.simple_spinner_item);
                        adapterWeeklyGoal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnWeeklyGoal.setAdapter(adapterWeeklyGoal);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeGoalActivity.this, MainActivity.class);

                setResult(RESULT_OK, intent);
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bmiInfo bmi_info = new bmiInfo();
                bmi_info.userID = uid;
                bmi_info.age = age;
                bmi_info.height = height;
                bmi_info.weight = weight;
                bmi_info.sex = sex;
                switch (spnGoal.getSelectedItemPosition()){
                    case 0:
                        bmi_info.goal = "Maintaint weight";
                        break;
                    case 1:
                        bmi_info.goal = "Gain weight";
                        break;
                    case 2:
                        bmi_info.goal = "Lose weight";
                        break;
                }
                if (spnWeeklyGoal.getVisibility()!=View.GONE){
                    int index = spnWeeklyGoal.getSelectedItemPosition();
                    switch (index){
                        case 0:
                            bmi_info.weeklyGoal = "0.25";
                            break;
                        case 1:
                            bmi_info.weeklyGoal = "0.5";
                            break;
                        case 2:
                            bmi_info.weeklyGoal = "1";
                            break;
                    }
                }
                else{
                    bmi_info.weeklyGoal = "0";
                }
                bmi_info.activityLevel = spnActivityLevel.getSelectedItem().toString();
                switch (spnActivityLevel.getSelectedItemPosition()) {
                    case 0:
                        bmi_info.activityLevel = "Not very active";
                        break;
                    case 1:
                        bmi_info.activityLevel = "Lightly active";
                        break;
                    case 2:
                        bmi_info.activityLevel = "Moderate";
                        break;
                    case 3:
                        bmi_info.activityLevel = "Active";
                        break;
                    case 4:
                        bmi_info.activityLevel = "Very active";
                        break;
                }
                bmi_info.time = System.currentTimeMillis();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("bmiDiary");
                String key = databaseReference.push().getKey();
                databaseReference.child(uid).child(key).setValue(bmi_info);
                Toast.makeText(ChangeGoalActivity.this, "Change goal successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void showLastGoal(){
        Query query = FirebaseDatabase.getInstance().getReference("bmiDiary").child(uid).orderByChild("time").limitToLast(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<bmiInfo> bmiInfos = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()){
                    bmiInfo bmiInfo = ds.getValue(bmiInfo.class);
                    bmiInfos.add(bmiInfo);
                }
                age = bmiInfos.get(0).age;
                weight = bmiInfos.get(0).weight;
                height = bmiInfos.get(0).height;
                switch (bmiInfos.get(0).goal) {
                    case "Maintain weight":
                        spnGoal.setSelection(0);
                        break;
                    case "Gain weight":
                        spnGoal.setSelection(1);
                        break;
                    case "Lose weihgt":
                        spnGoal.setSelection(2);
                        break;
                }
                sex = bmiInfos.get(0).sex;

                if (bmiInfos.get(0).weeklyGoal.equals("0")){
                    tvWeeklyGoal.setVisibility(View.GONE);
                    spnWeeklyGoal.setVisibility(View.GONE);
                }
                else{
                    tvWeeklyGoal.setVisibility(View.VISIBLE);
                    spnWeeklyGoal.setVisibility(View.VISIBLE);
                    if (bmiInfos.get(0).goal.equals("Gain weight")){
                        ArrayAdapter<CharSequence> adapterWeeklyGoal = ArrayAdapter.createFromResource(context, R.array.gain_weeklyGoals, android.R.layout.simple_spinner_item);
                        adapterWeeklyGoal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnWeeklyGoal.setAdapter(adapterWeeklyGoal);
                        switch (bmiInfos.get(0).weeklyGoal) {
                            case "0.25":
                                spnWeeklyGoal.setSelection(0);
                                break;
                            case "0.5":
                                spnWeeklyGoal.setSelection(1);
                                break;
                            case "1":
                                spnWeeklyGoal.setSelection(2);
                                break;
                        }
                    }
                    else{
                        ArrayAdapter<CharSequence> adapterWeeklyGoal = ArrayAdapter.createFromResource(context, R.array.lose_weeklyGoals, android.R.layout.simple_spinner_item);
                        adapterWeeklyGoal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnWeeklyGoal.setAdapter(adapterWeeklyGoal);
                        switch (bmiInfos.get(0).weeklyGoal) {
                            case "0.25":
                                spnWeeklyGoal.setSelection(0);
                                break;
                            case "0.5":
                                spnWeeklyGoal.setSelection(1);
                                break;
                            case "1":
                                spnWeeklyGoal.setSelection(2);
                                break;
                        }
                    }
                }
                switch (bmiInfos.get(0).activityLevel) {
                    case "Not very active":
                        spnActivityLevel.setSelection(0);
                        break;
                    case "Lightly active":
                        spnActivityLevel.setSelection(1);
                        break;
                    case "Moderate":
                        spnActivityLevel.setSelection(2);
                        break;
                    case "Active":
                        spnActivityLevel.setSelection(3);
                        break;
                    case "Very active":
                        spnActivityLevel.setSelection(4);
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Error:" + error.getMessage());
            }
        });
    }
}