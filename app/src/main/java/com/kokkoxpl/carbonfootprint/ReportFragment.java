package com.kokkoxpl.carbonfootprint;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.renderer.LegendRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.kokkoxpl.carbonfootprint.enums.ReportOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReportFragment extends Fragment {
    private Spinner spinner;
    private TextView resultTextView;
    private PieChart pieChart;

    private DatabaseManager databaseManager;
    private final List<Data> data;
    private List<Record> records;
    private Map<Integer, Float> dataCostMap;

    public ReportFragment(DatabaseManager databaseManager, List<Data> data) {
        super(R.layout.fragment_report);
        this.databaseManager = databaseManager;
        this.data = data;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner = view.findViewById(R.id.report_spinner);
        resultTextView = view.findViewById(R.id.report_result);
        pieChart = view.findViewById(R.id.report_pie_chart);

        dataCostMap = new HashMap<>();

        for (Data value : data) {
            dataCostMap.put(value.getID(), value.getCost());
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.report_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ReportOptions reportOption;
                switch (position) {
                    case 1 -> reportOption = ReportOptions.MONTH;
                    case 2 -> reportOption = ReportOptions.YEAR;
                    case 3 -> reportOption = ReportOptions.ALL;
                    default -> reportOption = ReportOptions.WEEK;
                }
                records = databaseManager.getRecordsByDate(reportOption);
                setUsage();
                setPieChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

    }

    private void setUsage() {
        float result = records.stream()
                .map((record) -> record.getQuantity() * dataCostMap.get(record.getIdOfData()))
                .reduce(0f, Float::sum);

        resultTextView.setText(String.format(Locale.getDefault(),"%.2f", result));
    }

    private void setPieChart() {
        List<PieEntry> entries = new ArrayList<>();
        int[] colors = {
                Color.parseColor("#FF004F"), Color.parseColor("#FC01D8"),
                Color.parseColor("#FFFC00"), Color.parseColor("#D50014"),
                Color.parseColor("#0866FF"), Color.parseColor("#1D9BF0"),
                Color.parseColor("#A544FF"), Color.parseColor("#FF4500")
        };

        for (Data value : data) {
            float f = records.stream()
                    .filter(record -> record.getIdOfData() == value.getID())
                    .map((record) -> record.getQuantity() * dataCostMap.get(record.getIdOfData()))
                    .reduce(0f, Float::sum);


            entries.add(new PieEntry(f, value.getName()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);
        dataSet.setValueTextSize(14f);
        dataSet.setSliceSpace(1f);
        dataSet.setValueTextColor(Color.BLACK);

        PieData data = new PieData(dataSet);
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(40f);
        pieChart.setTransparentCircleRadius(0f);
        pieChart.setDrawEntryLabels(false);

        Legend legend = pieChart.getLegend();
        legend.setTextSize(14f);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setWordWrapEnabled(true);

        pieChart.setData(data);
        pieChart.invalidate();
    }
}