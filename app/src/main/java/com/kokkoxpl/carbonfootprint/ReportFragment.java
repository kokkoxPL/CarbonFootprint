package com.kokkoxpl.carbonfootprint;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class ReportFragment extends Fragment {
    private DatabaseManager databaseManager;
    private Spinner spinner;
    private TextView result;

    public ReportFragment(DatabaseManager databaseManager) {
        super(R.layout.fragment_report);
        this.databaseManager = databaseManager;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner = view.findViewById(R.id.report_spinner);
        result = view.findViewById(R.id.report_result);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.option_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                result.setText(databaseManager.getReport(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

}