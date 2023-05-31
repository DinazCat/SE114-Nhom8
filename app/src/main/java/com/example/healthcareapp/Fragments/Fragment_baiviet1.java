package com.example.healthcareapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Adapter.IngredientAdapter;
import com.example.healthcareapp.Model.IngredientData;
import com.example.healthcareapp.PostActivity;
import com.example.healthcareapp.R;

import java.util.ArrayList;


public class Fragment_baiviet1 extends Fragment {

    public static RecyclerView listI;
    ImageButton addI;
    public static EditText making, summary;
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
        if( PostActivity.thaotac.equals("push"))
        {
            FMaking=""; FSummary="";
        }
        else{

            making.setText(FMaking);
            summary.setText(FSummary);
            IngredientAdapter adapter = new IngredientAdapter(Fragment_baiviet1.this.getActivity(), listIdata);
            listI.setAdapter(adapter);
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