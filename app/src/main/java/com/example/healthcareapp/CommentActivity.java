package com.example.healthcareapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcareapp.Adapter.CommentAdapter;
import com.example.healthcareapp.Fragments.BlogFragment;
import com.example.healthcareapp.Model.Comment;
import com.example.healthcareapp.Model.Noti;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    ArrayList<String> commentIds;

    private EditText addComment;
    private CircleImageView imageProfile;
    private TextView post;

    private String postId;
    private String authorId;
    private String userToken;
    private String foodname;
    FirebaseUser curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        authorId = intent.getStringExtra("authorId");
        foodname = intent.getStringExtra("foodname");
        commentIds = intent.getStringArrayListExtra("commentIds");
        curUser = FirebaseAuth.getInstance().getCurrentUser();

        addComment = findViewById(R.id.add_comment);
        imageProfile = findViewById(R.id.image_profile);
        post = findViewById(R.id.post);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ImageButton btBack = findViewById(R.id.back);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(curUser.getPhotoUrl()!=null){

            Picasso.get().load(curUser.getPhotoUrl()).into(imageProfile);
        }
        else{
            String uri = "https://i.pinimg.com/originals/0c/3b/3a/0c3b3adb1a7530892e55ef36d3be6cb8.png";
            Picasso.get().load(uri).into(imageProfile);
        }


        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, commentList, postId);
        recyclerView.setAdapter(commentAdapter);

        getComment();
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(addComment.getText().toString())) {
                    Toast.makeText(CommentActivity.this, "No comment added!", Toast.LENGTH_SHORT).show();
                } else {
                    pushComment();
                }
            }
        });
    }

    private void getComment(){
        FirebaseFirestore.getInstance().collection("comments")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        commentList.clear();
                            for (DocumentSnapshot document : value.getDocuments()) {
                                if(commentIds.contains(document.getId())) {
                                    Comment comment = document.toObject(Comment.class);
                                    comment.setId(document.getId());
                                    commentList.add(comment);
                                }
                            }
                            commentAdapter.notifyDataSetChanged();
                    }
                });
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            commentList.clear();
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                if(commentIds.contains(document.getId())) {
//                                    Comment comment = document.toObject(Comment.class);
//                                    comment.setId(document.getId());
//                                    commentList.add(comment);
//                                }
//                            }
//                            commentAdapter.notifyDataSetChanged();
//                        } else {
//                            System.out.println(task.getException());
//                        }
//                    }
//                });
    }

    private void pushComment(){
        CollectionReference ref = FirebaseFirestore.getInstance().collection("comments");
        Comment comment = new Comment("", postId, addComment.getText().toString(), curUser.getUid());
        ref.add(comment).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    DocumentReference document = task.getResult();
                    String commentId = document.getId();
                    comment.setId(commentId);
                    commentList.add(comment);
                    commentAdapter.notifyDataSetChanged();
                    DocumentReference postRef = FirebaseFirestore.getInstance().collection("posts").document(postId);
                    postRef.update("comments", FieldValue.arrayUnion(commentId));
                    Toast.makeText(CommentActivity.this, "Comment added!", Toast.LENGTH_SHORT).show();
                    Noti item = new Noti();
                    item.PostownerId = authorId;
                    item.guestId = curUser.getUid();
                    item.classify = "cmt";
                    item.postid = postId;
                    item.message = " commented on your post about food: "+foodname;
                    item.Read = "no";
                    item.time = String.valueOf(System.currentTimeMillis());
                    if(!curUser.getUid().equals(authorId))
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

                    FirebaseDatabase.getInstance().getReference()
                            .child("notificationSetting")
                            .child(authorId)
                            .child("comment")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        sendNotification(authorId, comment.getComment(), foodname);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                } else {
                    Toast.makeText(CommentActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        addComment.setText("");

    }
    private void sendNotification(String userId, String comment, String foodname){
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
                        userToken, "Social Food Blog", FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + " commented on your post about " + foodname + ": " + comment, getApplicationContext()
                );
                notificationsSender.sendNotification();
            }
        }, 3000);
    }

}