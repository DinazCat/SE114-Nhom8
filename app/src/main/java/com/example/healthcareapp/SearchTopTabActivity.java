package com.example.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.healthcareapp.Adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class SearchTopTabActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_top_tab);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewpager);
        btnBack = findViewById(R.id.back);
        ViewPagerAdapter vpa = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(vpa);
        tabLayout.setupWithViewPager(viewPager);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchTopTabActivity.this, MainActivity.class);

                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

}