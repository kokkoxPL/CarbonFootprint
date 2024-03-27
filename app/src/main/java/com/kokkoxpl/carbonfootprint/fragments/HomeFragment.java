package com.kokkoxpl.carbonfootprint.fragments;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Process;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.kokkoxpl.carbonfootprint.R;
import com.kokkoxpl.carbonfootprint.adapters.RecordListAdapter;
import com.kokkoxpl.carbonfootprint.data.db.AppDatabase;
import com.kokkoxpl.carbonfootprint.data.db.entities.DataRecord;
import com.kokkoxpl.carbonfootprint.data.db.entities.DataValue;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private TextView dateTextView;
    private ImageButton prev;
    private ImageButton next;
    private ImageButton save;
    private RecyclerView recyclerView;
    private CalendarView calendarView;
    private ExtendedFloatingActionButton extendedFloatingActionButton;

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

        dataValues = appDatabase.dataValueDao().getApps();

        dateTextView = view.findViewById(R.id.home_current_date);
        prev = view.findViewById(R.id.home_previous_day);
        next = view.findViewById(R.id.home_next_day);
        save = view.findViewById(R.id.home_save_records);
        recyclerView = view.findViewById(R.id.home_record_list);
        calendarView = view.findViewById(R.id.home_calendar);
        extendedFloatingActionButton = view.findViewById(R.id.home_get_data);

        dataRecords = new ArrayList<>();
        dataRecordsMap = new HashMap<>();

        calendarView.setVisibility(View.GONE);
        currentDate = LocalDate.now();
        setNewDate();

        recordListAdapter = new RecordListAdapter(dataValues, dataRecords, getContext());
        recyclerView.setAdapter(recordListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);

        save.setOnClickListener(v -> appDatabase.dataRecordDao().updateRecords(dataRecords));

        prev.setOnClickListener(v -> changeDate(-1));

        next.setOnClickListener(v -> changeDate(1));

        extendedFloatingActionButton.setOnClickListener(v -> getDataFromPhone());

        dateTextView.setOnClickListener(v -> {
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
        calendarView.setDate(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
        setNewDate();
        recordListAdapter.setRecords(dataRecords);
    }

    private void setNewDate() {
        String newDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(currentDate);
        dateTextView.setText(newDate);

        dataRecordsMap = appDatabase.dataRecordDao().getRecordsMapByDate(newDate);

        dataRecords.clear();
        for (DataValue dataValue : dataValues) {
            DataRecord dataRecord = dataRecordsMap.get(dataValue.id());
            if (dataRecord == null) {
                dataRecord = new DataRecord(dataValue.id(), 0, newDate);
            }
            dataRecords.add(dataRecord);
        }
    }

    private void getDataFromPhone() {
        @SuppressLint("ServiceCast")
        AppOpsManager appOpsManager = (AppOpsManager) getContext().getSystemService(AppCompatActivity.APP_OPS_SERVICE);

        if (appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), getContext().getPackageName()) != AppOpsManager.MODE_ALLOWED) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            return;
        }

        UsageStatsManager usageStatsManager = (UsageStatsManager) getContext().getSystemService(Context.USAGE_STATS_SERVICE);
        Map<String, UsageStats> usageStatsMap = usageStatsManager.queryAndAggregateUsageStats(
                currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                System.currentTimeMillis());

        for (DataValue dataValue : dataValues) {
            var app = usageStatsMap.get(dataValue.packageName());
            if (app != null) {
                DataRecord dataRecord = dataRecords.stream().filter(dataRecordS -> dataRecordS.getIdOfData() == dataValue.id()).findFirst().get();
                dataRecord.setQuantity((int) (app.getTotalTimeInForeground() / 60000));
            }
        }
        recordListAdapter.setRecords(dataRecords);
    }
}