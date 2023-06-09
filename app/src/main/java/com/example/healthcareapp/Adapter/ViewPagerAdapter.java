package com.example.healthcareapp.Adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.healthcareapp.Fragments.AddNewFoodFragment;
import com.example.healthcareapp.Fragments.AddRecipeFragment;
import com.example.healthcareapp.Fragments.AddWaterFragment;
import com.example.healthcareapp.Fragments.HomeFragment;
import com.example.healthcareapp.Fragments.SearchExerciseFragment;
import com.example.healthcareapp.Fragments.SearchFoodFragment;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private final ArrayList<String> fragmentTitle = new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SearchFoodFragment();
            case 1:
                return new AddNewFoodFragment();
            case 2:
                return new AddRecipeFragment();
            case 3:
                return new SearchExerciseFragment();
            case 4:
                return new AddWaterFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Add Food";
                break;
            case 1:
                title = "New Food";
                break;
            case 2:
                title = "New Recipe";
                break;
            case 3:
                title = "Add Exercise";
                break;
            case 4:
                title = "Add Water";
                break;
        }
        return title;
    }
}
