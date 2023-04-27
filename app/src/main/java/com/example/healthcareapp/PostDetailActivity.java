package com.example.healthcareapp;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PostDetailActivity extends AppCompatActivity {
public static PostInformation info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        TextView time = findViewById(R.id.time);
        ListView lv = findViewById(R.id.listview);
        TextView FName = findViewById(R.id.write);
        RatingBar FRate = findViewById(R.id.ratingbar);
        TextView FIngredient = findViewById(R.id.write1);
        TextView FMaking = findViewById(R.id.write2);
        TextView FSummary = findViewById(R.id.write3);
        FName.setText(info.postFoodName);
        FIngredient.setText(info.postFoodIngredient);
        FMaking.setText(info.postFoodMaking);
        FSummary.setText(info.postFoodSummary);
        FRate.setRating(Float.parseFloat(info.postFoodRating));
        time.setText(info.posttime);
        CustomAdapter2 adapter = new CustomAdapter2(this, info.postimgs);
        lv.setAdapter(adapter);
    }
}