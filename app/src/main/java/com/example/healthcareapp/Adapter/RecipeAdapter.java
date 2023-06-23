package com.example.healthcareapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Fragments.AddFragment;
import com.example.healthcareapp.Fragments.AddRecipeFragment;
import com.example.healthcareapp.ListInterface.ClickIngredientItem;
import com.example.healthcareapp.ListInterface.ClickRecipeItem;
import com.example.healthcareapp.Model.ingredient;
import com.example.healthcareapp.Model.note;
import com.example.healthcareapp.Model.recipe;
import com.example.healthcareapp.NoteActivity;
import com.example.healthcareapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>{
    List<recipe> recipeList;

    public RecipeAdapter(List<recipe> recipeList, Context mainContext) {
        this.recipeList = recipeList;
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


            }
        });
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
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference("newRecipe");
                        database.child(uid).child(re.getIdRecipe()).addListenerForSingleValueEvent(new ValueEventListener() {
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
