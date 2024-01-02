package com.kokkoxpl.carbonfootprint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {
    private final List<Data> list;
    private List<Record> records;

    public DataAdapter(List<Data> list, List<Record> records) {
        this.list = list;
        this.records = records;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.data_card, viewGroup, false);

        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataViewHolder viewHolder, final int position) {
        viewHolder.bind(list.get(position), records.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
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
                quantityTextView.setText(String.valueOf(Integer.valueOf(quantityTextView.getText().toString()) + 1));
            });

            minusButton.setOnClickListener(v -> {
                if(Integer.valueOf(quantityTextView.getText().toString()) > 0) {
                    quantityTextView.setText(String.valueOf(Integer.valueOf(quantityTextView.getText().toString()) - 1));
                }
            });
        }
    }
}