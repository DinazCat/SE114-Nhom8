package com.example.healthcareapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.healthcareapp.Model.food;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<food> foodList;

    public FoodAdapter(List<food> foodList) {
        this.foodList = foodList;
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
        food _food = foodList.get(position);
        if (_food == null) return;
        //holder.imgFood.setImageResource(_food.getImgFood());
        holder.tvNameFood.setText(_food.getNameFood());
        holder.tvCaloriesFood.setText(_food.getCaloriesFood());
        holder.tvServing.setText(_food.getServingFood());
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

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imageFood);
            tvNameFood = itemView.findViewById(R.id.nameFood);
            tvCaloriesFood = itemView.findViewById(R.id.caloriesFood);
            tvServing = itemView.findViewById(R.id.servingFood);
        }


    }
}