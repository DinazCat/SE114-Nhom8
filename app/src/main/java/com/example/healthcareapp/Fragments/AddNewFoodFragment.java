package com.example.healthcareapp.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcareapp.Adapter.NewFoodAdapter;
import com.example.healthcareapp.ListInterface.ClickFoodItem;
import com.example.healthcareapp.Model.food;
import com.example.healthcareapp.R;
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
    RecyclerView recyclerViewNewFood;
    List<food> newFoodList;
    NewFoodAdapter newFoodAdapter;
    Button btSaveFood;
    EditText etNameNewFood, etCalorieNewFood;
    DatabaseReference database, database1, db;
    private Spinner spn;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    TextView tvEngVie;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_food, container, false);
        tvEngVie = view.findViewById(R.id.textView5);
        btSaveFood = view.findViewById(R.id.saveFood);
        Calendar calendar = Calendar.getInstance();
        String today = DateFormat.format("yyyy-MM-dd", calendar).toString();

        etNameNewFood = view.findViewById(R.id.addFoodName);
        etCalorieNewFood = view.findViewById(R.id.addFoodCalorie);
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
        //region RecyleViewFood
        recyclerViewNewFood = view.findViewById(R.id.recyclerviewNewFood);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerViewNewFood.getContext());
        recyclerViewNewFood.setLayoutManager(linearLayoutManager);
        newFoodList = new ArrayList<>();
        newFoodAdapter = new NewFoodAdapter(newFoodList, new ClickFoodItem() {
            @Override
            public void onClickItemFood(food _food) {
                etNameNewFood.setText(_food.getNameFood());
                etCalorieNewFood.setText(_food.getCaloriesFood());
                for (int i=0; i<spn.getCount(); i++) {
                    if (spn.getItemAtPosition(i).equals(_food.getServingFood()))
                        spn.setSelection(i);
                }
                DatabaseReference database = FirebaseDatabase.getInstance().getReference("newFoodUserAdd");
                database.child(uid).child(_food.getIdFood()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().removeValue();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                // Xóa trong recycle view food khong cho add nữa
                if (tvEngVie.getText().toString().equals("List New Food You Added")) {
                    DatabaseReference database1 = FirebaseDatabase.getInstance().getReference("foodsEng");
                    database1.child(_food.getIdFood()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            snapshot.getRef().removeValue();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else {
                    DatabaseReference database1 = FirebaseDatabase.getInstance().getReference("foods");
                    database1.child(_food.getIdFood()).addListenerForSingleValueEvent(new ValueEventListener() {
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
        });

        recyclerViewNewFood.setAdapter(newFoodAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(recyclerViewNewFood.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewNewFood.addItemDecoration(itemDecoration);

        database = FirebaseDatabase.getInstance().getReference("newFoodUserAdd");
        database.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newFoodList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    food _food = dataSnapshot.getValue(food.class);
                    newFoodList.add(_food);
                }
                newFoodAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //endregion

        //lưu dữ liệu xuống 2 tree food và newfooduseradded
        btSaveFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCalorieNewFood.getText().toString().trim().isEmpty() || etNameNewFood.getText().toString().trim().isEmpty() ) {
                    Toast.makeText(getContext(), "Please Enter Name, Calorie and Serving", Toast.LENGTH_SHORT).show();
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

                            database = FirebaseDatabase.getInstance().getReference("newFoodUserAdd");
                            database.child(uid).child(_food.getIdFood()).setValue(_food);

                            if (tvEngVie.getText().toString().equals("List New Food You Added")) {
                                DatabaseReference database1 = FirebaseDatabase.getInstance().getReference("foodsEng");
                                database1.child(_food.getIdFood()).setValue(_food);
                            }
                            else {
                                DatabaseReference database1 = FirebaseDatabase.getInstance().getReference("foods");
                                database1.child(_food.getIdFood()).setValue(_food);
                            }
                            etNameNewFood.setText("");
                            etCalorieNewFood.setText("");

                            Toast.makeText(getContext(), "Add New Food Success", Toast.LENGTH_SHORT).show();

                            newFoodAdapter.notifyDataSetChanged();
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
        });
        return view;
    }


}