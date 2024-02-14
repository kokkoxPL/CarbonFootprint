package com.kokkoxpl.carbonfootprint.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kokkoxpl.carbonfootprint.R;
import com.kokkoxpl.carbonfootprint.data.Data;
import com.kokkoxpl.carbonfootprint.data.Record;

import java.util.List;
import java.util.Locale;


public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.RecordViewHolder> {
    private final List<Data> data;
    private List<Record> records;
    private final Context context;


    public RecordListAdapter(List<Data> data, List<Record> records, Context context) {
        this.data = data;
        this.records = records;
        this.context = context;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new RecordViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.record_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder viewHolder, final int position) {
        viewHolder.bind(data.get(position), records.get(position), context);
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
        private final ImageView logoImageView;
        private final TextView nameTextView;
        private final TextView valueTextView;
        private final EditText quantityEditText;
        private final ImageButton minusButton;
        private final ImageButton plusButton;

        public RecordViewHolder(View view) {
            super(view);
            logoImageView = view.findViewById(R.id.record_row_data_logo);
            nameTextView = view.findViewById(R.id.record_row_data_name);
            valueTextView = view.findViewById(R.id.record_row_data_value);
            quantityEditText = view.findViewById(R.id.record_row_data_quantity);
            minusButton = view.findViewById(R.id.record_row_minus_button);
            plusButton = view.findViewById(R.id.record_row_plus_button);
        }


        public void bind(final Data item, Record record, Context context) {
            int changeValue = 15;
            nameTextView.setText(item.getName());
            valueTextView.setText(String.format(Locale.getDefault(),"%.2f", item.getCost()));

            String uri = String.format("@drawable/%s_logo", item.getName().toLowerCase());
            int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
            logoImageView.setImageDrawable(context.getResources().getDrawable(imageResource, context.getTheme()));

            quantityEditText.setText(String.valueOf(record.getQuantity()));

            plusButton.setOnClickListener(v -> {
                setQuantityEditText(changeValue);
            });

            minusButton.setOnClickListener(v -> {
                setQuantityEditText(-changeValue);
            });

            quantityEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().equals("")) {
                        quantityEditText.setText("0");
                        return;
                    }
                    record.setQuantity(Integer.parseInt(s.toString()));
                }
            });
        }

        private void setQuantityEditText(int changeValue) {
            String value = quantityEditText.getText().toString();
            if (value.equals("")) value = "0";

            int newValue = Integer.parseInt(value) + changeValue;
            if (newValue < 0) newValue = 0;

            quantityEditText.setText(String.valueOf(newValue));
        }
    }
}