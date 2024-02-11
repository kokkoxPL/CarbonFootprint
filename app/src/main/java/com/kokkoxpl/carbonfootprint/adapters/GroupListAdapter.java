package com.kokkoxpl.carbonfootprint.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kokkoxpl.carbonfootprint.R;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ItemViewHolder> {
    private final String[] list;

    public GroupListAdapter(String[] list) {
        this.list = list;
    }

    @NonNull
    @Override
    public GroupListAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new GroupListAdapter.ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GroupListAdapter.ItemViewHolder holder, final int position) {
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
            textView = itemView.findViewById(R.id.item_text_view);
        }

        public TextView getTextView() {
            return textView;
        }
    }
}