package com.example.healthcareapp.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.healthcareapp.Adapter.FoodAdapter;
import com.example.healthcareapp.Adapter.RecipeAdapter;
import com.example.healthcareapp.AddIngredientsActivity;
import com.example.healthcareapp.AddNewFoodActivity;
import com.example.healthcareapp.ListInterface.ClickFoodItem;
import com.example.healthcareapp.ListInterface.ClickRecipeItem;
import com.example.healthcareapp.Model.food;
import com.example.healthcareapp.Model.recipe;
import com.example.healthcareapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddRecipeFragment extends Fragment {

    DatabaseReference database1;
    RecyclerView recyclerViewRecipe1;
    private RecipeAdapter recipeAdapter;
    private List<recipe> recipeList;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_recipe, container, false);


        recyclerViewRecipe1 = view.findViewById(R.id.recyclerviewRecipe1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerViewRecipe1.getContext());
        recyclerViewRecipe1.setLayoutManager(linearLayoutManager);
        recipeList = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(recipeList, view.getContext());

        recyclerViewRecipe1.setAdapter(recipeAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(recyclerViewRecipe1.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewRecipe1.addItemDecoration(itemDecoration);

        database1 = FirebaseDatabase.getInstance().getReference("newRecipe");
        database1.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recipeList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    recipe re = dataSnapshot.getValue(recipe.class);
                    recipeList.add(re);
                }
                recipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }




}