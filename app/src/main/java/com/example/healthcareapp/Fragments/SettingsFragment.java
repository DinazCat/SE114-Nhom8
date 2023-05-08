package com.example.healthcareapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.healthcareapp.LoginActivity;
import com.example.healthcareapp.R;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    private FirebaseAuth auth;
    private Button logoutButton;
    GoogleSignInOptions gOptions;
    GoogleSignInClient gClient;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        auth = FirebaseAuth.getInstance();

        logoutButton = view.findViewById(R.id.logoutbtn);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                gClient = GoogleSignIn.getClient(getContext(), gOptions);
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

                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);

            }
        });



//        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(this);
//        if (gAccount != null){
//            String name = gAccount.getDisplayName();
//        }
        return view;
    }
}