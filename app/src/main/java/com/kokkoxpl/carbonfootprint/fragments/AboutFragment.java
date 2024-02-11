package com.kokkoxpl.carbonfootprint.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.kokkoxpl.carbonfootprint.R;
import com.kokkoxpl.carbonfootprint.adapters.GroupListAdapter;

public class AboutFragment extends Fragment {
    public AboutFragment() {
        super(R.layout.fragment_about);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.about_group_list);

        String[] list = getResources().getStringArray(R.array.about_list);
        GroupListAdapter groupListAdapter = new GroupListAdapter(list);

        recyclerView.setAdapter(groupListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }
}