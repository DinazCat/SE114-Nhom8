package com.example.healthcareapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.ListInterface.ClickIngredientItem;
import com.example.healthcareapp.ListInterface.ClickRecipeItem;
import com.example.healthcareapp.Model.ingredient;
import com.example.healthcareapp.Model.recipe;
import com.example.healthcareapp.R;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>{
    List<recipe> recipeList;
    private ClickRecipeItem clickRecipeItem;

    public RecipeAdapter(List<recipe> recipeList, ClickRecipeItem clickRecipeItem) {
        this.recipeList = recipeList;
        this.clickRecipeItem = clickRecipeItem;
    }
    public void setFilteredList(List<recipe> filteredList) {
        this.recipeList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_layout,parent,false);
        return new RecipeAdapter.RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.RecipeViewHolder holder, int position) {
        recipe re = recipeList.get(position);
        holder.recipeName.setText(re.getNameRecipe());
        holder.recipeCalorie.setText(re.getCalorieRecipe());
        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRecipeItem.onClickItemRecipe2(re);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRecipeItem.onClickItemRecipe(re);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (recipeList != null)
            return recipeList.size();
        return 0;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        private TextView recipeName;
        private TextView recipeCalorie;
        private Button btnShare;
        private Button btnDelete;


        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.nameRecipe);
            recipeCalorie = itemView.findViewById(R.id.caloriesRecipe);
            btnShare = itemView.findViewById(R.id.Share_btn);
            btnDelete = itemView.findViewById(R.id.Delete_btn);
        }


    }
}
