package com.example.healthcareapp;

import static com.example.healthcareapp.Fragments.AddImgFragment.PICK_IMAGE;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcareapp.Fragments.AddFragment;
import com.example.healthcareapp.Model.exercise;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class EditExerciseActivity extends AppCompatActivity {
    boolean imageCheck=true;
    public static Uri imagesNewFood;
    ImageButton cam, gal;
    LinearLayout chooseImg;
    exercise exerciseEdit;
    ImageButton img, btn_back;
    Button saveExercise;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    EditText etNameNewExercise, etCalorieNewExercise, etTimeNewExercise;
    DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exercise);
        saveExercise = findViewById(R.id.saveFood2);
        ImageButton Addimg = findViewById(R.id.imgpicker);
        cam = findViewById(R.id.camera);
        gal = findViewById(R.id.gallery);
        chooseImg = findViewById(R.id.choose);
        img = findViewById(R.id.imgpicker);
        btn_back = findViewById(R.id.back);

        Addimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chooseImg.getVisibility()==View.INVISIBLE)
                    chooseImg.setVisibility(View.VISIBLE);
                else chooseImg.setVisibility(View.INVISIBLE);


            }
        });
        System.out.println(imagesNewFood+"size");
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImg.setVisibility(View.INVISIBLE);
                ImagePicker.Companion.with(EditExerciseActivity.this)
                        .cameraOnly()
                        .maxResultSize(1080,1080)
                        .start(101);
            }
        });

        Intent i = getIntent();
        String idExercise = i.getStringExtra("id");
        database = FirebaseDatabase.getInstance().getReference("newExerciseUserAdd");
        database.child(uid).child(idExercise).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                exerciseEdit = snapshot.getValue(exercise.class);
                etNameNewExercise.setText(exerciseEdit.getNameExercise());
                etCalorieNewExercise.setText(exerciseEdit.getCaloriesBurnedAMin());
                etTimeNewExercise.setText(exerciseEdit.getMinutePerformed());
                imagesNewFood = Uri.parse(exerciseEdit.getImgExercise());
                img.setImageURI(Uri.parse(exerciseEdit.getImgExercise()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        etNameNewExercise = findViewById(R.id.addExerciseName);
        etCalorieNewExercise = findViewById(R.id.addExerciseCalorie);
        etTimeNewExercise = findViewById(R.id.addExerciseTime);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditExerciseActivity.this, TopTabExerciseActivity.class);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        saveExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LanguageUtils.getCurrentLanguage() == Language.ENGLISH) {
                    if (etCalorieNewExercise.getText().toString().trim().isEmpty() || etNameNewExercise.getText().toString().trim().isEmpty()
                            || etTimeNewExercise.getText().toString().trim().isEmpty() || imageCheck==false) {
                        Toast.makeText(EditExerciseActivity.this, "Please Enter Full", Toast.LENGTH_SHORT).show();
                    } else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(EditExerciseActivity.this);
                        dialog.setTitle("Save");
                        dialog.setIcon(R.drawable.noti_icon);
                        dialog.setMessage("You want to save??");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                exercise e = new exercise();
                                e.setIdExercise(idExercise);
                                e.setNameExercise(etNameNewExercise.getText().toString().trim());
                                e.setCaloriesBurnedAMin(etCalorieNewExercise.getText().toString().trim());
                                e.setImgExercise(String.valueOf(imagesNewFood));
                                e.setMinutePerformed(etTimeNewExercise.getText().toString().trim());

                                DatabaseReference database1 = FirebaseDatabase.getInstance().getReference("newExerciseUserAdd");
                                database1.child(uid).child(idExercise).setValue(e);

                                Toast.makeText(EditExerciseActivity.this, "Update Exercise Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EditExerciseActivity.this, TopTabExerciseActivity.class);
                                setResult(RESULT_OK, intent);
                                finish();
                            }

                        });
                        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = dialog.create();
                        // Show the Alert Dialog box
                        alertDialog.show();

                    }
                }
                else {
                    if (etCalorieNewExercise.getText().toString().trim().isEmpty() || etNameNewExercise.getText().toString().trim().isEmpty()
                            || etTimeNewExercise.getText().toString().trim().isEmpty() || imageCheck == false) {
                        Toast.makeText(EditExerciseActivity.this, "Vui lòng điền đủ thông tin phần thức ăn ", Toast.LENGTH_SHORT).show();
                    } else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(EditExerciseActivity.this);
                        dialog.setTitle("Lưu");
                        dialog.setIcon(R.drawable.noti_icon);
                        dialog.setMessage("Bạn có muốn lưu??");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                exercise e = new exercise();
                                e.setIdExercise(idExercise);
                                e.setNameExercise(etNameNewExercise.getText().toString().trim());
                                e.setCaloriesBurnedAMin(etCalorieNewExercise.getText().toString().trim());
                                e.setImgExercise(String.valueOf(imagesNewFood));
                                e.setMinutePerformed(etTimeNewExercise.getText().toString().trim());

                                database = FirebaseDatabase.getInstance().getReference("newExerciseUserAdd");
                                database.child(uid).child(idExercise).setValue(exerciseEdit);

                                Toast.makeText(EditExerciseActivity.this, "Update Exercise Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EditExerciseActivity.this, TopTabExerciseActivity.class);
                                setResult(RESULT_OK, intent);
                                finish();
                                Toast.makeText(EditExerciseActivity.this, "Cập nhât hoạt động mới thành công", Toast.LENGTH_SHORT).show();

                            }

                        });
                        dialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = dialog.create();
                        // Show the Alert Dialog box
                        alertDialog.show();
                    }
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        imageCheck=true;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 101 && data != null) {
            if (data != null) {
                Uri uri = data.getData();
                if ( uri != null ) imagesNewFood= uri;

            }
        }
        else  if (requestCode == PICK_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                if ( uri != null ) imagesNewFood=uri;

            }
        }
        img.setImageURI(imagesNewFood);
    }
    private void delete(String idExercise) {
        database = FirebaseDatabase.getInstance().getReference("newExerciseUserAdd");
        database.child(uid).child(idExercise).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}