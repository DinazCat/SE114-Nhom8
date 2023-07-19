package com.example.healthcareapp.Fragments;

import static android.app.Activity.RESULT_OK;
import static com.example.healthcareapp.LanguageUtils.CURRENT_LANGUAGE;
import static com.example.healthcareapp.LanguageUtils.getCurrentLanguage;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcareapp.Adapter.ExerciseAdapter;
import com.example.healthcareapp.Language;
import com.example.healthcareapp.LanguageUtils;
import com.example.healthcareapp.ListInterface.ClickExerciseItem;
import com.example.healthcareapp.ListInterface.ClickFoodItem;
import com.example.healthcareapp.Model.exercise;
import com.example.healthcareapp.Model.food;
import com.example.healthcareapp.R;
import com.example.healthcareapp.SearchExerciseActivity;
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
    public static ExerciseAdapter exerciseAdapter;
    public static List<exercise> exerciseList;
    private SearchView searchViewExercise;
    TextView tvDate;
    Button btn_back;
    DatabaseReference database, database1, database2;
    TextView tvEngVie;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_exercise, container, false);
        tvEngVie = view.findViewById(R.id.textViewExercise);


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

        return view;
    }
    private void setRecyclerView(String date) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerViewExercise.getContext());
        recyclerViewExercise.setLayoutManager(linearLayoutManager);
        exerciseList =new ArrayList<>();

        exerciseAdapter = new ExerciseAdapter(exerciseList, new ClickExerciseItem() {
            @Override
            public void onClickItemExercise(exercise e) {
                    final Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_add_excercise);
                    Window window =dialog.getWindow();
                    if (window == null) {
                        return;
                    }
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    WindowManager.LayoutParams windowAttributes = window.getAttributes();
                    windowAttributes.gravity = Gravity.CENTER;
                    window.setAttributes(windowAttributes);

                    Button btnCancel = dialog.findViewById(R.id.cancel);
                    Button btnAdd = dialog.findViewById(R.id.add);
                    EditText number = dialog.findViewById(R.id.editTextNumber);


                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    btnAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Integer n = Integer.parseInt(number.getText().toString());
                            exercise e2 = e;
                            e2.setCaloriesBurnedAMin(String.valueOf(Integer.parseInt(e.getCaloriesBurnedAMin())/Integer.parseInt(e.getMinutePerformed())*n));
                            database = FirebaseDatabase.getInstance().getReference("exerciseDiary");
                            database.child(uid).child(date).child(String.valueOf(e.getIdExercise())).setValue(e);
                            Toast.makeText(getContext(), "Add Succes", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                    });
                    dialog.show();
            }
        });
        recyclerViewExercise.setAdapter(exerciseAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(recyclerViewExercise.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewExercise.addItemDecoration(itemDecoration);
        if (tvEngVie.getText().toString().equals("Add Exercise")) {
            database1 = FirebaseDatabase.getInstance().getReference("exercisesEng");
        }
        else {
            database1 = FirebaseDatabase.getInstance().getReference("exercises");
        }
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
        database2 = FirebaseDatabase.getInstance().getReference("newExerciseUserAdd");
        database2.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    exercise exercise = dataSnapshot.getValue(exercise.class);
                    exerciseList.add(exercise);
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