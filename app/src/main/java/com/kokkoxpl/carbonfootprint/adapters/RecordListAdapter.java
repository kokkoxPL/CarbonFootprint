package com.kokkoxpl.carbonfootprint.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
        private final ImageView logo;
        private final TextView name;
        private final TextView value;
        private final ImageButton minusButton;
        private final ImageButton plusButton;
        private final TextInputLayout quantityLayout;
        private final TextInputEditText quantityText;

        public RecordViewHolder(View view) {
            super(view);
            logo = view.findViewById(R.id.record_row_data_logo);
            name = view.findViewById(R.id.record_row_data_name);
            value = view.findViewById(R.id.record_row_data_value);
            minusButton = view.findViewById(R.id.record_row_minus_button);
            plusButton = view.findViewById(R.id.record_row_plus_button);
            quantityLayout = view.findViewById(R.id.textInputLayout);
            quantityText = view.findViewById(R.id.editTextUsername);
        }

        public void bind(final Data item, Record record, Context context) {
            int changeValue = 15;
            name.setText(item.getName());
            value.setText(String.format(Locale.getDefault(),"%.2f", item.getCost()));

            String uri = String.format("@drawable/%s_logo", item.getName().toLowerCase());
            int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
            logo.setImageDrawable(context.getResources().getDrawable(imageResource, context.getTheme()));

            plusButton.setOnClickListener(v -> {
                setQuantityEditText(changeValue);
            });

            minusButton.setOnClickListener(v -> {
                setQuantityEditText(-changeValue);
            });

            quantityText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        int value = Integer.parseUnsignedInt(s.toString());
                        if (value > 1440) {
                            Toast.makeText(context, "W jednym dniu jest 1440 minut", Toast.LENGTH_SHORT).show();
                            quantityText.setText("1440");
                            return;
                        }

                        record.setQuantity(value);
                        quantityLayout.setPrefixText(String.format("%s h\n%s min", value / 60, value % 60));
                    } catch (NumberFormatException e) {
                        quantityText.setText("0");
                    }
                }
            });

            quantityText.setText(String.valueOf(record.getQuantity()));
            quantityLayout.setPrefixTextColor(ColorStateList.valueOf(Color.LTGRAY));
        }

        private void setQuantityEditText(int changeValue) {
            try {
                quantityText.setText(String.valueOf(Integer.parseUnsignedInt(quantityText.getText().toString()) + changeValue));
            } catch (NumberFormatException e) {
                quantityText.setText("0");
            }
        }
    }
}