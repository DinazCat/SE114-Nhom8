package com.example.healthcareapp.Adapter;

import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.ListInterface.ClickFoodItem;
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

import org.checkerframework.checker.units.qual.C;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class NewFoodAdapter extends RecyclerView.Adapter<NewFoodAdapter.NewFoodViewHolder>{
    List<food> newFoodList;
    ClickFoodItem clickFoodItem;

    public NewFoodAdapter(List<food> newFoodList, ClickFoodItem clickFoodItem) {
        this.newFoodList = newFoodList;
        this.clickFoodItem = clickFoodItem;

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
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                dialog.setTitle("Delete");
                dialog.setIcon(R.drawable.noti_icon);
                dialog.setMessage("You want to delete??");
                dialog.setCancelable(false);
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                // Show the Alert Dialog box
                alertDialog.show();
            }
        });
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickFoodItem.onClickItemFood(_food);
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
        private ImageButton btnDelete, btnEdit;


        public NewFoodViewHolder(@NonNull View itemView) {
            super(itemView);
            newFoodName = itemView.findViewById(R.id.nameNewFood);
            newFoodCalorie = itemView.findViewById(R.id.caloriesNewFood);
            btnDelete = itemView.findViewById(R.id.Delete_btn);
            btnEdit = itemView.findViewById(R.id.Edit_btn);
        }


    }
}


