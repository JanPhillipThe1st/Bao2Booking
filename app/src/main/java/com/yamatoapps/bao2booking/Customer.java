package com.yamatoapps.bao2booking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Customer extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListView lvVehicles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        lvVehicles = findViewById(R.id.lvVehicles);
        ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
        VehicleAdapterCustomer adapter = new VehicleAdapterCustomer(Customer.this,vehicles);
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