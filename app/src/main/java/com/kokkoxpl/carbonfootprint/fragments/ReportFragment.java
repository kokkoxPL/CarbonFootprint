package com.kokkoxpl.carbonfootprint.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.kokkoxpl.carbonfootprint.R;
import com.kokkoxpl.carbonfootprint.data.ReportRange;
import com.kokkoxpl.carbonfootprint.data.db.AppDatabase;
import com.kokkoxpl.carbonfootprint.data.db.entities.DataValue;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReportFragment extends Fragment {
    private TabLayout tabLayout;
    private TextView resultTextView;
    private PieChart pieChart;
    private BarChart barChart;

    private AppDatabase appDatabase;
    private List<PieEntry> pieEntries;
    private List<BarEntry> barEntries;
    private List<DataValue> dataValues;
    private Map<String, Float> dataRecordsCostMap;
    private Map<String, Map<String, Float>> dataReportMap;
    private ReportRange reportRange;
    private String fromDate;

    public ReportFragment() {
        super(R.layout.fragment_report);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = view.findViewById(R.id.report_tab_options);
        resultTextView = view.findViewById(R.id.report_result);
        barChart = view.findViewById(R.id.report_bar_chart);
        pieChart = view.findViewById(R.id.report_pie_chart);

        appDatabase = AppDatabase.newInstance(getContext());
        dataValues = appDatabase.dataValueDao().getApps();

        setBarChart();
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
        switch (position) {
            case 0 -> reportRange = ReportRange.WEEK;
            case 1 -> reportRange = ReportRange.MONTH;
            case 2 -> reportRange = ReportRange.YEAR;
            default -> reportRange = ReportRange.ALL;
        }
        LocalDate currentDate = LocalDate.now();
        String toDate = currentDate.toString();

        switch (reportRange) {
            case WEEK -> fromDate = currentDate.minusWeeks(1).plusDays(1).toString();
            case MONTH -> fromDate = currentDate.minusMonths(1).plusDays(1).toString();
            case YEAR -> fromDate = currentDate.minusYears(1).plusDays(1).toString();
            default -> fromDate = LocalDate.ofEpochDay(0).toString();
        }
        dataRecordsCostMap = appDatabase.dataRecordDao().getRecordsMapByDateRange(fromDate, toDate);
        dataReportMap = appDatabase.dataRecordDao().getRecordsMapByDateRangeGroup(fromDate, toDate);

        setUsage();
        setChartData();
    }

    private void setUsage() {
        float result = dataRecordsCostMap.values().stream().reduce(0f, Float::sum);
        resultTextView.setText(String.format(Locale.getDefault(),"%.2f g CO2e", result));
    }

    private void setBarChart() {
        barEntries = new ArrayList<>();

        barChart.getDescription().setEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.getLegend().setWordWrapEnabled(true);

        Legend legend = barChart.getLegend();
        legend.setTextSize(14f);
        legend.setTextColor(Color.GRAY);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setTextColor(Color.GRAY);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
    }

    private void setNewBarData() {
        int[] colors = {
                Color.parseColor("#e60049"), Color.parseColor("#0bb4ff"),
                Color.parseColor("#ffa300"), Color.parseColor("#b3d4ff"),
                Color.parseColor("#9b19f5"), Color.parseColor("#00bfa0"),
                Color.parseColor("#dc0ab4"), Color.parseColor("#e6d800")
        };

        BarDataSet dataSet = new BarDataSet(barEntries, "");
        dataSet.setDrawValues(false);
        dataSet.setColors(colors);
        dataSet.setStackLabels(dataValues.stream().map(DataValue::name).toArray(String[]::new));

        BarData data = new BarData(dataSet);
        barChart.setData(data);
    }

    private void setPieChart() {
        pieEntries = new ArrayList<>();

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format(Locale.getDefault(),"%.2f g", value);
            }
        });
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
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
        legend.setTextColor(Color.GRAY);

        pieChart.setData(data);
    }

    private void setChartData() {
        List<PieEntry> pieEntriesTemp = new ArrayList<>();
        barEntries.clear();
        pieEntries.clear();

        for (var dataRecordsCostMap : dataRecordsCostMap.entrySet()) {
            float val = dataRecordsCostMap.getValue();

            if (val > 0) {
                pieEntriesTemp.add(new PieEntry(val, dataRecordsCostMap.getKey()));
            }
        }

        setBarEntries();

        pieEntriesTemp.sort(Comparator.comparing(PieEntry::getValue));
        Collections.reverse(pieEntriesTemp);
        pieEntries.addAll(pieEntriesTemp.subList(0, Math.min(3, pieEntriesTemp.size())));

        setNewBarData();
        barChart.notifyDataSetChanged();
        barChart.invalidate();

        pieChart.notifyDataSetChanged();
        pieChart.invalidate();
    }

    private void setBarEntries() {
        List<String> xValues = new ArrayList<>();
        LocalDate startDate = LocalDate.parse(fromDate);
        LocalDate endDate = LocalDate.now();
        float x = 0;

        for (; startDate.isBefore(endDate); startDate = startDate.plusDays(1)) {
            xValues.add(startDate.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("pl", "PL")));

            Map<String, Float> apps = dataReportMap.get(startDate.toString());
            if (apps != null) {
                float[] values = new float[dataValues.size()];

                for (int i = 0; i < dataValues.size(); i++) {
                    values[i] = apps.getOrDefault(dataValues.get(i).name(), 0f);
                }

                barEntries.add(new BarEntry(x++, values));
            } else {
                barEntries.add(new BarEntry(x++, 0));
            }
        }
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
    }
}