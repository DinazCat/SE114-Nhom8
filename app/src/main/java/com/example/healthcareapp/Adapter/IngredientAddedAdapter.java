package com.example.healthcareapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.ListInterface.ClickIngredientItem;
import com.example.healthcareapp.Model.ingredient;
import com.example.healthcareapp.R;

import java.util.List;

public class IngredientAddedAdapter extends RecyclerView.Adapter<IngredientAddedAdapter.IngredientAddedViewHolder> {

    List<ingredient> ingredientList;
    private ClickIngredientItem clickIngredientItem;

    public IngredientAddedAdapter(List<ingredient> ingredientsList, ClickIngredientItem clickIngredientItem) {
        this.ingredientList = ingredientsList;
        this.clickIngredientItem = clickIngredientItem;
    }
    public void setFilteredList(List<ingredient> filteredList) {
        this.ingredientList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientAddedAdapter.IngredientAddedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredientadded_layout,parent,false);
        return new IngredientAddedAdapter.IngredientAddedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAddedAdapter.IngredientAddedViewHolder holder, int position) {
        ingredient in = ingredientList.get(position);
        holder.ingredientName.setText(in.getNameIngredient());
        holder.ingredientCalorie.setText(in.getCalorieIngredient());
        holder.ingredientQuantity.setText(in.getQuantity());
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickIngredientItem.onClickItemIngredient(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (ingredientList != null)
            return ingredientList.size();
        return 0;
    }

    public class IngredientAddedViewHolder extends RecyclerView.ViewHolder {

        private TextView ingredientName;
        private TextView ingredientCalorie;
        private TextView ingredientQuantity;
        private Button btnDelete;


        public IngredientAddedViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.nameIngredient);
            ingredientCalorie = itemView.findViewById(R.id.caloriesIngredient);
            ingredientQuantity = itemView.findViewById(R.id.quantityIngredient);
            btnDelete = itemView.findViewById(R.id.Delete_btn);
        }


    }
}
