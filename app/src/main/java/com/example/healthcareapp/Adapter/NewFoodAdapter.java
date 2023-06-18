package com.example.healthcareapp.Adapter;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.ListInterface.ClickNewFoodItem;
import com.example.healthcareapp.ListInterface.ClickRecipeItem;
import com.example.healthcareapp.Model.food;
import com.example.healthcareapp.Model.recipe;
import com.example.healthcareapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;

public class NewFoodAdapter extends RecyclerView.Adapter<NewFoodAdapter.NewFoodViewHolder>{
    List<food> newFoodList;

    public NewFoodAdapter(List<food> newFoodList) {
        this.newFoodList = newFoodList;

    }
    public void setFilteredList(List<food> filteredList) {
        this.newFoodList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewFoodAdapter.NewFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_food_item,parent,false);
        return new NewFoodAdapter.NewFoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewFoodAdapter.NewFoodViewHolder holder, int position) {
        food _food = newFoodList.get(position);
        holder.newFoodName.setText(_food.getNameFood());
        holder.newFoodCalorie.setText(_food.getCaloriesFood());
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                // Xóa trong recycle view new food
                DatabaseReference database = FirebaseDatabase.getInstance().getReference("newFoodUserAdd");
                database.child(uid).child(_food.getIdFood()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().removeValue();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                // Xóa trong recycle view food khong cho add nữa
                DatabaseReference database1 = FirebaseDatabase.getInstance().getReference("foods");
                database1.child(_food.getIdFood()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().removeValue();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        if (newFoodList != null)
            return newFoodList.size();
        return 0;
    }

    public class NewFoodViewHolder extends RecyclerView.ViewHolder {

        private TextView newFoodName;
        private TextView newFoodCalorie;
        private Button btnDelete;


        public NewFoodViewHolder(@NonNull View itemView) {
            super(itemView);
            newFoodName = itemView.findViewById(R.id.nameNewFood);
            newFoodCalorie = itemView.findViewById(R.id.caloriesNewFood);
            btnDelete = itemView.findViewById(R.id.Delete_btn);
        }


    }
}


