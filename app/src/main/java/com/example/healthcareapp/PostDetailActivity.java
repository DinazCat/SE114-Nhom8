package com.example.healthcareapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.healthcareapp.Adapter.DetailimgAdapter;
import com.example.healthcareapp.Adapter.PostAdapter;
import com.example.healthcareapp.Fragments.ProfileFragment;
import com.example.healthcareapp.Model.Noti;
import com.example.healthcareapp.Model.PostInformation;
import com.example.healthcareapp.service.FcmNotificationsSender;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailActivity extends AppCompatActivity {
    public static PostInformation info;
    private boolean isliked = false;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser curUser = auth.getCurrentUser();
    TextView name;
    TextView time ;
    ListView lv ;
    TextView FName;
    //RatingBar FRate;
    TextView FRate;
    TextView FIngredient ;
    TextView FMaking ;
    TextView FSummary ;
    TextView FTag ;
    String userToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        CircleImageView userimg = findViewById(R.id.userimg);
         name = findViewById(R.id.username);
         time = findViewById(R.id.time);
         lv = findViewById(R.id.listview);
         FName = findViewById(R.id.tv_foodname);
         //FRate = findViewById(R.id.ratingbar);
         FRate = findViewById(R.id.tv_difficulty);
         FIngredient = findViewById(R.id.tv_ingredients);
         FMaking = findViewById(R.id.tv_steps);
         FSummary = findViewById(R.id.tv_summary);
         FTag = findViewById(R.id.tv_tag);

        Uri data = getIntent().getData();
        if (data != null && data.getScheme().equals("https")) {
            String postId = data.getQueryParameter("id");
            FirebaseFirestore.getInstance().collection("posts").document(postId).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            info = new PostInformation();
                            info = document.toObject(PostInformation.class);
                            info.id = document.getId();
                            try {
                                Picasso.get().load(info.userimg).into(userimg);
                            } catch (Exception e) {
                                String uri = "https://i.pinimg.com/originals/0c/3b/3a/0c3b3adb1a7530892e55ef36d3be6cb8.png";
                                Picasso.get().load(uri).into(userimg);
                            }
                            name.setText(info.username);
                            FName.setText(info.postFoodName);
                            FIngredient.setText(info.postFoodIngredient);
                            FMaking.setText(info.postFoodMaking);
                            FSummary.setText(info.postFoodSummary);
                            //FRate.setRating(Float.parseFloat(info.postFoodRating));
                            FRate.setText(info.postFoodRating);
                            FRate.setEnabled(false);
                            Calendar calendar = Calendar.getInstance(Locale.getDefault());
                            calendar.setTimeInMillis(Long.parseLong(info.posttime));
                            String pTime = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
                            time.setText(pTime);
                            DetailimgAdapter adapter = new DetailimgAdapter(PostDetailActivity.this, info.postimgs);
                            lv.setAdapter(adapter);
                        } else {
                            System.out.println("Post does not exists");
                        }
                    } else {
                        System.out.println(task.getException());
                    }
                }
            });
        } else {
            try {
                Picasso.get().load(info.userimg).into(userimg);
            } catch (Exception e) {
                String uri = "https://i.pinimg.com/originals/0c/3b/3a/0c3b3adb1a7530892e55ef36d3be6cb8.png";
                Picasso.get().load(uri).into(userimg);
            }
            name.setText(info.username);
            FName.setText(info.postFoodName);
            FIngredient.setText(info.postFoodIngredient);
            FMaking.setText(info.postFoodMaking);
            FSummary.setText(info.postFoodSummary);
            //FRate.setRating(Float.parseFloat(info.postFoodRating));
            FRate.setText(info.postFoodRating);
            FRate.setEnabled(false);
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTimeInMillis(Long.parseLong(info.posttime));
            String pTime = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
            time.setText(pTime);
            DetailimgAdapter adapter = new DetailimgAdapter(this, info.postimgs);
            lv.setAdapter(adapter);
            ViewGroup.LayoutParams layoutParams = lv.getLayoutParams();
            layoutParams.height = 705 * adapter.getCount();
            lv.setLayoutParams(layoutParams);

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

}
