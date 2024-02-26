package com.kokkoxpl.carbonfootprint.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.tabs.TabLayout;
import com.kokkoxpl.carbonfootprint.R;
import com.kokkoxpl.carbonfootprint.data.db.AppDatabase;
import com.kokkoxpl.carbonfootprint.data.db.entities.DataRecord;
import com.kokkoxpl.carbonfootprint.data.db.entities.DataValue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReportFragment extends Fragment {
    private TabLayout tabLayout;
    private TextView resultTextView;
    private PieChart pieChart;

    private final AppDatabase appDatabase;
    private Map<String, Float> dataRecordsCostMap;
    private List<PieEntry> pieEntries;

    public ReportFragment(AppDatabase appDatabase) {
        super(R.layout.fragment_report);
        this.appDatabase = appDatabase;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = view.findViewById(R.id.report_tab_options);
        resultTextView = view.findViewById(R.id.report_result);
        pieChart = view.findViewById(R.id.report_pie_chart);

        setPieChart();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                getRecords(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                getRecords(tab.getPosition());
            }
        });
        tabLayout.selectTab(tabLayout.getTabAt(0));
    }

    private void getRecords(int position) {
        LocalDate currentDate = LocalDate.now();
        String fromDate;
        String toDate = currentDate.toString();

        switch (position) {
            // WEEK
            case 0 -> fromDate = currentDate.minusWeeks(1).plusDays(1).toString();
            // MONTH
            case 1 -> fromDate = currentDate.minusMonths(1).plusDays(1).toString();
            // YEAR
            case 2 -> fromDate = currentDate.minusYears(1).plusDays(1).toString();
            // ALL
            default -> fromDate = LocalDate.ofEpochDay(0).toString();
        }
        dataRecordsCostMap = appDatabase.recordDao().getRecordsMapByDateRange(fromDate, toDate);
        setUsage();
        setChartData();
    }

    private void setUsage() {
        float result = dataRecordsCostMap.values().stream().reduce(0f, Float::sum);
        resultTextView.setText(String.format(Locale.getDefault(),"%.2f g CO2e", result));
    }

    private void setPieChart() {
        pieEntries = new ArrayList<>();
        int[] colors = {
                Color.parseColor("#e60049"), Color.parseColor("#0bb4ff"),
                Color.parseColor("#ffa300"), Color.parseColor("#b3d4ff"),
                Color.parseColor("#9b19f5"), Color.parseColor("#00bfa0"),
                Color.parseColor("#dc0ab4"), Color.parseColor("#e6d800")
        };

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format(Locale.getDefault(),"%.2f g", value);
            }
        });
        dataSet.setColors(colors);
        dataSet.setValueTextSize(12f);
        dataSet.setSliceSpace(2f);
        dataSet.setValueTextColor(Color.BLACK);

        PieData data = new PieData(dataSet);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(40f);
        pieChart.setTransparentCircleRadius(0f);
        pieChart.setDrawEntryLabels(false);
        pieChart.setHoleColor(Color.TRANSPARENT);

        Legend legend = pieChart.getLegend();
        legend.setTextSize(14f);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setWordWrapEnabled(true);
        legend.setTextColor(Color.LTGRAY);

        pieChart.setData(data);
    }

    private void setChartData() {
        pieEntries.clear();

        for (var dataRecordsCostMap : dataRecordsCostMap.entrySet()) {
            float val = dataRecordsCostMap.getValue();

            if (val > 0) {
                pieEntries.add(new PieEntry(val, dataRecordsCostMap.getKey()));
            }
        }

        pieChart.notifyDataSetChanged();
        pieChart.invalidate();
    }
}