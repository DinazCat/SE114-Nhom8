package com.example.healthcareapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthcareapp.Adapter.ExerciseAdapter;
import com.example.healthcareapp.Model.exercise;
import com.example.healthcareapp.R;

import java.util.ArrayList;
import java.util.List;

public class SearchExerciseFragment extends Fragment {

    private RecyclerView recyclerViewExercise;
    private ExerciseAdapter exerciseAdapter;
    private List<exercise> exerciseList;
    private SearchView searchViewExercise;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_exercise, container, false);


        searchViewExercise = view.findViewById(R.id.searchViewExercise);
        searchViewExercise.clearFocus();
        searchViewExercise.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });


        recyclerViewExercise = view.findViewById(R.id.recyclerviewSearchExercise);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerViewExercise.getContext());
        recyclerViewExercise.setLayoutManager(linearLayoutManager);
        exerciseAdapter = new ExerciseAdapter(getList());
        recyclerViewExercise.setAdapter(exerciseAdapter);
        exerciseList = getList();
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(recyclerViewExercise.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewExercise.addItemDecoration(itemDecoration);

        return view;
    }
    private void filterList(String text) {
        List<exercise> filteredList = new ArrayList<>();
        for (exercise exercise : exerciseList) {
            if (exercise.getNameExercise().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(exercise);

            }
        }
        if (filteredList.isEmpty()) {
            // Thong bao khong co
        }
        else {
            exerciseAdapter.setFilteredList(filteredList);
        }
    }
    private List<exercise> getList() {
        List<exercise> list = new ArrayList<>();
        list.add(new exercise("Raise Leg","10 minutes","405 cl"));
        list.add(new exercise("Badminton","120 minutes","303 cl"));
        list.add(new exercise("Back Butterfly","20 minutes","330 cl"));
        list.add(new exercise("Chin-Ups","10 minutes","420 cl"));
        list.add(new exercise("Dips","10 minutes","88 cl"));
        list.add(new exercise("Heel Raise","110 minutes","238 cl"));
        list.add(new exercise("Leg Press","20 minutes","175 cl"));
        list.add(new exercise("Chin-Ups","10 minutes","420 cl"));
        list.add(new exercise("Dips","10 minutes","88 cl"));
        list.add(new exercise("Heel Raise","110 minutes","238 cl"));
        list.add(new exercise("Leg Press","20 minutes","175 cl"));
        return list;
    }
}