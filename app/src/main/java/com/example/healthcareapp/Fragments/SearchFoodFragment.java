package com.example.healthcareapp.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcareapp.Adapter.ExpandableListViewAdapter;
import com.example.healthcareapp.Adapter.FoodAdapter;
import com.example.healthcareapp.Language;
import com.example.healthcareapp.LanguageUtils;
import com.example.healthcareapp.ListInterface.ClickFoodItem;
import com.example.healthcareapp.Model.exercise;
import com.example.healthcareapp.Model.food;
import com.example.healthcareapp.Model.note;
import com.example.healthcareapp.NoteActivity;
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


public class SearchFoodFragment extends Fragment {
    private RecyclerView recyclerViewFood;
    private FoodAdapter foodAdapter;
    private List<food> foodList;
    private SearchView searchView;
    private Spinner spn;
    DatabaseReference database, database1;
    TextView tvDate, tvEngVie;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_food, container, false);
        tvEngVie = view.findViewById(R.id.textViewAddFoodEngVie);
        tvDate = view.findViewById(R.id.date);
        Calendar calendar = Calendar.getInstance();
        String today = DateFormat.format("yyyy-MM-dd", calendar).toString();

        spn = (Spinner)view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(), R.array.meals, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adapter);
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchView = view.findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        //RecyclerView
        recyclerViewFood = view.findViewById(R.id.recyclerviewSearchFood);
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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerViewFood.getContext());
        recyclerViewFood.setLayoutManager(linearLayoutManager);
        foodList = new ArrayList<>();
        foodAdapter = new FoodAdapter(foodList, new ClickFoodItem() {
            @Override
            public void onClickItemFood(food _food) {
                if (LanguageUtils.getCurrentLanguage() == Language.ENGLISH) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("Add");
                    dialog.setIcon(R.drawable.noti_icon);
                    dialog.setMessage("You want to add??");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (tvEngVie.getText().toString().equals("Add food")) {
                                database = FirebaseDatabase.getInstance().getReference("foodDiary");
                                database.child(uid).child(date).child(spn.getSelectedItem().toString()).child(String.valueOf(_food.getIdFood())).setValue(_food);
                                Toast.makeText(getContext(), "Add Succes", Toast.LENGTH_SHORT).show();
                            } else {
                                if (spn.getSelectedItemPosition() == 0) {
                                    database = FirebaseDatabase.getInstance().getReference("foodDiary");
                                    database.child(uid).child(date).child("Breakfast").child(String.valueOf(_food.getIdFood())).setValue(_food);
                                    Toast.makeText(getContext(), "Add Succes", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (spn.getSelectedItemPosition() == 1) {
                                        database = FirebaseDatabase.getInstance().getReference("foodDiary");
                                        database.child(uid).child(date).child("Lunch").child(String.valueOf(_food.getIdFood())).setValue(_food);
                                        Toast.makeText(getContext(), "Add Succes", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (spn.getSelectedItemPosition() == 2) {
                                            database = FirebaseDatabase.getInstance().getReference("foodDiary");
                                            database.child(uid).child(date).child("Dinner").child(String.valueOf(_food.getIdFood())).setValue(_food);
                                            Toast.makeText(getContext(), "Add Succes", Toast.LENGTH_SHORT).show();
                                        } else {
                                            database = FirebaseDatabase.getInstance().getReference("foodDiary");
                                            database.child(uid).child(date).child("Snack").child(String.valueOf(_food.getIdFood())).setValue(_food);
                                            Toast.makeText(getContext(), "Add Succes", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                }
                            }
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
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("Thêm vào");
                    dialog.setIcon(R.drawable.noti_icon);
                    dialog.setMessage("Bạn có muốn thêm??");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (tvEngVie.getText().toString().equals("Add food")) {
                                database = FirebaseDatabase.getInstance().getReference("foodDiary");
                                database.child(uid).child(date).child(spn.getSelectedItem().toString()).child(String.valueOf(_food.getIdFood())).setValue(_food);
                                Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                if (spn.getSelectedItemPosition() == 0) {
                                    database = FirebaseDatabase.getInstance().getReference("foodDiary");
                                    database.child(uid).child(date).child("Breakfast").child(String.valueOf(_food.getIdFood())).setValue(_food);
                                    Toast.makeText(getContext(), "Thêm Thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (spn.getSelectedItemPosition() == 1) {
                                        database = FirebaseDatabase.getInstance().getReference("foodDiary");
                                        database.child(uid).child(date).child("Lunch").child(String.valueOf(_food.getIdFood())).setValue(_food);
                                        Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (spn.getSelectedItemPosition() == 2) {
                                            database = FirebaseDatabase.getInstance().getReference("foodDiary");
                                            database.child(uid).child(date).child("Dinner").child(String.valueOf(_food.getIdFood())).setValue(_food);
                                            Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                                        } else {
                                            database = FirebaseDatabase.getInstance().getReference("foodDiary");
                                            database.child(uid).child(date).child("Snack").child(String.valueOf(_food.getIdFood())).setValue(_food);
                                            Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                }
                            }
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
        recyclerViewFood.setAdapter(foodAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(recyclerViewFood.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewFood.addItemDecoration(itemDecoration);
        if (tvEngVie.getText().toString().equals("Add food")) {
            database1 = FirebaseDatabase.getInstance().getReference("foodsEng");
        }
        else {
            database1 = FirebaseDatabase.getInstance().getReference("foods");
        }

        database1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    food in = dataSnapshot.getValue(food.class);
                    foodList.add(in);
                }
                foodAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void filterList(String text) {
        List<food> filteredList = new ArrayList<>();
        for (food food : foodList) {
            if (food.getNameFood().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(food);

            }
        }
        if (filteredList.isEmpty()) {
            // Thong bao khong co
        }
        else {
            foodAdapter.setFilteredList(filteredList);
        }
    }


}