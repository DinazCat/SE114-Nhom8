package com.example.healthcareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText email, password, confirmpassword, username;
    private Button signupButton;
    private TextView loginRedirectText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmpassword);
        username = findViewById(R.id.name);
        signupButton = findViewById(R.id.signupbtn);
        loginRedirectText = findViewById(R.id.loginRedirect);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = email.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String confirmpass = confirmpassword.getText().toString().trim();
                String name = username.getText().toString().trim();
                boolean flag = false;
                if(name.isEmpty()){
                    username.setError("Name cannot be empty");
                    flag = true;
                }
                if (user.isEmpty()){
                    email.setError("Email cannot be empty");
                    flag = true;
                }
                if (pass.isEmpty()){
                    password.setError("Password cannot be empty");
                    flag = true;
                }
                if(!pass.equals(confirmpass)){
                    confirmpassword.setError("Password confirmation does not match!");
                    flag = true;
                }
                if(!flag){
                    auth.createUserWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FirebaseUser user = auth.getCurrentUser();
                                String email = user.getEmail();
                                String uid = user.getUid();
                                String name = username.getText().toString().trim();
                                HashMap<Object,String> hashMap = new HashMap<>();
                                hashMap.put("email", email);
                                hashMap.put("id", uid);
                                hashMap.put("name", name);
                                hashMap.put("img", "");
                                hashMap.put("phone", "");
                                hashMap.put("city", "");
                                hashMap.put("country", "");
                                hashMap.put("about", "");

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference = database.getReference("users");
                                databaseReference.child(uid).setValue(hashMap);

                                Toast.makeText(SignupActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            } else {
                                Toast.makeText(SignupActivity.this, "SignUp Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
    }
}