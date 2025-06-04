package com.yamatoapps.bao2booking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;


import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class Admin extends AppCompatActivity {

    ArrayList<Map<String,String>> vehekols = new ArrayList();

    Button btnMyBajaj,btnBookings,btnLogOut;
    ListView lvBookings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Map<String,String> vehiclemap = new HashMap<>();
        vehiclemap.put("model","BAJAJ RE");
        vehiclemap.put("speed","150");
        vehiclemap.put("displacement","100cc");
        vehiclemap.put("capacity","6 People");
        vehiclemap.put("features","High power, stylish design, comfortable seats. New model!");
        vehiclemap.put("id","12345");
        vehiclemap.put("image_url","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRkkU8BOiBHisREMMnvQAVDFowE6QWFwhz6WA&s");
        vehiclemap.put("booking_date","2025-06-05");
        vehekols.add(vehiclemap);

        setContentView(R.layout.activity_admin);
        btnMyBajaj = findViewById(R.id.btnMyBajaj);
        btnBookings = findViewById(R.id.btnBookings);
        btnLogOut = findViewById(R.id.btnLogOut);
        lvBookings = findViewById(R.id.lvBookings);
        ArrayList<VehicleBooking> vehicles = new ArrayList<VehicleBooking>();
        VehicleBookingAdapter adapter = new VehicleBookingAdapter(Admin.this,vehicles);
            for (Map<String,String> vehekol : vehekols){
                adapter.add(new VehicleBooking(
                        vehekol.get("model"),
                        vehekol.get("speed"),
                        vehekol.get("displacement"),
                        vehekol.get("capacity"),
                        vehekol.get("features"),
                        vehekol.get("image_url"),
                        vehekol.get("id"),
                        Date.valueOf(vehekol.get("booking_date"))
                ));
                lvBookings.setAdapter(adapter);
            }
        btnLogOut.setOnClickListener(view ->{
            finish();
        });
        btnMyBajaj.setOnClickListener(view -> {
            startActivity(new Intent(Admin.this,ManageBaoBao.class));
        });
    }
}