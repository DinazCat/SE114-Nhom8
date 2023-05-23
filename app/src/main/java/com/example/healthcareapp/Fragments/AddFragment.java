package com.example.healthcareapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.example.healthcareapp.Adapter.ExpandableListViewAdapter;
import com.example.healthcareapp.Model.food;
import com.example.healthcareapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
public class AddFragment extends Fragment {
    ExpandableListViewAdapter listViewAdapter;
    ExpandableListView expandableListView;
    List<String> meals;
    HashMap<String, List<food>> foodList;
    Button bt ;
    private FragmentAListener listenter;
    public interface FragmentAListener{
        void onInputASent(CharSequence input);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        expandableListView = view.findViewById(R.id.expandableLV);

        showList();

        listViewAdapter = new ExpandableListViewAdapter(expandableListView.getContext(), meals,foodList);
        expandableListView.setAdapter(listViewAdapter);

        bt = view.findViewById(R.id.addFood);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment((new SearchFoodFragment()));
            }
        });
        return view;
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
    private void showList() {
        meals = new ArrayList<String>();
        foodList = new HashMap<String, List<food>>();

        meals.add("Breakfast");
        meals.add("Lunch");
        meals.add("Dinner");
        meals.add("Snack");
        meals.add("Water");
        meals.add("Exercise");

        List<food> topic1 = new ArrayList<>();
        food f1 = new food("","Bread","90 cl","");
        topic1.add(f1);
        food f2 = new food("","Coffee","50 cl","");
        topic1.add(f2);
        food f3 = new food("","Banana","190 cl","");
        topic1.add(f3);

        List<food> topic2 = new ArrayList<>();
        food f4 = new food("","Meat","1190 cl","");
        topic2.add(f4);
        food f5 = new food("","Salad","40 cl","");
        topic2.add(f5);

        List<food> topic3 = new ArrayList<>();
        food f6 = new food("","Rice and Beans","110 cl","");
        topic3.add(f6);
        food f7 = new food("","Soup","90 cl","");
        topic3.add(f7);
        food f8 = new food("","Fish","990 cl","");
        topic3.add(f8);

        List<food> topic4 = new ArrayList<>();
        food f9 = new food("","Bread","90 cl","");
        topic4.add(f9);

        List<food> topic5 = new ArrayList<>();
        food f10 = new food("","Bread","90 cl","");
        topic5.add(f10);


        foodList.put(meals.get(0),topic1);
        foodList.put(meals.get(1),topic2);
        foodList.put(meals.get(2),topic3);
        foodList.put(meals.get(3),topic4);
        foodList.put(meals.get(4),topic5);

    }
}