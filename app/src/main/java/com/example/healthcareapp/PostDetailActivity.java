package com.example.healthcareapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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
    TextView name, countlike, countcmt ;
    TextView time ;
    ListView lv ;
    TextView FName;
    RatingBar FRate;
    TextView FIngredient ;
    TextView FMaking ;
    TextView FSummary ;
    ImageButton BtnLike;
    ImageButton BtnCmt;
    String userToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        CircleImageView userimg = findViewById(R.id.userimg);
         name = findViewById(R.id.username);
         time = findViewById(R.id.time);
         lv = findViewById(R.id.listview);
         FName = findViewById(R.id.write);
         FRate = findViewById(R.id.ratingbar);
         FIngredient = findViewById(R.id.write1);
         FMaking = findViewById(R.id.write2);
         FSummary = findViewById(R.id.write3);
         BtnLike = findViewById(R.id.btn_like);
         BtnCmt = findViewById(R.id.btn_comment);
         countcmt = findViewById(R.id.tv_cmts_count);
         countlike = findViewById(R.id.tv_likes_count);

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
                            FRate.setRating(Float.parseFloat(info.postFoodRating));
                            FRate.setEnabled(false);
                            Calendar calendar = Calendar.getInstance(Locale.getDefault());
                            calendar.setTimeInMillis(Long.parseLong(info.posttime));
                            String pTime = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
                            time.setText(pTime);
                            DetailimgAdapter adapter = new DetailimgAdapter(PostDetailActivity.this, info.postimgs);
                            lv.setAdapter(adapter);
                            countlike.setText("" + info.likes.size());
                            countcmt.setText("" + info.comments.size());
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
            FRate.setRating(Float.parseFloat(info.postFoodRating));
            FRate.setEnabled(false);
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTimeInMillis(Long.parseLong(info.posttime));
            String pTime = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
            time.setText(pTime);
            DetailimgAdapter adapter = new DetailimgAdapter(this, info.postimgs);
            lv.setAdapter(adapter);
            countlike.setText("" + info.likes.size());
            countcmt.setText("" + info.comments.size());

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
            DocumentReference postRef = FirebaseFirestore.getInstance().collection("posts").document(info.id);
            postRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        List<String> likes = (List<String>) documentSnapshot.get("likes");
                        if (likes != null && likes.contains(curUser.getUid())) {
                            BtnLike.setImageResource(R.drawable.red_heart);
                            countlike.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.red));
                            isliked = true;
                        } else {
                            BtnLike.setImageResource(R.drawable.heart);
                            countlike.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.black));
                        }

                    }
                }
            });
            BtnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DocumentReference postRef = FirebaseFirestore.getInstance().collection("posts").document(info.id);
                    if ( !isliked ) {
                        postRef.update("likes", FieldValue.arrayUnion(curUser.getUid())).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                info.likes.add(curUser.getUid());
                                BtnLike.setImageResource(R.drawable.red_heart);
                                countlike.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.red));
                                countlike.setText("" + info.likes.size());

                                sendNotification(info.userid);
                                Noti item = new Noti();
                                item.PostownerId = info.userid;
                                item.guestId = curUser.getUid();
                                item.classify = "like";
                                item.postid = info.id;
                                item.message = " đã thích bài viết của bạn về món ăn: " + info.postFoodName;
                                item.Read = "no";
                                item.time = String.valueOf(System.currentTimeMillis());
                                if ( !curUser.getUid().equals(info.userid) )
                                    FirebaseFirestore.getInstance().collection("Notification")
                                            .add(item)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    System.out.println("send noti success");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    System.out.println(e);
                                                }
                                            });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("Update failed");
                            }
                        });
                    } else {
                        postRef.update("likes", FieldValue.arrayRemove(curUser.getUid())).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                info.likes.remove(curUser.getUid());
                                BtnLike.setImageResource(R.drawable.heart);
                                countlike.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.black));
                                countlike.setText("" + info.likes.size());

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("Update failed");
                            }
                        });
                    }
                }
            });

            BtnCmt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PostDetailActivity.this, CommentActivity.class);
                    intent.putExtra("postId", info.id);
                    intent.putExtra("authorId", info.userid);
                    intent.putExtra("foodname", info.postFoodName);
                    startActivity(intent);

                }
            });
        }
    }

        private void sendNotification(String userId){

            FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userToken = snapshot.getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                            userToken, "Social Food Blog", FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + " liked your post!", getBaseContext()
                    );
                    notificationsSender.sendNotification();
                }
            }, 3000);
        }


}
