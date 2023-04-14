package com.example.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    TextView textView, textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
       // get Information of User
        Intent intent = getIntent();
        String _name = intent.getStringExtra("name");
        String _age = intent.getStringExtra("age");
        String _height = intent.getStringExtra("height");
        String _weight = intent.getStringExtra("weight");
        String _sex = intent.getStringExtra("sex");
        String _goal = intent.getStringExtra("goal");
        String _level = intent.getStringExtra("level");

        textView = findViewById(R.id.textViewBMI);
        textView2 = findViewById(R.id.textViewBMI2);
        if (Integer.parseInt(_level)==1)
        textView.setText("so mot");
        else
        if (Integer.parseInt(_level)==2)
            textView.setText("so hai");

        if (Integer.parseInt(_sex)==1)
            textView2.setText("Nam");
        else textView2.setText("Nu");

       /* auth = FirebaseAuth.getInstance();

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

        */
    }


}