package com.example.healthcareapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.healthcareapp.Model.bmiInfo;
import com.example.healthcareapp.Model.food;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChangeBmiAndGoal extends AppCompatActivity {
    String uid = FirebaseAuth.getInstance().getUid();
    EditText edtAge, edtWeight, edtHeight;
    TextView tvWeeklyGoal;
    ImageButton btnBack;
    Spinner spnGoal, spnWeeklyGoal, spnActivityLevel, spnSex;
    Context context;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_bmi_and_goal);
        context = this;//
        btnBack = findViewById(R.id.back);
        edtAge = findViewById(R.id.age);
        edtHeight = findViewById(R.id.height);
        edtWeight = findViewById(R.id.weight);
        spnSex = findViewById(R.id.sex);
        spnGoal = findViewById(R.id.goal);
        spnActivityLevel = findViewById(R.id.activityLevel);
        spnWeeklyGoal = findViewById(R.id.spn_weeklyGoal);
        tvWeeklyGoal = findViewById(R.id.tv_weeklyGoal);
        btnSave = findViewById(R.id.save);
        showLastBmiAndGoal();

        ArrayAdapter<CharSequence> adapterGoal = ArrayAdapter.createFromResource(this, R.array.goals, android.R.layout.simple_spinner_item);
        adapterGoal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGoal.setAdapter(adapterGoal);

        ArrayAdapter<CharSequence> adapterActivityLevel = ArrayAdapter.createFromResource(this, R.array.activityLevels, android.R.layout.simple_spinner_item);
        adapterActivityLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnActivityLevel.setAdapter(adapterActivityLevel);
        ArrayAdapter<CharSequence> adapterSex = ArrayAdapter.createFromResource(this, R.array.sex, android.R.layout.simple_spinner_item);
        adapterSex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSex.setAdapter(adapterSex);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeBmiAndGoal.this, MainActivity.class);

                setResult(RESULT_OK, intent);
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("bmiDiary").child(uid);
                Query query = ref.orderByChild("time").limitToLast(1);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<bmiInfo> bmiInfos = new ArrayList<>();
                        String keyId="";
                        for(DataSnapshot ds : snapshot.getChildren()){
                            bmiInfo bmiInfo = ds.getValue(bmiInfo.class);
                            bmiInfos.add(bmiInfo);
                            keyId = ds.getKey();
                        }

                        Calendar calendar = Calendar.getInstance();
                        String today = DateFormat.format("yyyy/MM/dd", calendar).toString();

                        calendar.setTimeInMillis(bmiInfos.get(0).time);
                        String date= DateFormat.format("yyyy/MM/dd", calendar).toString();
                        if (date.equals(today)) {
                            ref.child(keyId).child("time").setValue(System.currentTimeMillis());
                            ref.child(keyId).child("age").setValue(edtAge.getText().toString());
                            ref.child(keyId).child("weight").setValue(edtWeight.getText().toString());
                            ref.child(keyId).child("height").setValue(edtHeight.getText().toString());
                            ref.child(keyId).child("goal").setValue(spnGoal.getSelectedItem().toString());
                            ref.child(keyId).child("sex").setValue(spnSex.getSelectedItem().toString());
                            //sex
                            ref.child(keyId).child("activityLevel").setValue(spnActivityLevel.getSelectedItem().toString());
                            if (spnWeeklyGoal.getVisibility()!=View.GONE){
                                ref.child(keyId).child("weeklyGoal").setValue(spnWeeklyGoal.getSelectedItem().toString());
                            }
                            else{
                                ref.child(keyId).child("weeklyGoal").setValue("0");
                            }
                        }
                        else{
                            bmiInfo bmi_info = new bmiInfo();
                            bmi_info.userID = uid;
                            bmi_info.age = edtAge.getText().toString();
                            bmi_info.height = edtHeight.getText().toString();
                            bmi_info.weight = edtWeight.getText().toString();
                            bmi_info.sex = spnSex.getSelectedItem().toString();
                            bmi_info.goal = spnGoal.getSelectedItem().toString();
                            bmi_info.weeklyGoal = "";
                            if (spnWeeklyGoal.getVisibility()!=View.GONE){
                                bmi_info.weeklyGoal = spnActivityLevel.getSelectedItem().toString();
                            }
                            else{
                                bmi_info.weeklyGoal = "0";
                            }
                            bmi_info.activityLevel = "Very active";
                            bmi_info.time = System.currentTimeMillis();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("bmiDiary");
                            String key = databaseReference.push().getKey();
                            databaseReference.child(uid).child(key).setValue(bmi_info);
                        }
                        Intent intent = new Intent(ChangeBmiAndGoal.this, MainActivity.class);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "Error:" + error.getMessage());
                    }
                });
            }
        });

    }
    public void showLastBmiAndGoal(){
        Query query = FirebaseDatabase.getInstance().getReference("bmiDiary").child(uid).orderByChild("time").limitToLast(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<bmiInfo> bmiInfos = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()){
                    bmiInfo bmiInfo = ds.getValue(bmiInfo.class);
                    bmiInfos.add(bmiInfo);
                }
                spnGoal.setPrompt(bmiInfos.get(0).goal);
                edtAge.setText(bmiInfos.get(0).age);
                edtWeight.setText(bmiInfos.get(0).weight);
                edtHeight.setText(bmiInfos.get(0).height);
                ArrayAdapter<String> array_spnGoal=(ArrayAdapter<String>)spnGoal.getAdapter();
                spnGoal.setSelection(array_spnGoal.getPosition(bmiInfos.get(0).goal));
                ArrayAdapter<String> array_spnSex=(ArrayAdapter<String>)spnSex.getAdapter();
                spnSex.setSelection(array_spnSex.getPosition(bmiInfos.get(0).sex));
                
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
                        ArrayAdapter<String> array_spnWeeklyGoal=(ArrayAdapter<String>)spnWeeklyGoal.getAdapter();
                        spnWeeklyGoal.setSelection(array_spnWeeklyGoal.getPosition("Gain " + bmiInfos.get(0).weeklyGoal + " kg per week"));
                    }
                    else{
                        ArrayAdapter<CharSequence> adapterWeeklyGoal = ArrayAdapter.createFromResource(context, R.array.lose_weeklyGoals, android.R.layout.simple_spinner_item);
                        adapterWeeklyGoal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnWeeklyGoal.setAdapter(adapterWeeklyGoal);
                        ArrayAdapter<String> array_spnWeeklyGoal=(ArrayAdapter<String>)spnWeeklyGoal.getAdapter();
                        spnWeeklyGoal.setSelection(array_spnWeeklyGoal.getPosition("Lose " + bmiInfos.get(0).weeklyGoal + " kg per week"));
                    }
                }
                ArrayAdapter<String> array_spnActivityLevel=(ArrayAdapter<String>)spnActivityLevel.getAdapter();
                spnActivityLevel.setSelection(array_spnActivityLevel.getPosition(bmiInfos.get(0).activityLevel));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Error:" + error.getMessage());
            }
        });
    }

}
