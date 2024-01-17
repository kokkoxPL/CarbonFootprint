package com.kokkoxpl.carbonfootprint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.kokkoxpl.carbonfootprint.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "CF";

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment(""));

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int menuItemId =  item.getItemId();

            if (menuItemId ==  R.id.home) {
                replaceFragment(new HomeFragment(""));
                return true;
            }
            return true;
        });
    }

    private  void replaceFragment (Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.flFragment, fragment);
        transaction.commit();
    }
}