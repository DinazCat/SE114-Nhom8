package com.example.healthcareapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Adapter.HashtagAdapter;
import com.example.healthcareapp.Model.hashtag;
import com.example.healthcareapp.PostActivity;
import com.example.healthcareapp.R;

import java.util.ArrayList;
import java.util.Arrays;

public class Fragment_baiviet2 extends Fragment {
 public static ArrayList<String> hashtags = new ArrayList<>();
    public ArrayList<hashtag> mealtype = new ArrayList<>(Arrays.asList(new hashtag("Breakfast", false), new hashtag("Lunch", false), new hashtag("Dinner", false),new hashtag("Snack", false)));
    public ArrayList<hashtag> cookingstyle = new ArrayList<>(Arrays.asList(new hashtag("Fast Prep", false),new hashtag("No cooking", false), new hashtag("Fast & Easy", false),new hashtag("Slow Cooker", false),new hashtag("Grilling", false)));
    public ArrayList<hashtag> course = new ArrayList<>(Arrays.asList(new hashtag("Salads & Dressings", false), new hashtag("Desserts", false),new hashtag("Sides", false), new hashtag("Beverages & Smoothies", false), new hashtag("Soups & Stews", false)));
    public ArrayList<hashtag> mainingredient = new ArrayList<>(Arrays.asList(new hashtag(" Beans & Peas", false),new hashtag("Beef", false),new hashtag("Chicken", false),new hashtag("Egg", false),new hashtag( "Seafood", false),new hashtag("Pork", false),new hashtag("Pasta", false)));
    public ArrayList<hashtag> diettype = new ArrayList<>(Arrays.asList(new hashtag("Low-Fat", false), new hashtag("High-Protein", false),new hashtag("Vegetarian", false),new hashtag("Keto", false),new hashtag("Mediterranean", false),new hashtag("High-Fiber", false)));
    RecyclerView G1, G2, G3, G4, G5;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_baiviet2, container, false);
     //   hashtags = new ArrayList<>();
        if( !PostActivity.thaotac.equals("push"))
        {
            for(int i = 0; i < mealtype.size(); i++){
                if(hashtags.contains(mealtype.get(i).getName())){
                    mealtype.get(i).setTick(true);
                }
            }
            for(int i = 0; i < cookingstyle.size(); i++){
                if(hashtags.contains(cookingstyle.get(i).getName())){
                    cookingstyle.get(i).setTick(true);
                }
            }
            for(int i = 0; i < course.size(); i++){
                if(hashtags.contains(course.get(i).getName())){
                    course.get(i).setTick(true);
                }
            }
            for(int i = 0; i < mainingredient.size(); i++){
                if(hashtags.contains(mainingredient.get(i).getName())){
                    mainingredient.get(i).setTick(true);
                }
            }
            for(int i = 0; i < diettype.size(); i++){
                if(hashtags.contains(diettype.get(i).getName())){
                    diettype.get(i).setTick(true);
                }
            }

        }
        G1 = view.findViewById(R.id.mealtype);
        HashtagAdapter adapter1 = new HashtagAdapter(this.getActivity(), mealtype);
        G1.setAdapter(adapter1);
        G1.setLayoutManager(new GridLayoutManager(this.getActivity(), 3));
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//        G1.setLayoutManager(layoutManager);
        G2 = view.findViewById(R.id.cookingstyle);
        HashtagAdapter adapter2 = new HashtagAdapter(this.getActivity(), cookingstyle);
        G2.setAdapter(adapter2);
        G2.setLayoutManager(new GridLayoutManager(this.getActivity(), 3));
//        StaggeredGridLayoutManager layoutManager1 = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//        G2.setLayoutManager(layoutManager1);
        G3 = view.findViewById(R.id.course);
        HashtagAdapter adapter3 = new HashtagAdapter(this.getActivity(), course);
        G3.setAdapter(adapter3);
        G3.setLayoutManager(new GridLayoutManager(this.getActivity(), 3));
       // G3.setLayoutManager(layoutManager);
        G4 = view.findViewById(R.id.mainingredient);
        HashtagAdapter adapter4 = new HashtagAdapter(this.getActivity(), mainingredient);
        G4.setAdapter(adapter4);
        G4.setLayoutManager(new GridLayoutManager(this.getActivity(), 3));
//        StaggeredGridLayoutManager layoutManager2 = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//        G4.setLayoutManager(layoutManager2);
        G5 = view.findViewById(R.id.diettype);
        HashtagAdapter adapter5 = new HashtagAdapter(this.getActivity(), diettype);
        G5.setAdapter(adapter5);
        G5.setLayoutManager(new GridLayoutManager(this.getActivity(), 3));
//        StaggeredGridLayoutManager layoutManager3 = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//        G5.setLayoutManager(layoutManager3);
        return view;
    }
}