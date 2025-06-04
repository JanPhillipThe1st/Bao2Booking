package com.yamatoapps.bao2booking;

import java.util.Calendar;
import java.util.Date;

public class VehicleBooking {
    public String model = "";
    public String speed = "";
    public String displacement = "";
    public String capacity = "";
    public String features = "";
    public String image_url = "";
    public String id = "";
    public Date date_booked = Calendar.getInstance().getTime();
    public VehicleBooking(String model, String speed, String displacement, String capacity, String features,
                          String image_url, String id,Date date_booked ) {
        this.model = model;
        this.speed = speed;
        this.displacement = displacement;
        this.capacity = capacity;
        this.features = features;
        this.image_url = image_url;
        this.id = id;
        this.date_booked = date_booked;
    }
}
