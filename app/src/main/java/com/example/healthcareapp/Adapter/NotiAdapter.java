package com.example.healthcareapp.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.healthcareapp.Fragments.NotificationFragment;
import com.example.healthcareapp.Fragments.ProfileFragment;
import com.example.healthcareapp.Model.Noti;
import com.example.healthcareapp.Model.PostInformation;
import com.example.healthcareapp.PostDetailActivity;
import com.example.healthcareapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotiAdapter extends BaseAdapter {
    private Activity activity;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ArrayList<Noti> items;
    private FragmentManager f;

    public NotiAdapter(Activity activity, ArrayList<Noti> items,FragmentManager fragmentManager) {
        this.items = items;
        this.activity = activity;
        this.f = fragmentManager;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = activity.getLayoutInflater();
        view = inflater.inflate(R.layout.notificationitem, null);
        CircleImageView userImg = view.findViewById(R.id.userimg);
        TextView mess = view.findViewById(R.id.mess);
        TextView time = view.findViewById(R.id.time);
        ImageButton clear = view.findViewById(R.id.clear);
        ImageButton see = view.findViewById(R.id.see);
        see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(items.get(i).classify.equals("follow")){
                    ProfileFragment fragment = new ProfileFragment();
                    Bundle args = new Bundle();
                    args.putString("userId", items.get(i).guestId);
                    fragment.setArguments(args);
                    FragmentTransaction transaction = f.beginTransaction();
                    transaction.replace(R.id.frame_layout, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else {
                    DocumentReference docRef =  FirebaseFirestore.getInstance().collection("posts").document(items.get(i).postid);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    PostInformation Info ;
                                    Info = document.toObject(PostInformation.class);
                                    Info.id = document.getId();
                                    PostDetailActivity.info = Info;
                                    activity.startActivity(new Intent(activity,PostDetailActivity.class));
                                }
                            } else {
                                System.out.println( "get failed with "+ task.getException());
                            }
                        }
                    });
                }
            }

        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance().collection("Notification").document(items.get(i).id)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println(e);
                            }
                        });
                if(items.get(i).Read.equals("no")) {
                    NotificationFragment.noread.remove(i);
                    NotiAdapter adapter = new NotiAdapter(activity, NotificationFragment.noread,f);
                    NotificationFragment.noreadLv.setAdapter(adapter);
                }
                else{
                    NotificationFragment.read.remove(i);
                    NotiAdapter adapter = new NotiAdapter(activity, NotificationFragment.read,f);
                    NotificationFragment.readLv.setAdapter(adapter);
                }
            }
        });

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(items.get(i).time));
        String pTime = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
        time.setText(pTime);
        DatabaseReference databaseReference = database.getReference("users");
        Query query = databaseReference.orderByChild("id").equalTo(items.get(i).guestId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String name = ds.child("name").getValue() + "";
                    String Uri = ds.child("img").getValue() + "";
                    mess.setText(name + items.get(i).message);
                    try {
                        Picasso.get().load(Uri).into(userImg);
                    } catch (Exception e) {
                        String uri = "https://i.pinimg.com/originals/0c/3b/3a/0c3b3adb1a7530892e55ef36d3be6cb8.png";
                        Picasso.get().load(uri).into(userImg);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        return view;
    }

    private void readUserData() {

    }
}