package com.kokkoxpl.carbonfootprint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.color.DynamicColors;
import com.kokkoxpl.carbonfootprint.data.db.AppDatabase;
import com.kokkoxpl.carbonfootprint.data.db.entities.DataValue;
import com.kokkoxpl.carbonfootprint.fragments.AboutFragment;
import com.kokkoxpl.carbonfootprint.fragments.HomeFragment;
import com.kokkoxpl.carbonfootprint.fragments.ReportFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AppDatabase appDatabase;
    private List<DataValue> dataValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DynamicColors.applyToActivityIfAvailable(this);
        setContentView(R.layout.activity_main);
        appDatabase = AppDatabase.newInstance(this);

        dataValues = appDatabase.appDao().getApps();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int menuItemId =  item.getItemId();

            if (menuItemId ==  R.id.menu_home) {
                replaceFragment(new HomeFragment(appDatabase, dataValues));
            }
            else if (menuItemId ==  R.id.menu_report) {
                replaceFragment(new ReportFragment(appDatabase));
            }
            else if (menuItemId ==  R.id.menu_about) {
                replaceFragment(new AboutFragment());
            }
            return true;
        });
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int currentNightMode = newConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        AppCompatDelegate appCompatDelegate = getDelegate();
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                appCompatDelegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                appCompatDelegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }
        appCompatDelegate.applyDayNight();

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void replaceFragment (Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
    }
}