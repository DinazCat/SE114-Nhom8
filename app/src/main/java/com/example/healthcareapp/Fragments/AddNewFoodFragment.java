package com.example.healthcareapp.Fragments;

import static com.example.healthcareapp.Fragments.AddImgFragment.PICK_IMAGE;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcareapp.Adapter.NewFoodAdapter;
import com.example.healthcareapp.Language;
import com.example.healthcareapp.LanguageUtils;
import com.example.healthcareapp.ListInterface.ClickFoodItem;
import com.example.healthcareapp.Model.food;
import com.example.healthcareapp.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class AddNewFoodFragment extends Fragment {
    boolean imageCheck=false;
    public static Uri imagesNewFood;
    ImageButton cam, gal;
    LinearLayout chooseImg;
    Button btSaveFood, btAddImage;
    EditText etNameNewFood, etCalorieNewFood, edCarbs, edFat, edProtein;
    DatabaseReference database, database1, db;
    private Spinner spn;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    TextView tvEngVie;
    ImageButton img;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_food, container, false);
        ImageButton Addimg = view.findViewById(R.id.imgpicker);
        cam = view.findViewById(R.id.camera);
        gal = view.findViewById(R.id.gallery);
        chooseImg = view.findViewById(R.id.choose);
        img = view.findViewById(R.id.imgpicker);

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
                ImagePicker.Companion.with(AddNewFoodFragment.this)
                        .cameraOnly()
                        .maxResultSize(1080,1080)
                        .start(101);
            }
        });


        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImg.setVisibility(View.INVISIBLE);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        btSaveFood = view.findViewById(R.id.saveFood2);

        Calendar calendar = Calendar.getInstance();
        String today = DateFormat.format("yyyy-MM-dd", calendar).toString();

        etNameNewFood = view.findViewById(R.id.addFoodName);
        etCalorieNewFood = view.findViewById(R.id.addFoodCalorie);
        edProtein = view.findViewById(R.id.addFoodProtein);
        edCarbs = view.findViewById(R.id.addFoodCarb);
        edFat = view.findViewById(R.id.addFoodFat);

        //spn
        spn = (Spinner)view.findViewById(R.id.addServing);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(), R.array.serving, android.R.layout.simple_spinner_item);
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


        //lưu dữ liệu xuống 2 tree food và newfooduseradded
        btSaveFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LanguageUtils.getCurrentLanguage() == Language.ENGLISH) {
                    if (etCalorieNewFood.getText().toString().trim().isEmpty() || etNameNewFood.getText().toString().trim().isEmpty()
                            || edCarbs.getText().toString().trim().isEmpty() || edFat.getText().toString().trim().isEmpty()
                            || edProtein.getText().toString().trim().isEmpty() || imageCheck==false) {
                        Toast.makeText(getContext(), "Please Enter Full", Toast.LENGTH_SHORT).show();
                    } else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                        dialog.setTitle("Save");
                        dialog.setIcon(R.drawable.noti_icon);
                        dialog.setMessage("You want to save??");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Random random = new Random();
                                int randomID = random.nextInt(1000000);

                                food _food = new food();
                                _food.setIdFood(String.valueOf(randomID));
                                _food.setNameFood(etNameNewFood.getText().toString().trim());
                                _food.setCaloriesFood(etCalorieNewFood.getText().toString().trim());
                                _food.setServingFood(spn.getSelectedItem().toString().trim());
                                _food.setImgFood(String.valueOf(imagesNewFood));
                                _food.setCabsFood(edCarbs.getText().toString().trim());
                                _food.setFatFood(edFat.getText().toString().trim());
                                _food.setProteinFood(edProtein.getText().toString().trim());

                                database = FirebaseDatabase.getInstance().getReference("newFoodUserAdd");
                                database.child(uid).child(_food.getIdFood()).setValue(_food);

                                etNameNewFood.setText("");
                                etCalorieNewFood.setText("");
                                edCarbs.setText("");
                                edFat.setText("");
                                edProtein.setText("");
                                int id = getResources().getIdentifier("android:drawable/" + R.drawable.ic_addphoto, null, null);
                                img.setImageResource(id);
                                imageCheck=false;
                                Toast.makeText(getContext(), "Add New Food Success", Toast.LENGTH_SHORT).show();

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
                            || edCarbs.getText().toString().trim().isEmpty() || edFat.getText().toString().trim().isEmpty()
                            || edProtein.getText().toString().trim().isEmpty() || imageCheck==false) {
                        Toast.makeText(getContext(), "Vui lòng điền đủ thông tin phần thức ăn ", Toast.LENGTH_SHORT).show();
                    } else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                        dialog.setTitle("Lưu");
                        dialog.setIcon(R.drawable.noti_icon);
                        dialog.setMessage("Bạn có muốn lưu??");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Random random = new Random();
                                int randomID = random.nextInt(1000000);

                                food _food = new food();
                                _food.setIdFood(String.valueOf(randomID));
                                _food.setNameFood(etNameNewFood.getText().toString().trim());
                                _food.setCaloriesFood(etCalorieNewFood.getText().toString().trim());
                                _food.setServingFood(spn.getSelectedItem().toString().trim());
                                _food.setImgFood(String.valueOf(imagesNewFood));
                                _food.setCabsFood(edCarbs.getText().toString().trim());
                                _food.setFatFood(edFat.getText().toString().trim());
                                _food.setProteinFood(edProtein.getText().toString().trim());

                                database = FirebaseDatabase.getInstance().getReference("newFoodUserAdd");
                                database.child(uid).child(_food.getIdFood()).setValue(_food);

                                etNameNewFood.setText("");
                                etCalorieNewFood.setText("");
                                edCarbs.setText("");
                                edFat.setText("");
                                edProtein.setText("");
                                int id = getResources().getIdentifier("android:drawable/" + R.drawable.ic_addphoto, null, null);
                                img.setImageResource(id);
                                imageCheck=false;
                                Toast.makeText(getContext(), "Thêm thức ăn mới thành công", Toast.LENGTH_SHORT).show();

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

        return view;
    }
    ActivityResultLauncher<Intent> launcherActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                    }
                }
            });
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
}