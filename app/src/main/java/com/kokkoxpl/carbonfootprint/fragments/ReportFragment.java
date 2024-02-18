package com.kokkoxpl.carbonfootprint.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.kokkoxpl.carbonfootprint.R;
import com.kokkoxpl.carbonfootprint.data.Data;
import com.kokkoxpl.carbonfootprint.data.Record;
import com.kokkoxpl.carbonfootprint.data.db.DatabaseManager;
import com.kokkoxpl.carbonfootprint.data.enums.ReportOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReportFragment extends Fragment {
    private TabLayout tabLayout;
    private TextView resultTextView;
    private PieChart pieChart;

    private final DatabaseManager databaseManager;
    private final List<Data> data;
    private List<Record> records;
    private Map<Integer, Float> dataCostMap;
    private Map<Integer, Integer> colorsMap;
    private List<PieEntry> pieEntries;
    private List<Integer> colors;

    public ReportFragment(DatabaseManager databaseManager, List<Data> data) {
        super(R.layout.fragment_report);
        this.databaseManager = databaseManager;
        this.data = data;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = view.findViewById(R.id.report_tab_options);
        resultTextView = view.findViewById(R.id.report_result);
        pieChart = view.findViewById(R.id.report_pie_chart);

        dataCostMap = new HashMap<>();
        colorsMap = new HashMap<>();

        int[] colors = {
            Color.parseColor("#FF004F"), Color.parseColor("#FC01D8"),
            Color.parseColor("#FFFC00"), Color.parseColor("#D50014"),
            Color.parseColor("#0866FF"), Color.parseColor("#1D9BF0"),
            Color.parseColor("#A544FF"), Color.parseColor("#FF4500")
        };

        for (Data value : data) {
            dataCostMap.put(value.getID(), value.getCost());
            colorsMap.put(value.getID(), colors[value.getID() - 1]);
        }

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
        ReportOptions reportOption;
        switch (position) {
            case 1 -> reportOption = ReportOptions.MONTH;
            case 2 -> reportOption = ReportOptions.YEAR;
            case 3 -> reportOption = ReportOptions.ALL;
            default -> reportOption = ReportOptions.WEEK;
        }
        records = databaseManager.getRecordsByDate(reportOption);
        setUsage();
        setChartData();
    }

    private void setUsage() {
        float result = records.stream()
                .map((record) -> record.getQuantity() * dataCostMap.get(record.getIdOfData()))
                .reduce(0f, Float::sum);

        resultTextView.setText(String.format(Locale.getDefault(),"%.2f g CO2e", result));
    }

    private void setPieChart() {
        colors = new ArrayList<>();
        pieEntries = new ArrayList<>();

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
        colors.clear();

        for (Data value : data) {
            float val = records.stream()
                    .filter(record -> record.getIdOfData() == value.getID())
                    .map((record) -> record.getQuantity() * dataCostMap.get(record.getIdOfData()))
                    .reduce(0f, Float::sum);

            if (val > 0) {
                pieEntries.add(new PieEntry(val, value.getName()));
                colors.add(colorsMap.get(value.getID()));
            }
        }
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();
    }
}