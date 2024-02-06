package com.kokkoxpl.carbonfootprint;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HomeFragment extends Fragment {
    private TextView dateTextView;
    private ImageButton prev;
    private ImageButton next;
    private Button save;
    private RecyclerView recyclerView;

    private DatabaseManager databaseManager;
    private RecordListAdapter recordListAdapter;
    private LocalDate currentDate;
    private List<Data> data;
    private List<Record> records;

    public HomeFragment(DatabaseManager databaseManager, List<Data> data) {
        super(R.layout.fragment_home);
        this.databaseManager = databaseManager;
        this.data = data;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dateTextView = view.findViewById(R.id.home_current_date);
        prev = view.findViewById(R.id.home_previous_date);
        next = view.findViewById(R.id.home_next_date);
        save = view.findViewById(R.id.home_save_records);
        recyclerView = view.findViewById(R.id.home_record_list);

        currentDate = LocalDate.now();
        setNewDate();

        recordListAdapter = new RecordListAdapter(data, records);
        recyclerView.setAdapter(recordListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        save.setOnClickListener(v -> {
            databaseManager.updateRecords(records);
        });

        prev.setOnClickListener(v -> {
            changeDate(-1);
        });

        next.setOnClickListener(v -> {
            changeDate(1);
        });
    }

    public void changeDate(int days) {
        currentDate = currentDate.plusDays(days);
        setNewDate();

        recordListAdapter.setRecords(records);
    }

    public void setNewDate() {
        String newDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(currentDate);
        dateTextView.setText(newDate);
        records = databaseManager.getRecords(newDate);
    }
}