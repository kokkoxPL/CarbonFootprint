package com.kokkoxpl.carbonfootprint.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.kokkoxpl.carbonfootprint.data.db.entities.DataRecord;
import com.kokkoxpl.carbonfootprint.data.db.entities.DataValue;

import java.util.List;
import java.util.Locale;


public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.RecordViewHolder> {
    private final List<DataValue> dataValues;
    private List<DataRecord> dataRecords;
    private final Context context;


    public RecordListAdapter(List<DataValue> dataValues, List<DataRecord> dataRecords, Context context) {
        this.dataValues = dataValues;
        this.dataRecords = dataRecords;
        this.context = context;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new RecordViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.record_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder viewHolder, final int position) {
        viewHolder.bind(dataValues.get(position), dataRecords.get(position), context);
    }

    public void setRecords(@NonNull List<DataRecord> records) {
        this.dataRecords = records;
        notifyItemRangeChanged(0, getItemCount());
    }

    @Override
    public int getItemCount() {
        return dataRecords.size();
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

        public void bind(final DataValue dataValue, DataRecord dataRecord, Context context) {
            final int CHANGE_VALUE = 15;

            name.setText(dataValue.getName());
            value.setText(String.format(Locale.getDefault(),"%.2f", dataValue.getCost()));
            logo.setImageResource(context.getResources().getIdentifier(String.format("@drawable/logo_%s", dataValue.getName().toLowerCase()), null, context.getPackageName()));

            plusButton.setOnClickListener(v -> {
                setQuantityEditText(CHANGE_VALUE);
            });

            minusButton.setOnClickListener(v -> {
                setQuantityEditText(-CHANGE_VALUE);
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

                        dataRecord.setQuantity(value);
                        quantityLayout.setHint(String.format("%s h %s min", value / 60, value % 60));
                    } catch (NumberFormatException e) {
                        quantityText.setText("0");
                    }
                }
            });

            quantityText.setText(String.valueOf(dataRecord.getQuantity()));
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