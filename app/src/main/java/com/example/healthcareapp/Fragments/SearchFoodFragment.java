package com.example.healthcareapp.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
    DatabaseReference database, database1,database2;
    public TextView tvEngVie;
    ImageView back_star,front_star;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_food, container, false);
        tvEngVie = view.findViewById(R.id.textViewAddFoodEngVie);
        back_star = view.findViewById(R.id.back_star);
        front_star = view.findViewById(R.id.front_star);
        recyclerViewFood = view.findViewById(R.id.recyclerviewSearchFood);
        Calendar calendar = Calendar.getInstance();

        String today = DateFormat.format("yyyy-MM-dd", calendar).toString();
        back_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecyclerView_Star(today);
                front_star.setVisibility(View.VISIBLE);
            }
        });
        front_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecyclerView(today);
                front_star.setVisibility(View.INVISIBLE);
            }
        });
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

        setRecyclerView(today);

        return view;
    }
    private void setRecyclerView_Star(String date) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerViewFood.getContext());
        recyclerViewFood.setLayoutManager(linearLayoutManager);
        foodList = new ArrayList<>();
        foodAdapter = new FoodAdapter(foodList, new ClickFoodItem() {
            @Override
            public void onClickItemFood(food _food) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_add);
                Window window =dialog.getWindow();
                if (window == null) {
                    return;
                }
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                WindowManager.LayoutParams windowAttributes = window.getAttributes();
                windowAttributes.gravity = Gravity.CENTER;
                window.setAttributes(windowAttributes);

                TextView cabs = dialog.findViewById(R.id.cabs);
                TextView fat = dialog.findViewById(R.id.fat);
                TextView protein = dialog.findViewById(R.id.protein);
                Button btnCancel = dialog.findViewById(R.id.cancel);
                Button btnAdd = dialog.findViewById(R.id.add);
                EditText number = dialog.findViewById(R.id.editTextNumber);
                cabs.setText("Cabs: " + _food.getCabsFood());
                fat.setText("Fat: " + _food.getFatFood());
                protein.setText("Protein: " + _food.getProteinFood());

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
                        food _food2 = _food;
                        _food2.setCaloriesFood(String.valueOf(Integer.parseInt(_food.getCaloriesFood())*n));
                        if (tvEngVie.getText().toString().equals("Add food")) {
                            database = FirebaseDatabase.getInstance().getReference("foodDiary");
                            database.child(uid).child(date).child(spn.getSelectedItem().toString()).child(String.valueOf(_food2.getIdFood())).setValue(_food2);
                            Toast.makeText(getContext(), "Add Succes", Toast.LENGTH_SHORT).show();
                        } else {
                            if (spn.getSelectedItemPosition() == 0) {
                                database = FirebaseDatabase.getInstance().getReference("foodDiary");
                                database.child(uid).child(date).child("Breakfast").child(String.valueOf(_food.getIdFood())).setValue(_food2);
                                Toast.makeText(getContext(), "Add Succes", Toast.LENGTH_SHORT).show();
                            } else {
                                if (spn.getSelectedItemPosition() == 1) {
                                    database = FirebaseDatabase.getInstance().getReference("foodDiary");
                                    database.child(uid).child(date).child("Lunch").child(String.valueOf(_food.getIdFood())).setValue(_food2);
                                    Toast.makeText(getContext(), "Add Succes", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (spn.getSelectedItemPosition() == 2) {
                                        database = FirebaseDatabase.getInstance().getReference("foodDiary");
                                        database.child(uid).child(date).child("Dinner").child(String.valueOf(_food.getIdFood())).setValue(_food2);
                                        Toast.makeText(getContext(), "Add Succes", Toast.LENGTH_SHORT).show();
                                    } else {
                                        database = FirebaseDatabase.getInstance().getReference("foodDiary");
                                        database.child(uid).child(date).child("Snack").child(String.valueOf(_food.getIdFood())).setValue(_food2);
                                        Toast.makeText(getContext(), "Add Succes", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }
                        }
                        dialog.dismiss();
                    }

                });
                dialog.show();
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
                foodList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    food in = dataSnapshot.getValue(food.class);
                    if (Integer.parseInt(in.getCaloriesFood())<=Integer.parseInt(AddFragment.tvRemainingCalories.getText().toString())) {
                        foodList.add(in);
                    }
                }
                foodAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        database2 = FirebaseDatabase.getInstance().getReference("newFoodUserAdd");
        database2.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    food in = dataSnapshot.getValue(food.class);
                    if (Integer.parseInt(in.getCaloriesFood())<=Integer.parseInt(AddFragment.tvRemainingCalories.getText().toString())) {
                        foodList.add(in);
                    }
                }
                foodAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setRecyclerView(String date) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerViewFood.getContext());
        recyclerViewFood.setLayoutManager(linearLayoutManager);
        foodList = new ArrayList<>();
        foodAdapter = new FoodAdapter(foodList, new ClickFoodItem() {
            @Override
            public void onClickItemFood(food _food) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_add);
                Window window =dialog.getWindow();
                if (window == null) {
                    return;
                }
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                WindowManager.LayoutParams windowAttributes = window.getAttributes();
                windowAttributes.gravity = Gravity.CENTER;
                window.setAttributes(windowAttributes);

                TextView cabs = dialog.findViewById(R.id.cabs);
                TextView fat = dialog.findViewById(R.id.fat);
                TextView protein = dialog.findViewById(R.id.protein);
                Button btnCancel = dialog.findViewById(R.id.cancel);
                Button btnAdd = dialog.findViewById(R.id.add);
                EditText number = dialog.findViewById(R.id.editTextNumber);
                cabs.setText("Cabs: " + _food.getCabsFood());
                fat.setText("Fat: " + _food.getFatFood());
                protein.setText("Protein: " + _food.getProteinFood());

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
                        food _food2 = _food;
                        _food2.setCaloriesFood(String.valueOf(Integer.parseInt(_food.getCaloriesFood())*n));
                        if (tvEngVie.getText().toString().equals("Add food")) {
                            database = FirebaseDatabase.getInstance().getReference("foodDiary");
                            database.child(uid).child(date).child(spn.getSelectedItem().toString()).child(String.valueOf(_food2.getIdFood())).setValue(_food2);
                            Toast.makeText(getContext(), "Add Succes", Toast.LENGTH_SHORT).show();
                        } else {
                            if (spn.getSelectedItemPosition() == 0) {
                                database = FirebaseDatabase.getInstance().getReference("foodDiary");
                                database.child(uid).child(date).child("Breakfast").child(String.valueOf(_food.getIdFood())).setValue(_food2);
                                Toast.makeText(getContext(), "Add Succes", Toast.LENGTH_SHORT).show();
                            } else {
                                if (spn.getSelectedItemPosition() == 1) {
                                    database = FirebaseDatabase.getInstance().getReference("foodDiary");
                                    database.child(uid).child(date).child("Lunch").child(String.valueOf(_food.getIdFood())).setValue(_food2);
                                    Toast.makeText(getContext(), "Add Succes", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (spn.getSelectedItemPosition() == 2) {
                                        database = FirebaseDatabase.getInstance().getReference("foodDiary");
                                        database.child(uid).child(date).child("Dinner").child(String.valueOf(_food.getIdFood())).setValue(_food2);
                                        Toast.makeText(getContext(), "Add Succes", Toast.LENGTH_SHORT).show();
                                    } else {
                                        database = FirebaseDatabase.getInstance().getReference("foodDiary");
                                        database.child(uid).child(date).child("Snack").child(String.valueOf(_food.getIdFood())).setValue(_food2);
                                        Toast.makeText(getContext(), "Add Succes", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }
                        }
                        dialog.dismiss();
                    }

                });
                dialog.show();
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
                foodList.clear();
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
        database2 = FirebaseDatabase.getInstance().getReference("newFoodUserAdd");
        database2.child(uid).addValueEventListener(new ValueEventListener() {
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