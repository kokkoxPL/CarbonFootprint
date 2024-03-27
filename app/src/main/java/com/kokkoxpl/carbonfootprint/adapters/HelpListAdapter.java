package com.kokkoxpl.carbonfootprint.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kokkoxpl.carbonfootprint.R;

public class HelpListAdapter extends RecyclerView.Adapter<HelpListAdapter.ItemViewHolder> {
    private final String[] list;

    public HelpListAdapter(String[] list) {
        this.list = list;
    }

    @NonNull
    @Override
    public HelpListAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new HelpListAdapter.ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.help_card, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HelpListAdapter.ItemViewHolder holder, final int position) {
        holder.getTextView().setText(list[position]);
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.help_card_text);
        }

        public TextView getTextView() {
            return textView;
        }
    }
}