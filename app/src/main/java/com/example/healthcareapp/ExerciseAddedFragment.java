package com.example.healthcareapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthcareapp.Adapter.NewExerciseAdapter;
import com.example.healthcareapp.ListInterface.ClickExerciseItem;
import com.example.healthcareapp.Model.exercise;
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
    DatabaseReference database;
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
                Intent i = new Intent(getContext(), EditExerciseActivity.class);
                i.putExtra("id",e.getIdExercise());
                startActivity(i);
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