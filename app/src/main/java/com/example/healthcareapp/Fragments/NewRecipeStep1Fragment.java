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
import com.example.healthcareapp.Language;
import com.example.healthcareapp.LanguageUtils;
import com.example.healthcareapp.Model.ingredient;
import com.example.healthcareapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class NewRecipeStep1Fragment extends Fragment {
    Button btAddIngredient;
    EditText etNameRecipe, etPrepRecipe, etCookingRecipe;
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
        etNameRecipe = view.findViewById(R.id.addRecipeName);
        etCookingRecipe = view.findViewById(R.id.addRecipeCooking);
        etPrepRecipe = view.findViewById(R.id.addRecipePrep);

        btAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LanguageUtils.getCurrentLanguage() == Language.ENGLISH) {
                    Intent i = new Intent(view.getContext(), AddIngredientsActivity.class);
                    if (etNameRecipe.getText().toString().trim().isEmpty() || etCookingRecipe.getText().toString().trim().isEmpty() || etPrepRecipe.getText().toString().trim().isEmpty())
                        Toast.makeText(view.getContext(), "Please enter name, cooking time and prepare time", Toast.LENGTH_SHORT).show();
                    else {
                        i.putExtra("nameFood", etNameRecipe.getText().toString().trim());
                        i.putExtra("cookingFood", etCookingRecipe.getText().toString().trim());
                        i.putExtra("prepFood", etPrepRecipe.getText().toString().trim());
                        launcherActivity.launch(i);
                    }
                }
                else {
                    Intent i = new Intent(view.getContext(), AddIngredientsActivity.class);
                    if (etNameRecipe.getText().toString().trim().isEmpty() || etCookingRecipe.getText().toString().trim().isEmpty() || etPrepRecipe.getText().toString().trim().isEmpty())
                        Toast.makeText(view.getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    else {
                        i.putExtra("nameFood", etNameRecipe.getText().toString().trim());
                        i.putExtra("cookingFood", etCookingRecipe.getText().toString().trim());
                        i.putExtra("prepFood", etPrepRecipe.getText().toString().trim());
                        launcherActivity.launch(i);
                    }
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