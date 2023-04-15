package com.example.healthcareapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BlogFragment extends Fragment {
    public static ArrayList<PostInformation> postlist = new ArrayList<>();
    ListView listp ;
    private FirebaseFirestore db;

    Button addpost;

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
        // Inflate the layout for this fragment
        return view;
    }
}