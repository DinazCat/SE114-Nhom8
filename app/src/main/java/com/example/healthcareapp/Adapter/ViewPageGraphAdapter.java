package com.example.healthcareapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.healthcareapp.Fragments.CaloriesBurnedFragment;
import com.example.healthcareapp.Fragments.HeightGraphFragment;
import com.example.healthcareapp.Fragments.WeightGraphFragment;

public class ViewPageGraphAdapter extends FragmentStateAdapter {


    public ViewPageGraphAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new WeightGraphFragment();
            case 1:
                return new HeightGraphFragment();
            case 2:
                return new CaloriesBurnedFragment();
            default:
                return new WeightGraphFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
