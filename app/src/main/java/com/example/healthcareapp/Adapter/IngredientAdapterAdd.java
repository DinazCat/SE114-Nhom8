package com.example.healthcareapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.healthcareapp.ListInterface.ClickFoodItem;
import com.example.healthcareapp.ListInterface.ClickIngredientItem;
import com.example.healthcareapp.Model.food;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Model.ingredient;
import com.example.healthcareapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class IngredientAdapterAdd extends RecyclerView.Adapter<IngredientAdapterAdd.IngredientAddViewHolder> {

    List<ingredient> ingredientList;
    private ClickIngredientItem clickIngredientItem;

    public IngredientAdapterAdd(List<ingredient> ingredientsList, ClickIngredientItem clickIngredientItem) {
        this.ingredientList = ingredientsList;
        this.clickIngredientItem = clickIngredientItem;
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
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickIngredientItem.onClickItemIngredient(in);
            }
        });
        //holder.btnShare.setOnClickListener(new View.OnClickListener() {
        //@Override
        //public void onClick(View v) {
        //    clickIngredientItem.onClickItemShareIngredient(in);
        //}
        //});
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
        private Button btnAdd, btnShare;


        public IngredientAddViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.nameIngredient);
            ingredientCalorie = itemView.findViewById(R.id.caloriesIngredient);
            btnAdd = itemView.findViewById(R.id.Add_btn);
            //btnShare = itemView.findViewById(R.id.Share_btn);
        }


    }
}