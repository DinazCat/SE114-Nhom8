package com.example.healthcareapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.healthcareapp.Model.exercise;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private List<exercise> exerciseList;

    public ExerciseAdapter(List<exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }
    public void setFilteredList(List<exercise> filteredList) {
        this.exerciseList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item,parent,false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        exercise e = exerciseList.get(position);
        if (e == null) return;
        //holder.imgFood.setImageResource(e.getImgFood());
        holder.tvNameExercise.setText(e.getNameExercise());
        holder.tvCaloriesExercise.setText(e.getCaloriesBurnedAMin());
        holder.tvMinutePerformedExercise.setText(e.getMinutePerformed());
    }

    @Override
    public int getItemCount() {
        if (exerciseList != null)
            return exerciseList.size();
        return 0;
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imgExercise;
        private TextView tvNameExercise;
        private TextView tvCaloriesExercise;
        private TextView tvMinutePerformedExercise;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            imgExercise = itemView.findViewById(R.id.imageExercise);
            tvNameExercise = itemView.findViewById(R.id.nameExercise);
            tvCaloriesExercise = itemView.findViewById(R.id.caloriesBurnedAMin);
            tvMinutePerformedExercise = itemView.findViewById(R.id.minutePerformed);
        }


    }
}