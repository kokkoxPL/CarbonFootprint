package com.kokkoxpl.carbonfootprint.data.db.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "data_values")
public record DataValue(@PrimaryKey(autoGenerate = true) int id, String name, float cost) {

    @Override
    public int id() {
        return id;
    }
}