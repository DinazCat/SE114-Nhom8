package com.example.healthcareapp.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.healthcareapp.ChangeBmiAndGoal;
import com.example.healthcareapp.LoginActivity;
import com.example.healthcareapp.NotificationSettingActivity;
import com.example.healthcareapp.R;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    private FirebaseAuth auth;
    private Button btn_ChangeBmi;
    GoogleSignInOptions gOptions;
    GoogleSignInClient gClient;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        auth = FirebaseAuth.getInstance();

        TextView logout = view.findViewById(R.id.logout);
        TextView editprofile = view.findViewById(R.id.editprofile);
        TextView notifications = view.findViewById(R.id.noti);

        btn_ChangeBmi = view.findViewById(R.id.changeBmi);
        logout.setOnClickListener(new View.OnClickListener() {
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

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_layout, new EditProfileFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NotificationSettingActivity.class));
            }
        });

        //change bmi Info
        btn_ChangeBmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcherChangeBmiAndGoal.launch(new Intent(getContext(), ChangeBmiAndGoal.class));
            }
        });
        return view;
    }
    ActivityResultLauncher<Intent> launcherChangeBmiAndGoal = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                    }
                }
            });

}