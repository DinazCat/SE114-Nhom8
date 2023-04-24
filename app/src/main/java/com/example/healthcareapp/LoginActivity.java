package com.example.healthcareapp;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText email, password;
    private TextView signupRedirect, forgotPassword;
    private Button loginButton, googleBtn, facebookBtn;
    GoogleSignInOptions gOptions;
    GoogleSignInClient gClient;
    CallbackManager callbackManager;
    private static final String TAG = "FacebookAuthentication";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Init();

        auth = FirebaseAuth.getInstance();

        onStart();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = email.getText().toString();
                String pass = password.getText().toString();

                if (!user.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(user).matches()) {
                    if(!pass.isEmpty()){
                        auth.signInWithEmailAndPassword(user, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity2.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        password.setError("Empty fields are not allowed");
                    }
                }
                else if (user.isEmpty()) {
                    email.setError("Empty fields are not allowed");
                } else {
                    email.setError("Please enter correct email");
                }
            }
        });

        signupRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });


        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString((R.string.default_web_client_id)))
                .requestEmail()
                .build();
        gClient = GoogleSignIn.getClient(this, gOptions);

//        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(this);
//        if (gAccount != null){
//            finish();
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//        }
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                            try {
                                GoogleSignInAccount account = task.getResult(ApiException.class);
                                firebaseAuthwithGoogle(account);
                            } catch (ApiException e){
                                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = gClient.getSignInIntent();
                activityResultLauncher.launch(signInIntent);
            }
        });

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                // Facebook login successful, authenticate with Firebase
                                handleFacebookAccessToken(loginResult.getAccessToken());
                            }

                            @Override
                            public void onCancel() {
                                Log.d(TAG, "onCancel");
                            }

                            @Override
                            public void onError(FacebookException error) {
                                Log.d(TAG, "onError:" + error);
                            }
                        }
                );
            }
        });
    }
    private void Init(){
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginbtn);
        signupRedirect = findViewById(R.id.signupRedirect);
        forgotPassword = findViewById(R.id.forgotpassword);
        googleBtn = findViewById(R.id.gglogin);
        facebookBtn = findViewById(R.id.fblogin);
    }
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            finish();
            Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
            startActivity(intent);
        }
    }
    private void firebaseAuthwithGoogle(GoogleSignInAccount acct){
        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            String email = user.getEmail();
                            String uid = user.getUid();
                            String name = user.getDisplayName();
                            String img = user.getPhotoUrl().toString();
                            HashMap<Object,String> hashMap = new HashMap<>();
                            hashMap.put("email", email);
                            hashMap.put("id", uid);
                            hashMap.put("name", name);
                            hashMap.put("img", img);
                            hashMap.put("phone", "");
                            hashMap.put("city", "");
                            hashMap.put("country", "");
                            hashMap.put("about", "");

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference = database.getReference("users");
                            databaseReference.child(uid).setValue(hashMap);


                            startActivity(new Intent(LoginActivity.this, MainActivity2.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = auth.getCurrentUser();
                            String email = user.getEmail();
                            String uid = user.getUid();
                            String name = user.getDisplayName();
                            String img = user.getPhotoUrl().toString();
                            HashMap<Object,String> hashMap = new HashMap<>();
                            hashMap.put("email", email);
                            hashMap.put("id", uid);
                            hashMap.put("name", name);
                            hashMap.put("img", img);
                            hashMap.put("phone", "");
                            hashMap.put("city", "");
                            hashMap.put("country", "");
                            hashMap.put("about", "");

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference = database.getReference("users");
                            databaseReference.child(uid).setValue(hashMap);

                            onStart();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Facebook Login button callback
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}