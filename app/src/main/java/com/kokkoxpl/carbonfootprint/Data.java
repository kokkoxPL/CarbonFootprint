package com.kokkoxpl.carbonfootprint;

public class Data {
    private final int id;
    private final float cost;
    private final String name;

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