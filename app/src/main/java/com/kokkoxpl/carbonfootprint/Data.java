package com.kokkoxpl.carbonfootprint;

public class Data {
    private int id;
    private String name;
    private float cost;

    public Data(int id, String name, float cost) {
        this.id = id;
        this.name = name;
        this.cost = cost;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getCost() {
        return cost;
    }
}