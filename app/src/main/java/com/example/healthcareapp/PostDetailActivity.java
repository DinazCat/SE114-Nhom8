package com.example.healthcareapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    TextView FTag, Cal, Prep, Cooking, Total;
    String userToken;
    Button save;
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
        Cal = findViewById(R.id.tv_cal);
        Prep = findViewById(R.id.tv_prep);
        Cooking = findViewById(R.id.tv_cooking);
        Total = findViewById(R.id.tv_servings);
        save = findViewById(R.id.savepost);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference userRef = FirebaseFirestore.getInstance().collection("SavedPosts").document(curUser.getUid());
                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                List<String> postIds = (List<String>) document.get("postIds");
                                if (postIds != null && postIds.contains(info.id)) {
                                    // postId đã tồn tại, xử lý tương ứng (ví dụ: hiển thị thông báo)
                                    Toast.makeText(PostDetailActivity.this, "Post already exist in your wall", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Thêm postId vào list postids
                                    postIds.add(info.id);
                                    userRef.update("postIds", postIds)
                                            .addOnSuccessListener(aVoid -> {
                                                // Thành công, xử lý tương ứng (ví dụ: hiển thị thông báo)
                                                Toast.makeText(PostDetailActivity.this, "Post is saved successfully in your wall", Toast.LENGTH_SHORT).show();
                                            })
                                            .addOnFailureListener(e -> {
                                                // Xảy ra lỗi, xử lý tương ứng (ví dụ: hiển thị thông báo lỗi)
                                                System.out.println("lỗi ở else t1");
                                            });
                                }
                            }
                            else {
                                List<String> postIds = new ArrayList<>();
                                postIds.add(info.id);
                                Map<String, Object> savep = new HashMap<>();
                                savep.put("postIds", postIds);
                                userRef.set(savep)
                                        .addOnSuccessListener(aVoid -> {
                                            // Thành công, xử lý tương ứng (ví dụ: hiển thị thông báo)
                                            Toast.makeText(PostDetailActivity.this, "Post is saved successfully in your wall", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            // Xảy ra lỗi, xử lý tương ứng (ví dụ: hiển thị thông báo lỗi)
                                            System.out.println("lỗi ở else t2");
                                        });

                            }
                        }

                    }
                });
            }
        });


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
                                    if(info.Ingredient.size()>0) {
                                        String Ingredient = "- " + info.Ingredient.get(0).name + " (" + info.Ingredient.get(0).wty + " " + info.Ingredient.get(0).unit + ")";
                                        int i = 1;
                                        while (i < info.Ingredient.size()) {
                                            Ingredient = Ingredient + "\n" + "- " + info.Ingredient.get(i).name + " (" + info.Ingredient.get(i).wty + " " + info.Ingredient.get(i).unit + ")";
                                            i=i+1;
                                        }
                                        FIngredient.setText(Ingredient);
                                    }
                                    else FIngredient.setText("");
                                    if(info.Hashtag.size()>0) {
                                        String Tags = "# " + info.Hashtag.get(0) ;
                                        int i = 1;
                                        while (i < info.Hashtag.size()) {
                                            Tags = Tags + "\n" + "# " + info.Hashtag.get(i) ;
                                            i=i+1;
                                        }
                                        FTag.setText(Tags);
                                    }
                                    else FTag.setText("");
                                    FMaking.setText(info.postFoodMaking);
                                    FSummary.setText(info.postFoodSummary);
                                    //FRate.setRating(Float.parseFloat(info.postFoodRating));
                                    Total.setText(info.Total);
                                    Prep.setText(info.Prep);
                                    Cooking.setText(info.Cooking);
                                    Cal.setText(info.Calories);
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
            if(info.Ingredient.size()>0) {
                String Ingredient = "- " + info.Ingredient.get(0).name + " (" + info.Ingredient.get(0).wty + " " + info.Ingredient.get(0).unit + ")";
                int i = 1;
                while (i < info.Ingredient.size()) {
                    Ingredient = Ingredient + "\n" + "- " + info.Ingredient.get(i).name + " (" + info.Ingredient.get(i).wty + " " + info.Ingredient.get(i).unit + ")";
                    i=i+1;
                }
                FIngredient.setText(Ingredient);
            }
            else FIngredient.setText("");
            if(info.Hashtag.size()>0) {
                String Tags = "# " + info.Hashtag.get(0) ;
                int i = 1;
                while (i < info.Hashtag.size()) {
                    Tags = Tags + "\n" + "# " + info.Hashtag.get(i) ;
                    i=i+1;
                }
                FTag.setText(Tags);
            }
            else FTag.setText("");
            FMaking.setText(info.postFoodMaking);
            FSummary.setText(info.postFoodSummary);
            //FRate.setRating(Float.parseFloat(info.postFoodRating));
            Total.setText(info.Total);
            Prep.setText(info.Prep);
            Cooking.setText(info.Cooking);
            Cal.setText(info.Calories);
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
