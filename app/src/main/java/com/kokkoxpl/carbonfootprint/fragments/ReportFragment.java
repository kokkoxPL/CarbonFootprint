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
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.tabs.TabLayout;
import com.kokkoxpl.carbonfootprint.R;
import com.kokkoxpl.carbonfootprint.data.ReportRange;
import com.kokkoxpl.carbonfootprint.data.db.AppDatabase;
import com.kokkoxpl.carbonfootprint.data.db.entities.DataValue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
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

    private ReportRange getReportRange(int position) {
        return switch (position) {
            case 0 -> ReportRange.WEEK;
            case 1 -> ReportRange.MONTH;
            case 2 -> ReportRange.YEAR;
            default -> ReportRange.ALL;
        };
    }

    private void setFromDate(ReportRange reportRange) {
        LocalDate currentDate = LocalDate.now();
        switch (reportRange) {
            case WEEK -> fromDate = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toString();
            case MONTH -> fromDate =  currentDate.with(TemporalAdjusters.firstDayOfMonth()).toString();
            case YEAR -> fromDate =  currentDate.with(TemporalAdjusters.firstDayOfYear()).toString();
            case ALL -> fromDate =  LocalDate.ofEpochDay(0).toString();
        }
    }

    private void getRecords(int position) {
        String currentDate = LocalDate.now().toString();
        ReportRange reportRange = getReportRange(position);
        setFromDate(reportRange);

        dataRecordsCostMap = appDatabase.dataRecordDao().getRecordsMapByDateRange(fromDate, currentDate);
        dataReportMap = appDatabase.dataRecordDao().getRecordsMapByDateRangeGroup(fromDate, currentDate);

        setUsage();
        setChartData(reportRange);
    }

    private void setUsage() {
        float result = dataRecordsCostMap.values().stream().reduce(0f, Float::sum);
        resultTextView.setText(String.format(Locale.getDefault(),"%.2f g CO2e", result));
    }

    private void setBarChart() {
        barEntries = new ArrayList<>();
        int color = MaterialColors.getColor(getView(), android.R.attr.textColorPrimary);

        int[] colors = {
                Color.parseColor("#FF004F"), Color.parseColor("#FC01D8"),
                Color.parseColor("#FFFC00"), Color.parseColor("#D50014"),
                Color.parseColor("#0866FF"), Color.parseColor("#1D9BF0"),
                Color.parseColor("#A544FF"), Color.parseColor("#FF4500")
        };

        // WHY DO I HAVE TO DO THIS !!!
        barEntries.add(new BarEntry(0f, new float[] {0, 0, 0, 0, 0, 0, 0, 0}));
        BarDataSet dataSet = new BarDataSet(barEntries, "");
        barEntries.clear();

        dataSet.setDrawValues(false);
        dataSet.setColors(colors);
        dataSet.setStackLabels(dataValues.stream().map(DataValue::name).toArray(String[]::new));

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.6f);

        barChart.getDescription().setEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.getLegend().setWordWrapEnabled(true);

        Legend legend = barChart.getLegend();
        legend.setTextSize(14f);
        legend.setTextColor(color);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setTextColor(color);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

        YAxis leftAxis = barChart.getAxisLeft();
        YAxis rightAxis = barChart.getAxisRight();
        leftAxis.setTextColor(color);
        rightAxis.setTextColor(color);

        barChart.setData(data);
    }

    private void setPieChart() {
        pieEntries = new ArrayList<>();
        int color = MaterialColors.getColor(getView(), android.R.attr.textColorPrimary);

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
        legend.setTextColor(color);

        pieChart.setData(data);
    }

    private void setChartData(ReportRange reportRange) {
        setPieEntries();
        setBarEntries(reportRange);

        // I HATE BAR CHART
        ((BarDataSet) barChart.getBarData().getDataSetByIndex(0)).notifyDataSetChanged();
        barChart.getBarData().notifyDataChanged();
        barChart.notifyDataSetChanged();
        barChart.invalidate();

        pieChart.notifyDataSetChanged();
        pieChart.invalidate();

    }

    private void setBarEntries(ReportRange reportRange) {
        barEntries.clear();
        List<String> xValues = new ArrayList<>();
        LocalDate date = LocalDate.parse(fromDate);

        TextView textView = getView().findViewById(R.id.report_report_2);
        switch (reportRange) {
            case WEEK -> {
                textView.setText(R.string.report_report_2);

                final int TIME = 7;

                for (int i = 0; i < TIME; i++) {
                    xValues.add(date.getDayOfWeek().getDisplayName(TextStyle.SHORT, new Locale("pl", "PL")));

                    Map<String, Float> apps = dataReportMap.get(date.toString());
                    if (apps != null) {
                        float[] values = new float[dataValues.size()];

                        for (int j = 0; j < dataValues.size(); j++) {
                            values[j] = apps.getOrDefault(dataValues.get(j).name(), 0f);
                        }

                        barEntries.add(new BarEntry(i, values));
                    } else {
                        barEntries.add(new BarEntry(i, new float[] {0, 0, 0, 0, 0, 0, 0, 0}));
                    }
                    date = date.plusDays(1);
                }
            }
            case MONTH -> {
                textView.setText(R.string.report_report_2_extra);

                final int TIME = date.lengthOfMonth();

                float[] values = new float[] {0, 0, 0, 0, 0, 0, 0, 0};

                for (int i = 0; i < TIME; i++) {
                    Map<String, Float> apps = dataReportMap.get(date.toString());

                    if (apps != null) {
                        for (int j = 0; j < dataValues.size(); j++) {
                            values[j] += apps.getOrDefault(dataValues.get(j).name(), 0f);
                        }
                    }

                    if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                        xValues.add(String.format("%s %s", getResources().getString(R.string.report_options_0).toLowerCase(), ((i / 7) + 1)));
                        barEntries.add(new BarEntry((float) (i / 7), values));
                        values = new float[] {0, 0, 0, 0, 0, 0, 0, 0};
                    }
                    date = date.plusDays(1);
                }
            }
            case YEAR -> {
                textView.setText(R.string.report_report_2);

                final int TIME = 12;

                for (int i = 0; i < TIME; i++) {
                    xValues.add(date.getMonth().getDisplayName(TextStyle.SHORT, new Locale("pl", "PL")));

                    float[] values = new float[] {0, 0, 0, 0, 0, 0, 0, 0};

                    for (LocalDate dayOfMonth = date; dayOfMonth.isBefore(date.plusMonths(1)); dayOfMonth = dayOfMonth.plusDays(1)) {
                        Map<String, Float> apps = dataReportMap.get(dayOfMonth.toString());

                        if (apps != null) {
                            for (int j = 0; j < dataValues.size(); j++) {
                                values[j] += apps.getOrDefault(dataValues.get(j).name(), 0f);
                            }
                        }
                    }

                    barEntries.add(new BarEntry(i, values));
                    date = date.plusMonths(1);
                }
            }
            case ALL -> {
                textView.setText(R.string.report_report_2);

                float[] values = new float[] {0, 0, 0, 0, 0, 0, 0, 0};
                int i = 0;

                for (var varEntry : dataReportMap.entrySet()) {
                    LocalDate newDate = LocalDate.parse(varEntry.getKey(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    if((date.getYear() != newDate.getYear()) && !date.isEqual(LocalDate.ofEpochDay(0))) {
                        barEntries.add(new BarEntry(i++, values));
                        xValues.add(String.valueOf(date.getYear()));
                        values = new float[] {0, 0, 0, 0, 0, 0, 0, 0};
                    }

                    Map<String, Float> apps = varEntry.getValue();

                    if (apps != null) {
                        for (int j = 0; j < dataValues.size(); j++) {
                            values[j] += apps.getOrDefault(dataValues.get(j).name(), 0f);
                        }
                    }
                    date = newDate;
                }

                xValues.add(String.valueOf(date.getYear()));
                barEntries.add(new BarEntry(i, values));
            }
        }
        barChart.getXAxis().setLabelCount(barEntries.size());
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
    }

    private void setPieEntries() {
        List<PieEntry> pieEntriesTemp = new ArrayList<>();
        pieEntries.clear();

        for (var dataRecordsCostMap : dataRecordsCostMap.entrySet()) {
            float val = dataRecordsCostMap.getValue();

            if (val > 0) {
                pieEntriesTemp.add(new PieEntry(val, dataRecordsCostMap.getKey()));
            }
        }

        pieEntriesTemp.sort(Comparator.comparing(PieEntry::getValue));
        Collections.reverse(pieEntriesTemp);
        pieEntries.addAll(pieEntriesTemp.subList(0, Math.min(3, pieEntriesTemp.size())));
    }
}