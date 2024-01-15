package com.kokkoxpl.carbonfootprint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "CF";

    private DatabaseHelper databaseHelper;
    private DataAdapter dataAdapter;

    private TextView dateTextView;
    private ImageButton prev;
    private ImageButton next;
    private Button save;
    private RecyclerView recyclerView;

    private String currentDate;
    List<Data> data;
    List<Record> records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateTextView = findViewById(R.id.dateTextView);
        prev = findViewById(R.id.prevDayButton);
        next = findViewById(R.id.nextDayButton);
        save = findViewById(R.id.saveButton);
        recyclerView = findViewById(R.id.dataView);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null && bundle.containsKey("DATE")) {
            currentDate = bundle.getString("DATE");
        } else {
            currentDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
        }

        dateTextView.setText(currentDate);

        databaseHelper = new DatabaseHelper(this);
        data = databaseHelper.getData();
        records = databaseHelper.getRecords(currentDate);

        dataAdapter = new DataAdapter(data, records);
        recyclerView.setAdapter(dataAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        save.setOnClickListener(v -> {
            databaseHelper.updateRecords(records, currentDate);
        });

        prev.setOnClickListener(v -> {
            changeDate(-1);
        });

        next.setOnClickListener(v -> {
            changeDate(1);
        });
    }

    public void changeDate(long days) {
        Bundle b = new Bundle();
        LocalDate date = LocalDate.parse(dateTextView.getText().toString());
        b.putString("DATE", date.plusDays(days).toString());
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(b);
        startActivity(intent);
    }
}