package com.example.healthcareapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private String userId;
    ImageView userimg;
    TextView username, about;
    Button btn1, btn2;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser curUser = auth.getCurrentUser();

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        userimg = view.findViewById(R.id.img_user);
        username = view.findViewById(R.id.tv_name);
        about = view.findViewById(R.id.tv_about);
        btn1 = view.findViewById(R.id.bt_1);
        btn2 = view.findViewById(R.id.bt_2);
        Bundle args = getArguments();
        if (args != null) {
            userId = args.getString("userId");
            databaseReference = database.getReference("users");
            Query query = databaseReference.orderByChild("id").equalTo(userId);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds : snapshot.getChildren()){
                        String name = ds.child("name").getValue() + "";
                        String About = ds.child("about").getValue() + "";
                        String img = ds.child("img").getValue() + "";
                        username.setText(name);
                        if(About.isEmpty()) About = "Nothing added.";
                        about.setText(About);
                        try {
                            Picasso.get().load(img).into(userimg);
                        } catch (Exception e){
                            String uri = "https://i.pinimg.com/originals/0c/3b/3a/0c3b3adb1a7530892e55ef36d3be6cb8.png";
                            Picasso.get().load(uri).into(userimg);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            if(userId == curUser.getUid()){
                btn1.setText("Edit");
                btn2.setText("Logout");
                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userId = curUser.getUid();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.frame_layout, new EditProfileFragment());
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });
                btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GoogleSignInOptions gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                        GoogleSignInClient gClient = GoogleSignIn.getClient(getContext(), gOptions);
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
            }
            else{
                btn1.setText("Follow");
                btn2.setText("Chat");
                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }

        }
        // Inflate the layout for this fragment
        return view;
    }
}