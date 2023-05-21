package com.example.healthcareapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.healthcareapp.Adapter.NotiAdapter;
import com.example.healthcareapp.Model.Noti;
import com.example.healthcareapp.Model.PostInformation;
import com.example.healthcareapp.PostDetailActivity;
import com.example.healthcareapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationFragment extends Fragment {
    public static ArrayList<Noti> noread ;
    public static ArrayList<Noti> read ;
    LinearLayout readL, noreadL;
    public static ListView readLv, noreadLv;
    FirebaseUser curUser;
    private FirebaseFirestore db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        curUser = FirebaseAuth.getInstance().getCurrentUser();
        noread = new ArrayList<>();
        read = new ArrayList<>();
        readL = view.findViewById(R.id.readList);
        noreadL = view.findViewById(R.id.noreadList);
        readLv = view.findViewById(R.id.lvRead);
        noreadLv = view.findViewById(R.id.lvNoread);

        db = FirebaseFirestore.getInstance();
        db.collection("Notification")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Noti Info ;
                                Info = document.toObject(Noti.class);
                                Info.id = document.getId();
                                if(!Info.PostownerId.equals(curUser.getUid())) {
                                    if ( Info.Read.equals("no") ){
                                        System.out.println("count"+Info.id);
                                        noread.add(Info);
                                    }
                                    else read.add(Info);
                                    UpdateNoti(document.getId());
                                }
                            }
                            if(noread.size()==0){
                                noreadL.setVisibility(View.INVISIBLE);
                            }
                            else {
                                NotiAdapter adapter = new NotiAdapter(getActivity(), noread,getActivity().getSupportFragmentManager());
                                noreadLv.setAdapter(adapter);
                            }
                            if(read.size()==0){
                                readL.setVisibility(View.INVISIBLE);
                            }
                            else{
                                NotiAdapter adapter = new NotiAdapter(getActivity(), read,getActivity().getSupportFragmentManager());
                                readLv.setAdapter(adapter);
                            }


                        } else {
                            System.out.println(task.getException());
                        }
                    }


                });


        return view;
    }
    private void UpdateNoti(String id){
        Map<String, Object> update = new HashMap<>();
        update.put("Read", "yes");

        db.collection("Notification").document(id).set(update, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });
    }
}
