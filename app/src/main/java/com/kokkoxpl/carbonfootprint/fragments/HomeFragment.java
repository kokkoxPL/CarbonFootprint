package com.kokkoxpl.carbonfootprint.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kokkoxpl.carbonfootprint.R;
import com.kokkoxpl.carbonfootprint.adapters.RecordListAdapter;
import com.kokkoxpl.carbonfootprint.data.db.AppDatabase;
import com.kokkoxpl.carbonfootprint.data.db.entities.DataRecord;
import com.kokkoxpl.carbonfootprint.data.db.entities.DataValue;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private TextView dateTextView;
    private ImageButton prev;
    private ImageButton next;
    private ImageButton save;
    private RecyclerView recyclerView;
    CalendarView calendarView;

    private AppDatabase appDatabase;
    private RecordListAdapter recordListAdapter;
    private LocalDate currentDate;
    private List<DataValue> dataValues;
    private List<DataRecord> dataRecords;
    private Map<Integer, DataRecord> dataRecordsMap;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appDatabase = AppDatabase.newInstance(getContext());

        dataValues = appDatabase.appDao().getApps();

        dateTextView = view.findViewById(R.id.home_current_date);
        prev = view.findViewById(R.id.home_previous_day);
        next = view.findViewById(R.id.home_next_day);
        save = view.findViewById(R.id.home_save_records);
        recyclerView = view.findViewById(R.id.home_record_list);
        calendarView = view.findViewById(R.id.home_calendar);

        dataRecords = new ArrayList<>();
        dataRecordsMap = new HashMap<>();

        calendarView.setVisibility(View.GONE);

        currentDate = LocalDate.now();
        setNewDate();

        recordListAdapter = new RecordListAdapter(dataValues, dataRecords, getContext());
        recyclerView.setAdapter(recordListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);

        save.setOnClickListener(v -> {
            appDatabase.recordDao().updateRecords(dataRecords);
        });

        prev.setOnClickListener(v -> {
            changeDate(-1);
        });

        next.setOnClickListener(v -> {
            changeDate(1);
        });

        dateTextView.setOnClickListener(l -> {
            switch (calendarView.getVisibility()) {
                case View.GONE -> calendarView.setVisibility(View.VISIBLE);
                case View.VISIBLE -> calendarView.setVisibility(View.GONE);
            }
        });

        calendarView.setOnDateChangeListener((v, year, month, dayOfMonth) -> {
            currentDate = LocalDate.of(year, month + 1, dayOfMonth);
            changeDate(0);
            v.setVisibility(View.GONE);
        });
    }

    private void changeDate(int days) {
        currentDate = currentDate.plusDays(days);
        calendarView.setDate(currentDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
        setNewDate();
        recordListAdapter.setRecords(dataRecords);
    }

    private void setNewDate() {
        String newDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(currentDate);
        dateTextView.setText(newDate);

        dataRecordsMap = appDatabase.recordDao().getRecordsMapByDate(newDate);

        dataRecords.clear();
        for (DataValue dataValue : dataValues) {
            DataRecord dataRecord = dataRecordsMap.get(dataValue.getId());
            if (dataRecord == null) {
                dataRecord = new DataRecord(dataValue.getId(), 0, newDate);
            }
            dataRecords.add(dataRecord);
        }
    }
}