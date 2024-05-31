package com.kokkoxpl.carbonfootprint.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kokkoxpl.carbonfootprint.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HelpListAdapter extends RecyclerView.Adapter<HelpListAdapter.ItemViewHolder> {
    private final List<String> textList;
    private final List<String> imageList;
    private final Map<String, Integer> drawableMap;

    public HelpListAdapter(List<String> textList, List<String> imageList) {
        this.textList = textList;
        this.imageList = imageList;
        this.drawableMap = new HashMap<>();

        Field[] drawables = R.drawable.class.getFields();
        for (Field field : drawables) {
            final String fieldName = field.getName();
            if (fieldName.startsWith("image_") || fieldName.startsWith("logo_")) {
                try {
                    drawableMap.put(fieldName, field.getInt(null));
                } catch (Exception ignored) {
                }
            }
        }
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

        Integer resId = drawableMap.get(newList(imageList).get(pos));
        holder.getImageView().setImageResource(Objects.requireNonNullElseGet(resId, () -> R.drawable.placeholder));
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