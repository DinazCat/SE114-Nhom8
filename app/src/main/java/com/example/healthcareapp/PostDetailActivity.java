package com.example.healthcareapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.healthcareapp.Adapter.DetailimgAdapter;
import com.example.healthcareapp.Fragments.ProfileFragment;
import com.example.healthcareapp.Model.PostInformation;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailActivity extends AppCompatActivity {
    public static PostInformation info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        CircleImageView userimg = findViewById(R.id.userimg);
        TextView name = findViewById(R.id.username);
        TextView time = findViewById(R.id.time);
        ListView lv = findViewById(R.id.listview);
        TextView FName = findViewById(R.id.write);
        RatingBar FRate = findViewById(R.id.ratingbar);
        TextView FIngredient = findViewById(R.id.write1);
        TextView FMaking = findViewById(R.id.write2);
        TextView FSummary = findViewById(R.id.write3);
        try{
            Picasso.get().load(info.userimg).into(userimg);
        }
        catch (Exception e){
            String uri = "https://i.pinimg.com/originals/0c/3b/3a/0c3b3adb1a7530892e55ef36d3be6cb8.png";
            Picasso.get().load(uri).into(userimg);
        }
        name.setText(info.username);
        FName.setText(info.postFoodName);
        FIngredient.setText(info.postFoodIngredient);
        FMaking.setText(info.postFoodMaking);
        FSummary.setText(info.postFoodSummary);
        FRate.setRating(Float.parseFloat(info.postFoodRating));
        FRate.setEnabled(false);
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(info.posttime));
        String pTime = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
        time.setText(pTime);
        DetailimgAdapter adapter = new DetailimgAdapter(this, info.postimgs);
        lv.setAdapter(adapter);

        userimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = info.userid;
                ProfileFragment fragment = new ProfileFragment();
                Bundle args = new Bundle();
                args.putString("userId", userId);
                fragment.setArguments(args);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_layout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}