package com.kokkoxpl.carbonfootprint;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutFragment extends Fragment {
    RecyclerView recyclerView;

    public AboutFragment() {
        super(R.layout.fragment_about);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.about_group_list);

        GrupListAdapter grupListAdapter = new GrupListAdapter(getResources().getStringArray(R.array.about_list));

        recyclerView.setAdapter(grupListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    public static class GrupListAdapter extends RecyclerView.Adapter<GrupListAdapter.ItemViewHolder> {
        String[] items;

        GrupListAdapter(String[] items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
            holder.getTextView().setText(items[position]);
            Log.d("CF", items[position]);
        }

        @Override
        public int getItemCount() {
            return items.length;
        }

        public static class ItemViewHolder extends RecyclerView.ViewHolder {
            public TextView textView;

            public ItemViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.item_text_view);
            }

            public TextView getTextView() {
                return textView;
            }
        }
    }
}