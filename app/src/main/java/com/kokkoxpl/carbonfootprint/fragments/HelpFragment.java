package com.kokkoxpl.carbonfootprint.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.View;
import android.widget.TextView;

import com.kokkoxpl.carbonfootprint.R;
import com.kokkoxpl.carbonfootprint.adapters.HelpListAdapter;

public class HelpFragment extends Fragment {
    private RecyclerView recyclerView;

    private HelpListAdapter helpListAdapter;
    private String[] list;

    public HelpFragment() {
        super(R.layout.fragment_help);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.help_list);

        list = getResources().getStringArray(R.array.help_grid_array);
        helpListAdapter = new HelpListAdapter(list);

        recyclerView.setAdapter(helpListAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }
}