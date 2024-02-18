package com.kokkoxpl.carbonfootprint.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageButton;

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
        ImageButton facebook = view.findViewById(R.id.about_social_media_1);
        ImageButton instagram = view.findViewById(R.id.about_social_media_2);

        facebook.setOnClickListener(l -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/profile.php?id=61556750903052")));
        });

        instagram.setOnClickListener(l -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/cyfrowy.usmiech")));
        });

        String[] list = getResources().getStringArray(R.array.about_list);
        GroupListAdapter groupListAdapter = new GroupListAdapter(list);

        recyclerView.setAdapter(groupListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);
    }
}