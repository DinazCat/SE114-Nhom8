package com.example.healthcareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcareapp.Model.note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NoteActivity extends AppCompatActivity {
    DatabaseReference database;
    EditText edt_Content;
    TextView tv_date;
    Button btn_back;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Intent i = getIntent();
        String date = i.getStringExtra("date");
        String dateHT = i.getStringExtra("dateHT");

        tv_date = findViewById(R.id.date);
        tv_date.setText(dateHT);
        edt_Content = findViewById(R.id.contentNote);
        btn_back = findViewById(R.id.back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteActivity.this, MainActivity.class);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        database = FirebaseDatabase.getInstance().getReference("noteDiary");
        database.child(uid).child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                note _note = snapshot.getValue(note.class);
                if (snapshot.exists()) {
                    edt_Content.setText(_note.getContent());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Button btn_SaveNote;
        btn_SaveNote = findViewById(R.id.saveNote);
        btn_SaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LanguageUtils.getCurrentLanguage() == Language.ENGLISH) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(NoteActivity.this);
                    dialog.setTitle("Save");
                    dialog.setIcon(R.drawable.noti_icon);
                    dialog.setMessage("You want to save??");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            note _note = new note(edt_Content.getText().toString().trim(), date);
                            database.child(uid).child(date).setValue(_note);
                            Toast.makeText(NoteActivity.this, "Save Success", Toast.LENGTH_SHORT).show();
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
                    AlertDialog.Builder dialog = new AlertDialog.Builder(NoteActivity.this);
                    dialog.setTitle("Lưu");
                    dialog.setIcon(R.drawable.noti_icon);
                    dialog.setMessage("Bạn có muốn lưu??");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            note _note = new note(edt_Content.getText().toString().trim(), date);
                            database.child(uid).child(date).setValue(_note);
                            Toast.makeText(NoteActivity.this, "Lưu thành công", Toast.LENGTH_SHORT).show();
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