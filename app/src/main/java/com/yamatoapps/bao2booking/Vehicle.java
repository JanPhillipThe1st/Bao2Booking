package com.yamatoapps.bao2booking;

public class Vehicle {
    public String model = "";
    public String speed = "";
    public String displacement = "";
    public String capacity = "";
    public String features = "";
    public String image_url = "";
    public String id = "";

    public Vehicle(String model, String speed, String displacement, String capacity, String features, String image_url, String id) {
        this.model = model;
        this.speed = speed;
        this.displacement = displacement;
        this.capacity = capacity;
        this.features = features;
        this.image_url = image_url;
        this.id = id;
    }
}
