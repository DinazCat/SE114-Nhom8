package com.example.healthcareapp.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.healthcareapp.Adapter.ExerciseAdapter;
import com.example.healthcareapp.ListInterface.ClickExerciseItem;
import com.example.healthcareapp.ListInterface.ClickFoodItem;
import com.example.healthcareapp.Model.exercise;
import com.example.healthcareapp.Model.food;
import com.example.healthcareapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SearchExerciseFragment extends Fragment {

    private RecyclerView recyclerViewExercise;
    private ExerciseAdapter exerciseAdapter;
    private List<exercise> exerciseList;
    private SearchView searchViewExercise;
    TextView tvDate;
    DatabaseReference database, database1;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_exercise, container, false);

        tvDate = view.findViewById(R.id.date);
        Calendar calendar = Calendar.getInstance();
        String today = DateFormat.format("yyyy-MM-dd", calendar).toString();

        //SEARCH VIEW
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

        //RECYCLERVIEW
        recyclerViewExercise = view.findViewById(R.id.recyclerviewSearchExercise);
        setRecyclerView(today);

        //Set DATE
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        if (calendar.get(Calendar.DAY_OF_MONTH) == dayOfMonth && calendar.get(Calendar.MONTH) == month && calendar.get(Calendar.YEAR) == year){
                            tvDate.setText("Today");
                            String string = DateFormat.format("yyyy-MM-dd", calendar).toString();
                            setRecyclerView(string);
                        }
                        else{
                            calendar.set(year, month, dayOfMonth);
                            tvDate.setText(DateFormat.format("dd/MM/yyyy", calendar).toString());
                            String string = DateFormat.format("yyyy-MM-dd", calendar).toString();
                            setRecyclerView(string);
                        }
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        return view;
    }
    private void setRecyclerView(String date) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerViewExercise.getContext());
        recyclerViewExercise.setLayoutManager(linearLayoutManager);
        exerciseList =new ArrayList<>();

        exerciseAdapter = new ExerciseAdapter(exerciseList, new ClickExerciseItem() {
            @Override
            public void onClickItemExercise(exercise e) {
                database = FirebaseDatabase.getInstance().getReference("exerciseDiary");
                database.child(uid).child(date).child(String.valueOf(e.getIdExercise())).setValue(e);
            }
        });
        recyclerViewExercise.setAdapter(exerciseAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(recyclerViewExercise.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewExercise.addItemDecoration(itemDecoration);
        database1 = FirebaseDatabase.getInstance().getReference("exercises");
        database1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    exercise in = dataSnapshot.getValue(exercise.class);
                    exerciseList.add(in);
                }
                exerciseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
}