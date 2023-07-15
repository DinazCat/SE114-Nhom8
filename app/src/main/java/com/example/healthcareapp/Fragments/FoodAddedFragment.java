package com.example.healthcareapp.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcareapp.Adapter.FoodAdapter;
import com.example.healthcareapp.Adapter.NewFoodAdapter;
import com.example.healthcareapp.Language;
import com.example.healthcareapp.LanguageUtils;
import com.example.healthcareapp.ListInterface.ClickFoodItem;
import com.example.healthcareapp.Model.food;
import com.example.healthcareapp.R;
import com.example.healthcareapp.SearchTopTabActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FoodAddedFragment extends Fragment {
    RecyclerView recyclerViewNewFood;
    List<food> newFoodList;
    DatabaseReference database, database1, db;
    NewFoodAdapter newFoodAdapter;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_added, container, false);
        Calendar calendar = Calendar.getInstance();

        String date = DateFormat.format("yyyy-MM-dd", calendar).toString();
        //region RecyleViewFood
        recyclerViewNewFood = view.findViewById(R.id.recyclerviewNewFood);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerViewNewFood.getContext());
        recyclerViewNewFood.setLayoutManager(linearLayoutManager);
        newFoodList = new ArrayList<>();
        newFoodAdapter = new NewFoodAdapter(newFoodList, new ClickFoodItem() {
            @Override
            public void onClickItemFood(food _food) {
                //etNameNewFood.setText(_food.getNameFood());
                //etCalorieNewFood.setText(_food.getCaloriesFood());
                //for (int i=0; i<spn.getCount(); i++) {
                //    if (spn.getItemAtPosition(i).equals(_food.getServingFood()))
                //        spn.setSelection(i);
                //}
                if (LanguageUtils.getCurrentLanguage() == Language.ENGLISH) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("Delete");
                    dialog.setIcon(R.drawable.noti_icon);
                    dialog.setMessage("You want to delete??");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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
                            Intent i = new Intent(view.getContext(), SearchTopTabActivity.class);
                            startActivity(i);

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
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("Xóa");
                    dialog.setIcon(R.drawable.noti_icon);
                    dialog.setMessage("Bạn có muốn xóa??");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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
            // Xóa trong recycle view food khong cho add nữa
                /*if (tvEngVie.getText().toString().equals("List New Food You Added")) {
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
                }*/
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
        return view;
    }
}