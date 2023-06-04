package com.example.healthcareapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.healthcareapp.Fragments.AddFragment;
import com.example.healthcareapp.Fragments.GraphFragment;
import com.example.healthcareapp.ListInterface.ClickFoodItem;
import com.example.healthcareapp.Model.food;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<food> foodList;
    DatabaseReference database;
    private ClickFoodItem clickFoodItem;


    public FoodAdapter( List<food> foodList, ClickFoodItem clickFoodItem) {
        this.foodList = foodList;
        this.clickFoodItem = clickFoodItem;
    }
    public void setFilteredList(List<food> filteredList) {
        this.foodList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item,parent,false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        final food _food = foodList.get(position);
        if (_food == null) return;

        holder.tvNameFood.setText(_food.getNameFood());
        holder.tvCaloriesFood.setText(_food.getCaloriesFood());
        holder.tvServing.setText(_food.getServingFood());
        holder.btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickFoodItem.onClickItemFood(_food);
            }
        });
    }

    private void onClickGoToDetail (food _food) {

    }
    @Override
    public int getItemCount() {
        if (foodList != null)
            return foodList.size();
        return 0;
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imgFood;
        private TextView tvNameFood;
        private TextView tvCaloriesFood;
        private TextView tvServing;
        private Button btAdd;
        Spinner spinner;
        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imageFood);
            tvNameFood = itemView.findViewById(R.id.nameFood);
            tvCaloriesFood = itemView.findViewById(R.id.caloriesFood);
            tvServing = itemView.findViewById(R.id.servingFood);
            btAdd= itemView.findViewById(R.id.Add_btn);
        }


    }
}