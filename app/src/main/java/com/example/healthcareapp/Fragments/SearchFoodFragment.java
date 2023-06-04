package com.example.healthcareapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.healthcareapp.Adapter.FoodAdapter;
import com.example.healthcareapp.Model.exercise;
import com.example.healthcareapp.Model.food;
import com.example.healthcareapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SearchFoodFragment extends Fragment {
    private RecyclerView recyclerViewFood;
    private FoodAdapter foodAdapter;
    private List<food> foodList;
    private SearchView searchView;
    private Spinner spn;
    DatabaseReference database;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_food, container, false);

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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerViewFood.getContext());
        recyclerViewFood.setLayoutManager(linearLayoutManager);
        foodList = new ArrayList<>();
        foodAdapter = new FoodAdapter(foodList);
        recyclerViewFood.setAdapter(foodAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(recyclerViewFood.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewFood.addItemDecoration(itemDecoration);
        database = FirebaseDatabase.getInstance().getReference("foods");
        database.addValueEventListener(new ValueEventListener() {
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
        return view;
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