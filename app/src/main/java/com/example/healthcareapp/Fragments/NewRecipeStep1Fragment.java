package com.example.healthcareapp.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.healthcareapp.Adapter.IngredientAdapterAdd;
import com.example.healthcareapp.AddIngredientsActivity;
import com.example.healthcareapp.AddNewFoodActivity;
import com.example.healthcareapp.Model.ingredient;
import com.example.healthcareapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class NewRecipeStep1Fragment extends Fragment {
    Button btAddIngredient;
    EditText etNameIngredient;
    DatabaseReference database;
    RecyclerView recyclerView;
    IngredientAdapterAdd ingredientAdapterAdd;
    Button btnBack;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    ArrayList<ingredient> ingredientArrayList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_recipe_step1, container, false);

        btAddIngredient = view.findViewById(R.id.addIngredient);
        etNameIngredient = view.findViewById(R.id.addIngredientName);

        btAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(view.getContext(), AddIngredientsActivity.class);
                if (etNameIngredient.getText().toString().trim().isEmpty())
                    Toast.makeText(view.getContext(), "Enter Food Name", Toast.LENGTH_SHORT).show();
                else {
                    i.putExtra("nameFood", etNameIngredient.getText().toString().trim());
                    launcherActivity.launch(i);
                }
            }
        });
        return view;
    }

    ActivityResultLauncher<Intent> launcherActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                    }
                }
            });
}