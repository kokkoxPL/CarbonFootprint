package com.kokkoxpl.carbonfootprint.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kokkoxpl.carbonfootprint.R;

import java.util.ArrayList;
import java.util.List;

public class HelpListAdapter extends RecyclerView.Adapter<HelpListAdapter.ItemViewHolder> {
    private final List<String> list;
    private final Context context;

    public HelpListAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    private List<String> newList() {
        List<String> newList = new ArrayList<>();
        newList.add(list.get(list.size() - 1));
        newList.addAll(list);
        newList.add(list.get(0));
        return newList;
    }

    @NonNull
    @Override
    public HelpListAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new HelpListAdapter.ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.help_card, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HelpListAdapter.ItemViewHolder holder, final int position) {
        int pos = switch (position) {
            case 8 -> 1;
            case 0 -> 7;
            default -> position;
        };
        holder.getTextView().setText(newList().get(position));
        holder.getImageView().setImageResource(context.getResources().getIdentifier(String.format("@drawable/image_%s", pos), null, context.getPackageName()));
    }

    @Override
    public int getItemCount() {
        return newList().size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView imageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.help_card_text);
            imageView = itemView.findViewById(R.id.help_card_image);
        }

        public TextView getTextView() {
            return textView;
        }
        public ImageView getImageView() {
            return imageView;
        }
    }
}