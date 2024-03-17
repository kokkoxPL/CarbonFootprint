package com.kokkoxpl.carbonfootprint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kokkoxpl.carbonfootprint.data.db.AppDatabase;
import com.kokkoxpl.carbonfootprint.data.db.entities.DataValue;
import com.kokkoxpl.carbonfootprint.fragments.AboutFragment;
import com.kokkoxpl.carbonfootprint.fragments.HomeFragment;
import com.kokkoxpl.carbonfootprint.fragments.ReportFragment;

import java.util.List;

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
             if (menuItemId ==  R.id.menu_about) {
                replaceFragment(new AboutFragment());
            }
            return true;
        });
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
    }

    private void replaceFragment (Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
    }
}