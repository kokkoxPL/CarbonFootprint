package com.kokkoxpl.carbonfootprint.data.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "data_records")
public class DataRecord {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private final int idOfData;
    private int quantity;
    private final String date;

    public DataRecord(int idOfData, int quantity, String date) {
        this.idOfData = idOfData;
        this.quantity = quantity;
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getIdOfData() {
        return idOfData;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDate() {
        return date;
    }
}