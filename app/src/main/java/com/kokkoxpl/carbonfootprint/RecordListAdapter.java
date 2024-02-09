package com.kokkoxpl.carbonfootprint;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.RecordViewHolder> {
    private final List<Data> data;
    private List<Record> records;

    public RecordListAdapter(List<Data> data, List<Record> records) {
        this.data = data;
        this.records = records;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new RecordViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.record_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder viewHolder, final int position) {
        viewHolder.bind(data.get(position), records.get(position));
    }

    public void setRecords(@NonNull List<Record> records) {
        this.records = records;
        notifyItemRangeChanged(0, getItemCount());
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public static class RecordViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView valueTextView;
        private final EditText quantityEditText;
        private final ImageButton minusButton;
        private final ImageButton plusButton;

        public RecordViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.record_row_data_name);
            valueTextView = view.findViewById(R.id.record_row_data_value);
            quantityEditText = view.findViewById(R.id.record_row_data_quantity);
            minusButton = view.findViewById(R.id.record_row_minus_button);
            plusButton = view.findViewById(R.id.record_row_plus_button);
        }


        public void bind(final Data item, Record record) {
            int changeValue = 15;
            nameTextView.setText(item.getName());
            valueTextView.setText(String.format("%sg CO2e/min", item.getCost()));

            quantityEditText.setText(String.valueOf(record.getQuantity()));

            plusButton.setOnClickListener(v -> {
                changeQuantity(changeValue, record);
            });

            minusButton.setOnClickListener(v -> {
                changeQuantity(-changeValue, record);
            });
        }

        private void changeQuantity(int i, Record record) {
            String value = quantityEditText.getText().toString();
            if (value.equals("")) return;

            int newValue = Integer.parseInt(value) + i;
            if (newValue < 0) newValue = 0;

            record.setQuantity(newValue);
            quantityEditText.setText(String.valueOf(newValue));
        }
    }
}