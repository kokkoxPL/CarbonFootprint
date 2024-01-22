package com.kokkoxpl.carbonfootprint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {
    private final List<Data> data;
    private List<Record> records;

    public DataAdapter(List<Data> data, List<Record> records) {
        this.data = data;
        this.records = records;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new DataViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.data_card, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(DataViewHolder viewHolder, final int position) {
        viewHolder.bind(data.get(position), records.get(position));
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView quantityTextView;
        private final ImageButton minusButton;
        private final ImageButton plusButton;

        public DataViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.dataText);
            quantityTextView = view.findViewById(R.id.dataQuantity);
            minusButton = view.findViewById(R.id.minusButton);
            plusButton = view.findViewById(R.id.plusButton);
        }

        public void bind(final Data item, Record record) {
            nameTextView.setText(String.format("%s(%s)", item.getName(), item.getCost()));
            quantityTextView.setText(String.valueOf(record.getQuantity()));

            plusButton.setOnClickListener(v -> {
                int value = Integer.parseInt(quantityTextView.getText().toString());
                record.setQuantity(value + 1);
                quantityTextView.setText(String.valueOf(value + 1));
            });

            minusButton.setOnClickListener(v -> {
                int value = Integer.parseInt(quantityTextView.getText().toString());
                if(value > 0) {
                    record.setQuantity(value - 1);
                    quantityTextView.setText(String.valueOf(value - 1));
                }
            });
        }
    }
}