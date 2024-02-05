package com.kokkoxpl.carbonfootprint;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.View;

public class ReportFragment extends Fragment {
    private DatabaseManager databaseManager;

    public ReportFragment(DatabaseManager databaseManager) {
        super(R.layout.fragment_report);
        this.databaseManager = databaseManager;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}