package com.kokkoxpl.carbonfootprint;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
    private final String TAG = "CF";

    private TextView dateTextView;
    private ImageButton prev;
    private ImageButton next;
    private Button save;
    private RecyclerView recyclerView;

    private DatabaseHelper databaseHelper;
    private DataAdapter dataAdapter;
    private String currentDate;
    private List<Data> data;
    private List<Record> records;

    public HomeFragment(String currentDate) {
        super(R.layout.fragment_home);
        this.currentDate = currentDate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dateTextView = view.findViewById(R.id.dateTextView);
        prev = view.findViewById(R.id.prevDayButton);
        next = view.findViewById(R.id.nextDayButton);
        save = view.findViewById(R.id.saveButton);
        recyclerView = view.findViewById(R.id.dataView);


        Bundle bundle = getActivity().getIntent().getExtras();

        if(bundle != null && bundle.containsKey("DATE")) {
            currentDate = bundle.getString("DATE");
        } else {
            currentDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
        }

        currentDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());

        dateTextView.setText(currentDate);

        databaseHelper = new DatabaseHelper(view.getContext());
        data = databaseHelper.getData();
        records = databaseHelper.getRecords(currentDate);

        dataAdapter = new DataAdapter(data, records);
        recyclerView.setAdapter(dataAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

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

    public void changeDate(int days) {
        Bundle b = new Bundle();
        LocalDate date = LocalDate.parse(dateTextView.getText().toString());
        b.putString("DATE", date.plusDays(days).toString());
        Intent intent = new Intent(getActivity(), HomeFragment.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(b);
        startActivity(intent);
    }
}