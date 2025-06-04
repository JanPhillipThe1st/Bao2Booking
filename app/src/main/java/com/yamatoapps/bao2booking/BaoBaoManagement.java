package com.yamatoapps.bao2booking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;


import java.util.ArrayList;

public class BaoBaoManagement extends AppCompatActivity {

    ListView lvVehicles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bao_bao_management);
        lvVehicles = findViewById(R.id.lvVehicles);
        ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
        VehicleAdapter adapter = new VehicleAdapter(BaoBaoManagement.this,vehicles);
        db.collection("baobao").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot snapshot : queryDocumentSnapshots){
                adapter.add(new Vehicle(
                        snapshot.getString("model"),
                        snapshot.getString("speed"),
                        snapshot.getString("displacement"),
                        snapshot.getString("capacity"),
                        snapshot.getString("features"),
                        snapshot.getString("image_url"),
                        snapshot.getId()
                ));
                lvVehicles.setAdapter(adapter);
            }
        });
    }
}