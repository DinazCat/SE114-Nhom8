package com.example.healthcareapp;

import static com.example.healthcareapp.Fragments.AddImgFragment.PICK_IMAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.healthcareapp.Model.exercise;
import com.example.healthcareapp.Model.food;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditFoodActivity extends AppCompatActivity {
    public static Uri imagesNewFood;
    ImageButton cam, gal;
    LinearLayout chooseImg;
    food foodEdit;
    ImageButton img, btn_back;
    private Spinner spn;
    Button savefood;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    EditText etNameNewFood, etCalorieNewFood, etServing, etTimeNewFood, etCarbs, etProtein, etFat;
    DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food);
        etNameNewFood = findViewById(R.id.addFoodName);
        etCalorieNewFood = findViewById(R.id.addFoodCalorie);
        etCarbs = findViewById(R.id.addFoodCarb);
        etFat = findViewById(R.id.addFoodFat);
        etProtein = findViewById(R.id.addFoodProtein);
        //etServing = findViewById(R.id.addServing);
        savefood = findViewById(R.id.saveFood2);
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
                ImagePicker.Companion.with(EditFoodActivity.this)
                        .cameraOnly()
                        .maxResultSize(1080,1080)
                        .start(101);
            }
        });
        //spn
        spn = (Spinner)findViewById(R.id.addServing);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.serving, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adapter);
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Intent i = getIntent();
        String idExercise = i.getStringExtra("id");
        database = FirebaseDatabase.getInstance().getReference("newFoodUserAdd");
        database.child(uid).child(idExercise).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodEdit = snapshot.getValue(food.class);
                etNameNewFood.setText(foodEdit.getNameFood());
                etCalorieNewFood.setText(foodEdit.getCaloriesFood());
                for (int i=0;i<spn.getCount();i++)
                    if (foodEdit.getServingFood().equals(spn.getItemAtPosition(i).toString()))
                        spn.setSelection(i);
                etCarbs.setText(foodEdit.getCabsFood());
                etFat.setText(foodEdit.getFatFood());
                etProtein.setText(foodEdit.getProteinFood());
                imagesNewFood = Uri.parse(foodEdit.getImgFood());
                img.setImageURI(Uri.parse(foodEdit.getImgFood()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditFoodActivity.this, TopTabExerciseActivity.class);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        savefood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LanguageUtils.getCurrentLanguage() == Language.ENGLISH) {
                    if (etCalorieNewFood.getText().toString().trim().isEmpty() || etNameNewFood.getText().toString().trim().isEmpty()
                            || etFat.getText().toString().trim().isEmpty() || etCarbs.getText().toString().trim().isEmpty() ||
                            etProtein.getText().toString().trim().isEmpty()) {
                        Toast.makeText(EditFoodActivity.this, "Please Enter Full", Toast.LENGTH_SHORT).show();
                    } else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(EditFoodActivity.this);
                        dialog.setTitle("Save");
                        dialog.setIcon(R.drawable.noti_icon);
                        dialog.setMessage("You want to save??");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                food e = new food();
                                e.setIdFood(idExercise);
                                e.setNameFood(etNameNewFood.getText().toString().trim());
                                e.setCaloriesFood(etCalorieNewFood.getText().toString().trim());
                                e.setServingFood(spn.getSelectedItem().toString().trim());
                                e.setCabsFood(etCarbs.getText().toString().trim());
                                e.setFatFood(etFat.getText().toString().trim());
                                e.setProteinFood(etProtein.getText().toString().trim());
                                e.setImgFood(String.valueOf(imagesNewFood));

                                DatabaseReference database1 = FirebaseDatabase.getInstance().getReference("newFoodUserAdd");
                                database1.child(uid).child(idExercise).setValue(e);

                                Toast.makeText(EditFoodActivity.this, "Update Exercise Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EditFoodActivity.this, TopTabExerciseActivity.class);
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
                    if (etCalorieNewFood.getText().toString().trim().isEmpty() || etNameNewFood.getText().toString().trim().isEmpty()
                            || etFat.getText().toString().trim().isEmpty() || etCarbs.getText().toString().trim().isEmpty() ||
                            etProtein.getText().toString().trim().isEmpty()) {
                        Toast.makeText(EditFoodActivity.this, "Vui lòng điền đủ thông tin phần thức ăn ", Toast.LENGTH_SHORT).show();
                    } else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(EditFoodActivity.this);
                        dialog.setTitle("Lưu");
                        dialog.setIcon(R.drawable.noti_icon);
                        dialog.setMessage("Bạn có muốn lưu??");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                food e = new food();
                                e.setIdFood(idExercise);
                                e.setNameFood(etNameNewFood.getText().toString().trim());
                                e.setServingFood(spn.getSelectedItem().toString().trim());
                                e.setCabsFood(etCarbs.getText().toString().trim());
                                e.setFatFood(etFat.getText().toString().trim());
                                e.setProteinFood(etProtein.getText().toString().trim());
                                e.setImgFood(String.valueOf(imagesNewFood));

                                DatabaseReference database1 = FirebaseDatabase.getInstance().getReference("newFoodUserAdd");
                                database1.child(uid).child(idExercise).setValue(e);

                                Toast.makeText(EditFoodActivity.this, "Update Exercise Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EditFoodActivity.this, TopTabExerciseActivity.class);
                                setResult(RESULT_OK, intent);
                                finish();
                                Toast.makeText(EditFoodActivity.this, "Cập nhât hoạt động mới thành công", Toast.LENGTH_SHORT).show();

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
        database = FirebaseDatabase.getInstance().getReference("newFoodUserAdd");
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