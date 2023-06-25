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
import com.example.healthcareapp.Language;
import com.example.healthcareapp.LanguageUtils;

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
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        if (LanguageUtils.getCurrentLanguage() == Language.ENGLISH) {
            switch (position) {
                case 0:
                    title = "Add Food";
                    break;
                case 1:
                    title = "Add New Food";
                    break;
            }
        }
        else {
            switch (position) {
                case 0:
                    title = "Thêm thức ăn";
                    break;
                case 1:
                    title = "Thêm thức ăn mới";
                    break;
            }
        }
        return title;
    }
}
