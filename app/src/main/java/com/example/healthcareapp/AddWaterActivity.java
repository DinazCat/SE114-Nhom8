package com.example.healthcareapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcareapp.Fragments.AddFragment;
import com.example.healthcareapp.Model.water;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Random;

public class AddWaterActivity extends AppCompatActivity {
    Button btnSave;
    FirebaseDatabase firebaseDatabase;
    EditText edtWater;
    Button btn_back;
    TextView tvDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_water);
        btn_back = findViewById(R.id.back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddWaterActivity.this, AddFragment.class);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        edtWater = findViewById(R.id.water);
        btnSave = findViewById(R.id.save);
        tvDate = findViewById(R.id.date);
        Calendar calendar = Calendar.getInstance();
        String string = DateFormat.format("yyyy-MM-dd", calendar).toString();
        setWaterAmount(string);
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddWaterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        if (calendar.get(Calendar.DAY_OF_MONTH) == dayOfMonth && calendar.get(Calendar.MONTH) == month && calendar.get(Calendar.YEAR) == year){
                            tvDate.setText("Today");
                            String string = DateFormat.format("yyyy-MM-dd", calendar).toString();
                            setWaterAmount(string);

                        }
                        else{
                            calendar.set(year, month, dayOfMonth);
                            tvDate.setText(DateFormat.format("dd/MM/yyyy", calendar).toString());
                            String string = DateFormat.format("yyyy-MM-dd", calendar).toString();
                            setWaterAmount(string);
                        }
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private void setWaterAmount(String date) {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LanguageUtils.getCurrentLanguage() == Language.ENGLISH) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(AddWaterActivity.this);
                    dialog.setTitle("Save");
                    dialog.setIcon(R.drawable.noti_icon);
                    dialog.setMessage("You want to save??");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (edtWater.getText().toString().trim().equals("")) {
                                Toast.makeText(AddWaterActivity.this, "Input cannot be blank", Toast.LENGTH_LONG).show();
                            } else {
                                String uid = FirebaseAuth.getInstance().getUid();
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("water");

                                water w = new water();
                                Random random = new Random();
                                int randomID = random.nextInt(100000);
                                w.setWaterAmount(edtWater.getText().toString());
                                w.setTime(date);
                                w.setIdwater(String.valueOf(randomID));
                                databaseReference.child(uid).child(date).child(String.valueOf(randomID)).setValue(w);

                            }
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
                else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(AddWaterActivity.this);
                    dialog.setTitle("Lưu");
                    dialog.setIcon(R.drawable.noti_icon);
                    dialog.setMessage("Bạn có muốn lưu??");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (edtWater.getText().toString().trim().equals("")) {
                                Toast.makeText(AddWaterActivity.this, "Không thể để trống", Toast.LENGTH_LONG).show();
                            } else {
                                String uid = FirebaseAuth.getInstance().getUid();
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("water");

                                water w = new water();
                                Random random = new Random();
                                int randomID = random.nextInt(100000);
                                w.setWaterAmount(edtWater.getText().toString());
                                w.setTime(date);
                                w.setIdwater(String.valueOf(randomID));
                                databaseReference.child(uid).child(date).child(String.valueOf(randomID)).setValue(w);

                            }
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
        });
    }
}