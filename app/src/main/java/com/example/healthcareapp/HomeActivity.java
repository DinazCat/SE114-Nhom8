package com.example.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private Button logoutButton;
    GoogleSignInOptions gOptions;
    GoogleSignInClient gClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();

        logoutButton = findViewById(R.id.logoutbtn);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                gClient = GoogleSignIn.getClient(HomeActivity.this, gOptions);
                if (AccessToken.getCurrentAccessToken() != null && auth.getCurrentUser() != null) {
                    auth.signOut();
                    LoginManager.getInstance().logOut();
                }
                else if (gClient != null && auth.getCurrentUser() != null) {
                    auth.signOut();
                    gClient.signOut();
                }
                else if (auth.getCurrentUser() != null) {
                    auth.signOut();
                }
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });



//        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(this);
//        if (gAccount != null){
//            String name = gAccount.getDisplayName();
//        }
    }
}