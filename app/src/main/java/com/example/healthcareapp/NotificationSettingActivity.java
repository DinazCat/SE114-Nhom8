package com.example.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class NotificationSettingActivity extends AppCompatActivity {
    CheckBox cbfollow, cblike, cbcomment, cbnewpost;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setting);

        ImageButton btnBack = findViewById(R.id.back);
        cbfollow = findViewById(R.id.cb_follow);
        cblike = findViewById(R.id.cb_like);
        cbcomment = findViewById(R.id.cb_comment);
        cbnewpost = findViewById(R.id.cb_newpost);

        SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        cbfollow.setChecked(preferences.getBoolean("followCheckbox", false));
        cblike.setChecked(preferences.getBoolean("likeCheckbox", false));
        cbcomment.setChecked(preferences.getBoolean("commentCheckbox", false));
        cbnewpost.setChecked(preferences.getBoolean("newpostCheckbox", false));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cbfollow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("followCheckbox", b);
                editor.apply();
                if(b) {
                    database.getReference().child("notificationSetting")
                            .child(FirebaseAuth.getInstance().getUid()).child("follow").setValue(b);
                } else {
                    database.getReference().child("notificationSetting")
                            .child(FirebaseAuth.getInstance().getUid()).child("follow").removeValue();
                }
            }
        });
        cblike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("likeCheckbox", b);
                editor.apply();
                if(b) {
                    database.getReference().child("notificationSetting")
                            .child(FirebaseAuth.getInstance().getUid()).child("like").setValue(b);
                } else {
                    database.getReference().child("notificationSetting")
                            .child(FirebaseAuth.getInstance().getUid()).child("like").removeValue();
                }
            }
        });
        cbcomment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("commentCheckbox", b);
                editor.apply();
                if(b) {
                    database.getReference().child("notificationSetting")
                            .child(FirebaseAuth.getInstance().getUid()).child("comment").setValue(b);
                } else {
                    database.getReference().child("notificationSetting")
                            .child(FirebaseAuth.getInstance().getUid()).child("comment").removeValue();
                }
            }
        });
        cbnewpost.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("newpostCheckbox", b);
                editor.apply();
                if(b) {
                    database.getReference().child("notificationSetting")
                            .child(FirebaseAuth.getInstance().getUid()).child("newpost").setValue(b);
                } else {
                    database.getReference().child("notificationSetting")
                            .child(FirebaseAuth.getInstance().getUid()).child("newpost").removeValue();
                }
            }
        });
    }
}