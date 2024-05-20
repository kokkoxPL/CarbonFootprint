package com.kokkoxpl.carbonfootprint.adapters;

import android.content.Context;
import android.util.Log;
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
    private final List<String> textList;
    private final List<String> imageList;
    private final Context context;

    public HelpListAdapter(List<String> textList, List<String> imageList, Context context) {
        this.textList = textList;
        this.imageList = imageList;
        this.context = context;
    }

    private List<String> newList(List<String> list) {
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
        int pos = position;
        if (pos == textList.size() + 1) pos = 1;
        else if (pos == 0) pos = textList.size();

        holder.getTextView().setText(newList(textList).get(position));
        holder.getImageView().setImageResource(context.getResources().getIdentifier(String.format("@drawable/%s", newList(imageList).get(pos)), null, context.getPackageName()));
    }

    @Override
    public int getItemCount() {
        return textList.size() + 2;
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