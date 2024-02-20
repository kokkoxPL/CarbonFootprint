package com.kokkoxpl.carbonfootprint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kokkoxpl.carbonfootprint.data.Data;
import com.kokkoxpl.carbonfootprint.data.db.AppDatabase;
import com.kokkoxpl.carbonfootprint.data.db.DatabaseManager;
import com.kokkoxpl.carbonfootprint.data.db.entities.DataValue;
import com.kokkoxpl.carbonfootprint.fragments.AboutFragment;
import com.kokkoxpl.carbonfootprint.fragments.HomeFragment;
import com.kokkoxpl.carbonfootprint.fragments.ReportFragment;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DatabaseManager databaseManager;
    private AppDatabase appDatabase;
    private List<Data> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseManager = new DatabaseManager(this).open();
        appDatabase = AppDatabase.newInstance(this);

        List<DataValue> dataValue = appDatabase.appDao().getApps();

        Log.d("CF", Arrays.toString(dataValue.toArray()));
//        data = databaseManager.getData();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int menuItemId =  item.getItemId();

            if (menuItemId ==  R.id.menu_home) {
                replaceFragment(new HomeFragment(databaseManager, data));
            }
            else if (menuItemId ==  R.id.menu_report) {
                replaceFragment(new ReportFragment(databaseManager, data));
            }
            else if (menuItemId ==  R.id.menu_about) {
                replaceFragment(new AboutFragment());
            }
            return true;
        });
        bottomNavigationView.setSelectedItemId(R.id.menu_about);
    }

    private void replaceFragment (Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment, fragment).commit();
    }

    @Override
    protected void onDestroy() {
        databaseManager.close();
        super.onDestroy();
    }
}