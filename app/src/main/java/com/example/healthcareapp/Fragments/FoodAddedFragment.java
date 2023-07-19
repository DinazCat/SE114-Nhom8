package com.example.healthcareapp.Fragments;

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

import com.example.healthcareapp.Adapter.NewFoodAdapter;
import com.example.healthcareapp.EditFoodActivity;
import com.example.healthcareapp.ListInterface.ClickFoodItem;
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

public class FoodAddedFragment extends Fragment {
    RecyclerView recyclerViewNewFood;
    List<food> newFoodList;
    DatabaseReference database;
    NewFoodAdapter newFoodAdapter;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_added, container, false);
        Calendar calendar = Calendar.getInstance();

        String date = DateFormat.format("yyyy-MM-dd", calendar).toString();
        //region RecyleViewFood
        recyclerViewNewFood = view.findViewById(R.id.recyclerviewNewFood);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerViewNewFood.getContext());
        recyclerViewNewFood.setLayoutManager(linearLayoutManager);
        newFoodList = new ArrayList<>();
        newFoodAdapter = new NewFoodAdapter(newFoodList,new ClickFoodItem() {
            @Override
            public void onClickItemFood(food _food) {
                Intent i = new Intent(getContext(), EditFoodActivity.class);
                i.putExtra("id",_food.getIdFood());
                startActivity(i);
            }
        });

        recyclerViewNewFood.setAdapter(newFoodAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(recyclerViewNewFood.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewNewFood.addItemDecoration(itemDecoration);

        database = FirebaseDatabase.getInstance().getReference("newFoodUserAdd");
        database.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newFoodList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    food _food = dataSnapshot.getValue(food.class);
                    newFoodList.add(_food);
                }
                newFoodAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //endregion
        return view;
    }
}