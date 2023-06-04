package com.example.healthcareapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.healthcareapp.Model.food;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Model.ingredient;
import com.example.healthcareapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class IngredientAdapterAdd extends RecyclerView.Adapter<IngredientAdapterAdd.IngredientAddViewHolder> {

    List<ingredient> ingredientList;
    Context context;
    public IngredientAdapterAdd(Context context, List<ingredient> ingredientsList) {
        this.ingredientList = ingredientsList;
        this.context = context;
    }
    public void setFilteredList(List<ingredient> filteredList) {
        this.ingredientList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientAddViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item_add,parent,false);
        return new IngredientAddViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAddViewHolder holder, int position) {
        ingredient in = ingredientList.get(position);
        holder.ingredientName.setText(in.getNameIngredient());
        holder.ingredientCalorie.setText(in.getCalorieIngredient());
    }

    @Override
    public int getItemCount() {
        if (ingredientList != null)
            return ingredientList.size();
        return 0;
    }

    public class IngredientAddViewHolder extends RecyclerView.ViewHolder {

        private TextView ingredientName;
        private TextView ingredientCalorie;


        public IngredientAddViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.nameIngredient);
            ingredientCalorie = itemView.findViewById(R.id.caloriesIngredient);

        }


    }
}