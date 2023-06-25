package com.example.healthcareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcareapp.Adapter.ExerciseAdapter;
import com.example.healthcareapp.Fragments.AddFragment;
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

public class SearchExerciseActivity extends AppCompatActivity {
    private RecyclerView recyclerViewExercise;
    private ExerciseAdapter exerciseAdapter;
    private List<exercise> exerciseList;
    private SearchView searchViewExercise;
    TextView tvDate;
    Button btn_back;
    DatabaseReference database, database1;
    TextView tvEngVie;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_exercise);tvEngVie = findViewById(R.id.textViewExercise);
        btn_back = findViewById(R.id.back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchExerciseActivity.this, AddFragment.class);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        tvDate = findViewById(R.id.date);
        Calendar calendar = Calendar.getInstance();
        String today = DateFormat.format("yyyy-MM-dd", calendar).toString();

        //SEARCH VIEW
        searchViewExercise = findViewById(R.id.searchViewExercise);
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
        recyclerViewExercise = findViewById(R.id.recyclerviewSearchExercise);
        setRecyclerView(today);

        //Set DATE
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(SearchExerciseActivity.this, new DatePickerDialog.OnDateSetListener() {
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

    }

    private void setRecyclerView(String date) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerViewExercise.getContext());
        recyclerViewExercise.setLayoutManager(linearLayoutManager);
        exerciseList =new ArrayList<>();

        exerciseAdapter = new ExerciseAdapter(exerciseList, new ClickExerciseItem() {
            @Override
            public void onClickItemExercise(exercise e) {
                if (LanguageUtils.getCurrentLanguage() == Language.ENGLISH) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(SearchExerciseActivity.this);
                    dialog.setTitle("Add");
                    dialog.setIcon(R.drawable.noti_icon);
                    dialog.setMessage("You want to add??");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            database = FirebaseDatabase.getInstance().getReference("exerciseDiary");
                            database.child(uid).child(date).child(String.valueOf(e.getIdExercise())).setValue(e);
                            Toast.makeText(SearchExerciseActivity.this, "Add Succes", Toast.LENGTH_SHORT).show();
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
                }
                else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(SearchExerciseActivity.this);
                    dialog.setTitle("Thêm vào");
                    dialog.setIcon(R.drawable.noti_icon);
                    dialog.setMessage("Bạn có muốn thêm hoạt động này vào không??");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            database = FirebaseDatabase.getInstance().getReference("exerciseDiary");
                            database.child(uid).child(date).child(String.valueOf(e.getIdExercise())).setValue(e);
                            Toast.makeText(SearchExerciseActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
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