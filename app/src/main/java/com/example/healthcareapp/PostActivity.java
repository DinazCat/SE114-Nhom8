package com.example.healthcareapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.healthcareapp.Fragments.Fragment_baiviet1;
import com.example.healthcareapp.Fragments.Fragment_baiviet2;
import com.example.healthcareapp.Model.Noti;
import com.example.healthcareapp.Model.PostInformation;
import com.example.healthcareapp.Model.recipe;
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
import com.google.firebase.firestore.CollectionReference;
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
    public static recipe re;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    ImageButton back;
    //Map<String, Object> user;
    List<String> fileimgs = new ArrayList<>();
    Button backBtn, nextBtn;
    CircleImageView userimg;
    TextView username, header;
    PostInformation postInfo;
    public static String postIdtoUpdate;
    public static String thaotac;
    BaivietFragment baivietFragment = new BaivietFragment();
    AddImgFragment addImgFragment = new AddImgFragment();
    Fragment_baiviet1 baivietFragment1 = new Fragment_baiviet1();
    Fragment_baiviet2 baivietFragment2 = new Fragment_baiviet2();
    int page = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        backBtn = findViewById(R.id.backBtn);
        nextBtn = findViewById(R.id.nextBtn);
//        userimg = findViewById(R.id.iv_user);
//        username = findViewById(R.id.tv_username);
        re = getIntentonshare();
        header = findViewById(R.id.header);
        if(thaotac.equals( "edit")) {
            header.setText("Sửa bài viết");
        }
//        if(user.getPhotoUrl()!=null){
//            Picasso.get().load(user.getPhotoUrl()).into(userimg);
//        }
//        else{
//            String uri = "https://i.pinimg.com/originals/0c/3b/3a/0c3b3adb1a7530892e55ef36d3be6cb8.png";
//            Picasso.get().load(uri).into(userimg);
//        }
//        if(user.getDisplayName()!=null)
//            username.setText(user.getDisplayName());
        replaceFragment(baivietFragment);
        backBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(page > 0) page = page - 1;
                if(page == 0);
                replaceFragment(baivietFragment);
                if(page == 1)
                    replaceFragment(baivietFragment1);
                if(page == 2)
                    replaceFragment(baivietFragment2);
                if(page == 3)
                    replaceFragment(new AddImgFragment());

            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(page < 3) page = page + 1;
                if(page == 0);
                replaceFragment(baivietFragment);
                if(page == 1)
                    replaceFragment(baivietFragment1);
                if(page == 2)
                    replaceFragment(baivietFragment2);
                if(page == 3)
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
                if(BaivietFragment.FoodName.getText().toString() != null &&
                        BaivietFragment.Total.getText().toString() != null &&
                        BaivietFragment.Cal.getText().toString() != null &&
                        BaivietFragment.Prep.getText().toString() != null &&
                        BaivietFragment.Cooking.getText().toString() != null &&
                        BaivietFragment.FRating != ""&&
                        Fragment_baiviet1.making != null &&
                        Fragment_baiviet1.making.getText().toString() != null &&
                        Fragment_baiviet1.listIdata.size() > 0)
                {
                    addDatatoFirestore();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please enter post's information!", Toast.LENGTH_LONG);
                }

            }
        });

    }
    private boolean Checkdone(PostInformation p){
        if(p.postFoodName != "" && p.postFoodRating != "" && p.postFoodMaking != "" && p.Calories != "" && p.Prep !="" && p.Cooking != "" && p.Total != "" && p.Ingredient.size() > 0)
        {
            return true;
        }
        return false;
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
    String postIdtoNoti = "";
    public void uploadpost(){
        postInfo = new PostInformation();
        postInfo.userid = user.getUid();
        postInfo.username = user.getDisplayName();
        postInfo.postFoodName = BaivietFragment.FoodName.getText().toString();
        postInfo.postFoodRating = BaivietFragment.FRating;
        postInfo.Total = BaivietFragment.Total.getText().toString();
        postInfo.Calories = BaivietFragment.Cal.getText().toString();
        postInfo.Prep = BaivietFragment.Prep.getText().toString();
        postInfo.Cooking = BaivietFragment.Cooking.getText().toString();
        postInfo.postFoodMaking = Fragment_baiviet1.making.getText().toString();
        postInfo.postFoodSummary = Fragment_baiviet1.summary.getText().toString();
        postInfo.Ingredient = Fragment_baiviet1.listIdata;
        postInfo.Hashtag = Fragment_baiviet2.hashtags;
        postInfo.postimgs = new ArrayList<>();
        postInfo.postimgs.addAll(fileimgs);
        postInfo.likes = new ArrayList<>();
        postInfo.comments = new ArrayList<>();
        postInfo.userimg = user.getPhotoUrl()!=null ? user.getPhotoUrl().toString() : "";
        postInfo.posttime = String.valueOf(System.currentTimeMillis());

        CollectionReference collectionRef=db.collection("posts");
        collectionRef.add(postInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(PostActivity.this, "Post Uploaded!!", Toast.LENGTH_SHORT).show();
                        System.out.println("post success.");
                        postIdtoNoti = documentReference.getId();
                        Map<String, Object> update = new HashMap<>();
                        update.put("id", documentReference.getId());
                        db.collection("posts").document(documentReference.getId()).set(update, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(PostActivity.this, "update post id success", Toast.LENGTH_SHORT).show();
                            }
                        });
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
        update.put("Ingredient", Fragment_baiviet1.listIdata);
        update.put("postFoodMaking", Fragment_baiviet1.making.getText().toString());
        update.put("postFoodName", BaivietFragment.FoodName.getText().toString());
        update.put("postFoodRating", BaivietFragment.FRating);
        update.put("Total", BaivietFragment.Total.getText().toString());
        update.put("Prep", BaivietFragment.Prep.getText().toString());
        update.put("Cooking", BaivietFragment.Cooking.getText().toString());
        update.put("Calories", BaivietFragment.Cal.getText().toString());
        update.put("postFoodSummary", Fragment_baiviet1.summary.getText().toString());
        update.put("Hashtag", Fragment_baiviet2.hashtags);
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

                    FirebaseDatabase.getInstance().getReference()
                            .child("notificationSetting")
                            .child(followerUserId)
                            .child("newpost")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        sendNotificationToUser(followerUserId);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                    //noti screen
                    Noti item = new Noti();
                    item.PostownerId = followerUserId;
                    item.guestId = user.getUid();
                    item.classify = "post";
                    item.postid = postIdtoNoti;
                    item.message = " just uploaded a new post: "+BaivietFragment.FoodName;
                    item.Read = "no";
                    item.time = String.valueOf(System.currentTimeMillis());
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
    private recipe getIntentonshare() {
        recipe _re = new recipe();
        Intent i = getIntent();
        String s = "";
        s = i.getStringExtra("idRecipe");
        _re.setIdRecipe(s);

        s = i.getStringExtra("nameRecipe");
        _re.setNameRecipe(s);

        s = i.getStringExtra("caloRecipe");
        _re.setCalorieRecipe(s);

        s = i.getStringExtra("cookingRecipe");
        _re.setCooking(s);

        s = i.getStringExtra("prepRecipe");
        _re.setPrep(s);
        return _re;
    }
}