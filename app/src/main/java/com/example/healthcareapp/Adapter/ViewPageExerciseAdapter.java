package com.example.healthcareapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.healthcareapp.ExerciseAddedFragment;
import com.example.healthcareapp.Fragments.AddNewExerciseFragment;
import com.example.healthcareapp.Fragments.AddNewFoodFragment;
import com.example.healthcareapp.Fragments.FoodAddedFragment;
import com.example.healthcareapp.Fragments.HomeFragment;
import com.example.healthcareapp.Fragments.SearchExerciseFragment;
import com.example.healthcareapp.Fragments.SearchFoodFragment;
import com.example.healthcareapp.Language;
import com.example.healthcareapp.LanguageUtils;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ViewPageExerciseAdapter extends FragmentStatePagerAdapter {
    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private final ArrayList<String> fragmentTitle = new ArrayList<>();

    public ViewPageExerciseAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SearchExerciseFragment();
            case 1:
                return new ExerciseAddedFragment();
            case 2:
                return new AddNewExerciseFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        if (LanguageUtils.getCurrentLanguage() == Language.ENGLISH) {
            switch (position) {
                case 0:
                    title = "Add Exercise";
                    break;
                case 1:
                    title = "New Exercise Added";
                    break;
                case 2:
                    title = "Add New Exercise";
                    break;
            }
        } else {
            switch (position) {
                case 0:
                    title = "Thêm hoạt động";
                    break;
                case 1:
                    title = "Hoạt động mới đã thêm";
                    break;
                case 2:
                    title = "Thêm hoạt động mới";
                    break;
            }
        }
        return title;
    }
}
