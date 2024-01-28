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

    private DatabaseHelper databaseHelper;
    private DataAdapter dataAdapter;
    private LocalDate currentDate;
    private List<Data> data;
    private List<Record> records;

    public HomeFragment(DatabaseHelper databaseHelper, List<Data> data) {
        super(R.layout.fragment_home);
        this.databaseHelper = databaseHelper;
        this.data = data;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dateTextView = view.findViewById(R.id.dateTextView);
        prev = view.findViewById(R.id.prevDayButton);
        next = view.findViewById(R.id.nextDayButton);
        save = view.findViewById(R.id.saveButton);
        recyclerView = view.findViewById(R.id.dataView);

        currentDate = LocalDate.now();
        setNewDate();

        dataAdapter = new DataAdapter(data, records);
        recyclerView.setAdapter(dataAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        save.setOnClickListener(v -> {
            databaseHelper.updateRecords(records);
        });

        prev.setOnClickListener(v -> {
            changeDate(-1);
        });

        next.setOnClickListener(v -> {
            changeDate(1);
        });
    }

    public void changeDate(int days) {
        currentDate = currentDate.plusDays(1);
        setNewDate();

        dataAdapter.setRecords(records);
        dataAdapter.notifyItemRangeChanged(0, dataAdapter.getItemCount());
    }

    public void setNewDate() {
        String newDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(currentDate);
        dateTextView.setText(newDate);
        records = databaseHelper.getRecords(newDate);
    }
}