package com.kokkoxpl.carbonfootprint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kokkoxpl.carbonfootprint.fragments.AboutFragment;
import com.kokkoxpl.carbonfootprint.fragments.HelpFragment;
import com.kokkoxpl.carbonfootprint.fragments.HomeFragment;
import com.kokkoxpl.carbonfootprint.fragments.ReportFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int menuItemId =  item.getItemId();

            if (menuItemId ==  R.id.menu_home) {
                replaceFragment(new HomeFragment());
            }
            else if (menuItemId ==  R.id.menu_report) {
                replaceFragment(new ReportFragment());
            }
            else if (menuItemId ==  R.id.menu_help) {
                replaceFragment(new HelpFragment());
            }
            else if (menuItemId ==  R.id.menu_about) {
                replaceFragment(new AboutFragment());
            }
            return true;
        });
        bottomNavigationView.setSelectedItemId(R.id.menu_about);
    }

    private void replaceFragment (Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
    }
}