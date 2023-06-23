package com.example.healthcareapp.Adapter;


import android.app.Activity;
import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.healthcareapp.Fragments.AddFragment;
import com.example.healthcareapp.Fragments.AddRecipeFragment;
import com.example.healthcareapp.Fragments.NewRecipeStep1Fragment;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ViewPageRecipeAdapter extends FragmentStatePagerAdapter {
    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private final ArrayList<String> fragmentTitle = new ArrayList<>();

    public ViewPageRecipeAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AddRecipeFragment();
            case 1:
                return new NewRecipeStep1Fragment();
            default:
                return new AddFragment();
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
        switch (position) {
            case 0:
                title = "Recipe You Added";
                break;
            case 1:
                title = "Add New Recipe";
                break;
        }
        return title;
    }

}
