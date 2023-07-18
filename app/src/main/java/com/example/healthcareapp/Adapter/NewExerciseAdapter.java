package com.example.healthcareapp.Adapter;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Fragments.SearchFoodFragment;
import com.example.healthcareapp.Language;
import com.example.healthcareapp.LanguageUtils;
import com.example.healthcareapp.ListInterface.ClickExerciseItem;
import com.example.healthcareapp.ListInterface.ClickFoodItem;
import com.example.healthcareapp.Model.exercise;
import com.example.healthcareapp.Model.food;
import com.example.healthcareapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewExerciseAdapter extends RecyclerView.Adapter<NewExerciseAdapter.NewExerciseViewHolder>{
    List<exercise> newExerciseList;
    ClickExerciseItem clickExerciseItem;


    public NewExerciseAdapter(List<exercise> newFoodList, ClickExerciseItem clickFoodItem) {
        this.newExerciseList = newFoodList;
        this.clickExerciseItem = clickExerciseItem;
    }
    public void setFilteredList(List<exercise> filteredList) {
        this.newExerciseList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewExerciseAdapter.NewExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_food_item,parent,false);
        return new NewExerciseAdapter.NewExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewExerciseAdapter.NewExerciseViewHolder holder, int position) {
        exercise e = newExerciseList.get(position);
        if (e == null) return;
        if (e.getImgExercise()!=null || e.getImgExercise()!="null") {
            Picasso.get().load(e.getImgExercise()).into(holder.imgExercise);
        }
        holder.newExerciseName.setText(e.getNameExercise());
        holder.newExerciseCalorie.setText(e.getCaloriesBurnedAMin());
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LanguageUtils.getCurrentLanguage() == Language.ENGLISH) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                    dialog.setTitle("Delete");
                    dialog.setIcon(R.drawable.noti_icon);
                    dialog.setMessage("You want to delete??");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            for (int i=0; i<SearchFoodFragment.foodList.size();i++){
                                if (SearchFoodFragment.foodList.get(i).getIdFood()==e.getIdExercise())
                                    SearchFoodFragment.foodList.remove(i);
                                SearchFoodFragment.foodAdapter.notifyDataSetChanged();
                            }
                            // Xóa trong recycle view new food
                            DatabaseReference database = FirebaseDatabase.getInstance().getReference("newFoodUserAdd");
                            database.child(uid).child(e.getIdExercise()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                    dialog.setTitle("Xóa");
                    dialog.setIcon(R.drawable.noti_icon);
                    dialog.setMessage("Bạn có muốn xóa??");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            // Xóa trong recycle view new food
                            DatabaseReference database = FirebaseDatabase.getInstance().getReference("newFoodUserAdd");
                            database.child(uid).child(e.getIdExercise()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                    dialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = dialog.create();
                    // Show the Alert Dialog box
                    alertDialog.show();
                }
            }
        });
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickExerciseItem.onClickItemExercise(e);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (newExerciseList != null)
            return newExerciseList.size();
        return 0;
    }

    public class NewExerciseViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView imgExercise;
        private TextView newExerciseName;
        private TextView newExerciseCalorie;
        private ImageButton btnDelete, btnEdit;


        public NewExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            imgExercise = itemView.findViewById(R.id.imageFood);
            newExerciseName = itemView.findViewById(R.id.nameNewFood);
            newExerciseCalorie = itemView.findViewById(R.id.caloriesNewFood);
            btnDelete = itemView.findViewById(R.id.Delete_btn);
            btnEdit = itemView.findViewById(R.id.Edit_btn);
        }


    }

}


