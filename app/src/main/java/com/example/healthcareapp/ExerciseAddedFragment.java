package com.example.healthcareapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthcareapp.Adapter.NewExerciseAdapter;
import com.example.healthcareapp.Adapter.NewFoodAdapter;
import com.example.healthcareapp.ListInterface.ClickExerciseItem;
import com.example.healthcareapp.ListInterface.ClickFoodItem;
import com.example.healthcareapp.Model.exercise;
import com.example.healthcareapp.Model.food;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ExerciseAddedFragment extends Fragment {

    RecyclerView recyclerViewNewExercise;
    List<exercise> newExerciseList;
    DatabaseReference database, database1, db;
    NewExerciseAdapter newExerciseAdapter;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_added, container, false);
        Calendar calendar = Calendar.getInstance();

        String date = DateFormat.format("yyyy-MM-dd", calendar).toString();
        //region RecyleViewFood
        recyclerViewNewExercise = view.findViewById(R.id.recyclerviewNewExercise);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerViewNewExercise.getContext());
        recyclerViewNewExercise.setLayoutManager(linearLayoutManager);
        newExerciseList = new ArrayList<>();
        newExerciseAdapter = new NewExerciseAdapter(newExerciseList, new ClickExerciseItem() {
            @Override
            public void onClickItemExercise(exercise e) {
                if (LanguageUtils.getCurrentLanguage() == Language.ENGLISH) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("Delete2");
                    dialog.setIcon(R.drawable.noti_icon);
                    dialog.setMessage("You want to delete??");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseReference database = FirebaseDatabase.getInstance().getReference("newExerciseUserAdd");
                            database.child(uid).child(e.getIdExercise()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    snapshot.getRef().removeValue();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            Intent i = new Intent(view.getContext(), SearchTopTabActivity.class);
                            startActivity(i);

                        }
                    });
                    dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = dialog.create();
                    // Show the Alert Dialog box
                    alertDialog.show();
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("Xóa");
                    dialog.setIcon(R.drawable.noti_icon);
                    dialog.setMessage("Bạn có muốn xóa??");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseReference database = FirebaseDatabase.getInstance().getReference("newFoodUserAdd");
                            database.child(uid).child(e.getIdExercise()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    snapshot.getRef().removeValue();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            Intent i = new Intent(view.getContext(), TopTabExerciseActivity.class);
                            startActivity(i);
                        }

                    });
                    dialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = dialog.create();
                    // Show the Alert Dialog box
                    alertDialog.show();
                }
            }
        });
                recyclerViewNewExercise.setAdapter(newExerciseAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(recyclerViewNewExercise.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewNewExercise.addItemDecoration(itemDecoration);

        database = FirebaseDatabase.getInstance().getReference("newExerciseUserAdd");
        database.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newExerciseList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    exercise e = dataSnapshot.getValue(exercise.class);
                    newExerciseList.add(e);
                }
                newExerciseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}