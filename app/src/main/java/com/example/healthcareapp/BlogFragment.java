package com.example.healthcareapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BlogFragment extends Fragment {
    public static ArrayList<PostInformation> postlist = new ArrayList<>();
    ListView listp ;
    private FirebaseFirestore db;

    Button addpost;
    ImageView imgUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog, container, false);
        listp = view.findViewById(R.id.listpost);
        db = FirebaseFirestore.getInstance();
        db.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PostInformation Info ;
                                System.out.println(document.toObject(PostInformation.class));
                                Info = document.toObject(PostInformation.class);
                                postlist.add(Info);
                            }
                            CustomAdapter2 adapter = new CustomAdapter2( BlogFragment.this,postlist);
                            listp.setAdapter(adapter);


                        } else {
                            System.out.println(task.getException());
                        }
                    }


                });
        addpost=view.findViewById(R.id.addP);
        addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PostActivity.class));
            }
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser curUser = auth.getCurrentUser();
        imgUser = view.findViewById(R.id.img_avatar);
        if(curUser.getPhotoUrl() != null) {
            Picasso.get().load(curUser.getPhotoUrl()).into(imgUser);
        }else {
            String uri = "https://i.pinimg.com/originals/0c/3b/3a/0c3b3adb1a7530892e55ef36d3be6cb8.png";
            Picasso.get().load(uri).into(imgUser);
        }

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userId = curUser.getUid();
                ProfileFragment fragment = new ProfileFragment();
                Bundle args = new Bundle();
                args.putString("userId", userId);
                fragment.setArguments(args);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}