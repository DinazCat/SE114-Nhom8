package com.example.healthcareapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Adapter.IngredientAdapter;
import com.example.healthcareapp.Model.IngredientData;
import com.example.healthcareapp.Model.ingredient;
import com.example.healthcareapp.PostActivity;
import com.example.healthcareapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Fragment_baiviet1 extends Fragment {
    DatabaseReference databaseCalo;
    public static RecyclerView listI;
    ImageButton addI;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    public static EditText making=null, summary;
    public static ArrayList<IngredientData> listIdata = new ArrayList<>();
    public static String FMaking, FSummary;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_baiviet1, container, false);
        listI = view.findViewById(R.id.listI);
        addI = view.findViewById(R.id.addIngredient);
        making = view.findViewById(R.id.recipe);
        summary = view.findViewById(R.id.saySt);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        listI.setLayoutManager(linearLayoutManager);
        listI.setItemAnimator(new DefaultItemAnimator());
        if (PostActivity.thaotac.equals("Share")) {
            databaseCalo = FirebaseDatabase.getInstance().getReference("newFoods");
            databaseCalo.child(uid).child(PostActivity.re.getIdRecipe()).child(PostActivity.re.getNameRecipe()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listIdata.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ingredient in = dataSnapshot.getValue(ingredient.class);
                        IngredientData object = new IngredientData();
                        object.name = in.getNameIngredient();
                        object.wty = in.getQuantity();
                        object.unit = "gram";
                        listIdata.add(object);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "fail",Toast.LENGTH_SHORT).show();
                }
            });
        }
        if( PostActivity.thaotac.equals("push"))
        {
            FMaking=""; FSummary="";
            if(listIdata.size()>0)
            {
                IngredientAdapter adapter = new IngredientAdapter(Fragment_baiviet1.this.getActivity(), listIdata);
                listI.setAdapter(adapter);
            }
        }
        else{

            making.setText(FMaking);
            summary.setText(FSummary);
            if(listIdata.size()>0) {
                IngredientAdapter adapter = new IngredientAdapter(Fragment_baiviet1.this.getActivity(), listIdata);
                listI.setAdapter(adapter);
            }
        }
        addI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IngredientData object = new IngredientData();
                object.name = "";
                object.wty = "";
                object.unit = "";
                listIdata.add(object);
                IngredientAdapter adapter = new IngredientAdapter(Fragment_baiviet1.this.getActivity(), listIdata);
                listI.setAdapter(adapter);
                //System.out.println(listIdata.size());
            }
        });


        return view;
    }
}