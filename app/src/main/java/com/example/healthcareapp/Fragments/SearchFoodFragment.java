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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.healthcareapp.Adapter.FoodAdapter;
import com.example.healthcareapp.Model.food;
import com.example.healthcareapp.R;

import java.util.ArrayList;
import java.util.List;


public class SearchFoodFragment extends Fragment {
    private RecyclerView recyclerViewFood;
    private FoodAdapter foodAdapter;
    private List<food> foodList;
    private SearchView searchView;
    private Spinner spn;
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
        foodAdapter = new FoodAdapter(getListFoods());
        recyclerViewFood.setAdapter(foodAdapter);
        foodList = getListFoods();
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(recyclerViewFood.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewFood.addItemDecoration(itemDecoration);
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

    private List<food> getListFoods() {
        List<food> list = new ArrayList<>();
        list.add(new food("Rice","Rice","405 cl","300g"));
        list.add(new food("Spaghetti","Spaghetti","303 cl","300g"));
        list.add(new food("Pasta","Pasta","330 cl","300g"));
        list.add(new food("Potatoes","Potatoes","420 cl","300g"));
        list.add(new food("Bread","Bread","88 cl","1 slice/40g"));
        list.add(new food("Macaroni","Macaroni","238 cl","250g"));
        list.add(new food("Noodles","Noodles","175 cl","250g"));
        return list;
    }

}