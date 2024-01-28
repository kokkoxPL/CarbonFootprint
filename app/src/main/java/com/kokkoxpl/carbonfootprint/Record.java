package com.kokkoxpl.carbonfootprint;

public class Record {
    private final int id;
    private final int idOfData;
    private int quantity;
    private final String date;

    public Record(int id, int idOfData, int quantity, String date) {
        this.id = id;
        this.idOfData = idOfData;
        this.quantity = quantity;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getIdOfData() {
        return idOfData;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDate() {
        return date;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}