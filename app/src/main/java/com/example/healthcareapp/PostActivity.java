package com.example.healthcareapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.healthcareapp.Fragments.AddImgFragment;
import com.example.healthcareapp.Fragments.BaivietFragment;
import com.example.healthcareapp.Model.PostInformation;
import com.example.healthcareapp.service.FcmNotificationsSender;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;


public class PostActivity extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    ImageButton back;
    //Map<String, Object> user;
    List<String> fileimgs = new ArrayList<>();
    Button baivietBtn, hinhanhBtn;
    CircleImageView userimg;
    TextView username, header;
    PostInformation postInfo;
    public static String postIdtoUpdate;
    public static String thaotac;
    BaivietFragment baivietFragment = new BaivietFragment();
    AddImgFragment addImgFragment = new AddImgFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        baivietBtn = findViewById(R.id.bvBtn);
        hinhanhBtn = findViewById(R.id.haBtn);
        userimg = findViewById(R.id.iv_user);
        username = findViewById(R.id.tv_username);
        header = findViewById(R.id.header);
        if(thaotac.equals( "edit")) header.setText("Sửa bài viết");
        if(user.getPhotoUrl()!=null){
            Picasso.get().load(user.getPhotoUrl()).into(userimg);
        }
        else{
            String uri = "https://i.pinimg.com/originals/0c/3b/3a/0c3b3adb1a7530892e55ef36d3be6cb8.png";
            Picasso.get().load(uri).into(userimg);
        }
        if(user.getDisplayName()!=null)
            username.setText(user.getDisplayName());
        replaceFragment(baivietFragment);
        baivietBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                replaceFragment(baivietFragment);
               // System.out.println("ten1"+ BaivietFragment.FoodName.getText());

            }
        });
        hinhanhBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                replaceFragment(new AddImgFragment());

            }
        });
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostActivity.this, MainActivity.class));
            }
        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        db = FirebaseFirestore.getInstance();

        Button Postbtn =  findViewById(R.id.dang);
        Postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BaivietFragment.FoodName.getText().toString() !="" || AddImgFragment.images.size() != 0)
                {
                    addDatatoFirestore();
                }

            }
        });

    }
    private  void replaceFragment(Fragment f){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.addPframe,f);
        fragmentTransaction.commit();
    }

    private void addDatatoFirestore(){
        try{
            if(AddImgFragment.images.size() > 0){
                int sum = 0;
                for(int i = 0; i < AddImgFragment.images.size(); i++) {
                    if(AddImgFragment.images.get(i).toString().substring(0,5).equals("https"))
                    {
                        sum = sum + 1;
                        fileimgs.add(AddImgFragment.images.get(i).toString());
                    }
                    uploadImage(AddImgFragment.images.get(i), i);
                }
                if(sum == AddImgFragment.images.size()) editpost();
            }
            else {
                if(thaotac.equals("push"))
                    uploadpost();
                else editpost();
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }

    }

     private void uploadImage(Uri filePath, int i) {
        if (filePath != null) {
            if(filePath.toString().substring(0,5).equals("https"))return;
            String ImageName = filePath.toString();
            String  imgName = UUID.randomUUID().toString();
            String extension = ImageName.substring(ImageName.lastIndexOf('.'));

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + imgName);

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
//                                    Toast
//                                            .makeText(PostActivity.this,
//                                                    "Image Uploaded!!",
//                                                    Toast.LENGTH_SHORT)
//                                            .show();
                                    DownloadUrl(imgName, i);
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(PostActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });

        }
    }
    public void DownloadUrl(String filename, int i)
    {
        StorageReference islandRef = storageReference.child("images/"+filename);
        islandRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL
                fileimgs.add(uri.toString());
                if(i + 1 == AddImgFragment.images.size()) {
                    if(thaotac.equals("push"))
                        uploadpost();
                    else editpost();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                System.out.println(exception);
            }
        });

    }
    public void uploadpost(){
        postInfo = new PostInformation();
        postInfo.userid = user.getUid();
        postInfo.username = user.getDisplayName();
        postInfo.postFoodName = BaivietFragment.FoodName.getText().toString();
        postInfo.postFoodRating = BaivietFragment.FRating;
        postInfo.postFoodIngredient = BaivietFragment.Ingredient.getText().toString();
        postInfo.postFoodMaking = BaivietFragment.Making.getText().toString();
        postInfo.postFoodSummary = BaivietFragment.Summary.getText().toString();
        postInfo.postimgs = new ArrayList<>();
        postInfo.postimgs.addAll(fileimgs);
        postInfo.likes = new ArrayList<>();
        postInfo.comments = new ArrayList<>();
        postInfo.userimg = user.getPhotoUrl()!=null ? user.getPhotoUrl().toString() : "";
        postInfo.posttime = String.valueOf(System.currentTimeMillis());

        db.collection("posts")
                .add(postInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(PostActivity.this, "Post Uploaded!!", Toast.LENGTH_SHORT).show();
                        System.out.println("post success.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("No post success");
                    }
                });
        sendNotification();
    }
    public void editpost(){
        Map<String, Object> update = new HashMap<>();
        update.put("postFoodIngredient", BaivietFragment.Ingredient.getText().toString());
        update.put("postFoodMaking", BaivietFragment.Making.getText().toString());
        update.put("postFoodName", BaivietFragment.FoodName.getText().toString());
        update.put("postFoodRating", BaivietFragment.FRating);
        update.put("postFoodSummary", BaivietFragment.Summary.getText().toString());
        update.put("postimgs", fileimgs);

        db.collection("posts").document(postIdtoUpdate).set(update, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(PostActivity.this, "Post saved!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void sendNotification(){
        DatabaseReference followersRef = FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getUid()).child("followers");
        followersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String followerUserId = userSnapshot.getKey();
                    sendNotificationToUser(followerUserId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error);
            }
        });

    }
    private void sendNotificationToUser(String userId) {
        DatabaseReference tokensRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("token");
        tokensRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userToken = snapshot.getValue(String.class);
                if (userToken != null) {
                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                            userToken, "Social Food Blog", "The user you are following, " + user.getDisplayName() + ", just uploaded a new post.", getApplicationContext()
                    );
                    notificationsSender.sendNotification();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error);
            }
        });
    }
}