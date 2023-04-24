package com.example.healthcareapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.UUID;

public class EditProfileFragment extends Fragment {
    EditText etName, etAbout, etEmail, etPhone, etCountry, etCity;
    Button btSave;
    ImageView image;
    Uri imageUri;
    String imgFile;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    FirebaseAuth auth;
    FirebaseUser user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        etName = view.findViewById(R.id.et_name);
        etAbout = view.findViewById(R.id.et_about);
        etEmail= view.findViewById(R.id.et_email);
        etPhone = view.findViewById(R.id.et_phone);
        etCity = view.findViewById(R.id.et_city);
        etCountry = view.findViewById(R.id.et_country);
        btSave = view.findViewById(R.id.bt_save);
        image = view.findViewById(R.id.img_user);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseReference = database.getReference("users");
        Query query = databaseReference.orderByChild("id").equalTo(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    String name = ds.child("name").getValue() + "";
                    String about = ds.child("about").getValue() + "";
                    String img = ds.child("img").getValue() + "";
                    String city = ds.child("city").getValue() + "";
                    String country = ds.child("country").getValue() + "";
                    String email = ds.child("email").getValue() + "";
                    String phone = ds.child("phone").getValue() + "";
                    etName.setText(name);
                    etAbout.setText(about);
                    etCity.setText(city);
                    etCountry.setText(country);
                    etEmail.setText(email);
                    etPhone.setText(phone);
                    try {
                        Picasso.get().load(img).into(image);
                        imgFile = img;
                    } catch (Exception e){
                        String uri = "https://i.pinimg.com/originals/0c/3b/3a/0c3b3adb1a7530892e55ef36d3be6cb8.png";
                        Picasso.get().load(uri).into(image);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageUri != null) {
                    uploadImage(imageUri);
                }
                else {
                    updateProfile();
                }

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(EditProfileFragment.this)
                        .crop()
                        .maxResultSize(1080,1080)
                        .start(101);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 101 && data != null) {
            Uri uri = data.getData();
            if (uri != null) imageUri = uri;
            try {
                Picasso.get().load(imageUri).into(image);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private void uploadImage(Uri filePath) {
        if (filePath != null) {
            String ImageName = filePath.toString();
            String  imgName = UUID.randomUUID().toString();
            String extension = ImageName.substring(ImageName.lastIndexOf('.'));

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref = storageReference.child("images/" + imgName);

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath).addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    progressDialog.dismiss();
                                    DownloadUrl(imgName);
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                }
                            });

        }
    }
    public void DownloadUrl(String filename)
    {
        StorageReference islandRef = storageReference.child("images/"+filename);
        islandRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL
                imgFile = uri.toString();
                updateProfile();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                System.out.println(exception);
            }
        });
    }
    private void updateProfile(){
        String uid = user.getUid();
        String email = etEmail.getText().toString();
        String name = etName.getText().toString();
        String about = etAbout.getText().toString();
        String phone = etPhone.getText().toString();
        String city = etCity.getText().toString();
        String country = etCountry.getText().toString();
        String img = imgFile;
        HashMap<Object,String> hashMap = new HashMap<>();
        hashMap.put("id", uid);
        hashMap.put("email", email);
        hashMap.put("name", name);
        hashMap.put("img", img);
        hashMap.put("phone", phone);
        hashMap.put("city", city);
        hashMap.put("country", country);
        hashMap.put("about", about);

        databaseReference.child(uid).setValue(hashMap);
    }
}