package com.kokkoxpl.carbonfootprint.data.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "data_records")
public class DataRecord {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int idOfData;
    public int quantity;
    public String date;
}