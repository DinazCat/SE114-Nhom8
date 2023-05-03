package com.example.healthcareapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
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
    TextView username;
    PostInformation postInfo;
    BaivietFragment baivietFragment = new BaivietFragment();
    AddImgFragment addImgFragment = new AddImgFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

//        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.food);
//        Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(bitmap, 100);
//
//        ImageView circularImageView = findViewById(R.id.add);
//        circularImageView.setImageBitmap(circularBitmap);
        baivietBtn = findViewById(R.id.bvBtn);
        hinhanhBtn = findViewById(R.id.haBtn);
        userimg = findViewById(R.id.iv_user);
        username = findViewById(R.id.tv_username);
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
                startActivity(new Intent(PostActivity.this, MainActivity2.class));
            }
        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        db = FirebaseFirestore.getInstance();

        Button Postbtn =  findViewById(R.id.dang);
        Postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BaivietFragment.FName !="" || AddImgFragment.images.size() != 0)
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
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        listView = findViewById(R.id.listview);
//        Uri uri = data.getData();
//        if(uri != null) images.add(uri);
//        CustomAdapter1 adapter = new CustomAdapter1(this, images);
//        listView.setAdapter(adapter);
//    }
    private void addDatatoFirestore(){
        try{
            if(AddImgFragment.images.size() > 0)
            for(int i = 0; i < AddImgFragment.images.size(); i++) {
                uploadImage(AddImgFragment.images.get(i), i);
            }
            else {
                uploadpost();
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }

    }

     private void uploadImage(Uri filePath, int i) {
        if (filePath != null) {
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
                      uploadpost();
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
        postInfo.likes = 0;
        postInfo.comments = 0;
        postInfo.userimg = user.getPhotoUrl()!=null ? user.getPhotoUrl().toString() : "";
        postInfo.posttime = String.valueOf(System.currentTimeMillis());

        db.collection("posts")
                .add(postInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Log.d("TB", "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast
                                .makeText(PostActivity.this,
                                        "Post Uploaded!!",
                                        Toast.LENGTH_SHORT)
                                .show();
                        System.out.println("post success.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Log.w("TB", "Error adding document", e);
                        System.out.println("No post success");
                    }
                });

    }
}