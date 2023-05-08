package com.example.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.healthcareapp.Fragments.BlogFragment;
import com.example.healthcareapp.Fragments.GraphFragment;
import com.example.healthcareapp.Fragments.HomeFragment;
import com.example.healthcareapp.Fragments.SettingsFragment;
import com.example.healthcareapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate((getLayoutInflater()));
        setContentView(binding.getRoot());
        // Data Information of User
        Intent intent = getIntent();
        String _name = intent.getStringExtra("name");
        String _age = intent.getStringExtra("age");
        String _height = intent.getStringExtra("height");
        String _weight = intent.getStringExtra("weight");
        String _sex = intent.getStringExtra("sex");
        String _goal = intent.getStringExtra("goal");
        String _level = intent.getStringExtra("level");

        //

        //Binding for Bottom App Bar
        replaceFragment((new HomeFragment()));
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch ( (item.getItemId())) {
                case R.id.home:
                    replaceFragment((new HomeFragment()));
                    break;

                case R.id.graph:
                    replaceFragment((new GraphFragment()));
                    break;

                case R.id.blogs:
                    replaceFragment((new BlogFragment()));
                    break;

                case R.id.settings:
                    replaceFragment((new SettingsFragment()));
                    break;
            }
            return true;
        });

    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}