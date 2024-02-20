package com.kokkoxpl.carbonfootprint.data.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "data_values")
public class DataValue {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public float cost;
}