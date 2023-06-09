package com.example.healthcareapp.Fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcareapp.Adapter.ExpandableListViewAdapter;
import com.example.healthcareapp.MainActivity;
import com.example.healthcareapp.Model.water;
import com.example.healthcareapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Random;

public class AddWaterFragment extends Fragment {

    Button btnSave;
    FirebaseDatabase firebaseDatabase;
    EditText edtWater;
    TextView tvDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_water, container, false);
        edtWater = view.findViewById(R.id.water);
        btnSave = view.findViewById(R.id.save);
        tvDate = view.findViewById(R.id.date);
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
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

        return view;
    }
    private void setWaterAmount(String date) {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                //you can return AddFragment or MainActivity if you want
                if (edtWater.getText().toString().trim().equals("")){
                    Toast.makeText(getActivity(), "Input cannot be blank", Toast.LENGTH_LONG).show();
                }
                else {
                    String uid = FirebaseAuth.getInstance().getUid();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("water");

                    water w = new water();
                    Random random = new Random();
                    int randomID = random.nextInt(100000);
                    w.setWaterAmount(edtWater.getText().toString());
                    w.setTime(date);
                    databaseReference.child(uid).child(date).child(String.valueOf(randomID)).setValue( w);

                }
            }
        });
    }
}