package com.yamatoapps.bao2booking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VehicleAdapterCustomer extends ArrayAdapter<Vehicle>  {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public VehicleAdapterCustomer(@NonNull Context context, ArrayList<Vehicle> objects) {
        super(context, 0, objects);
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Vehicle item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bao_bao_card_customer, parent, false);
        }
        // Lookup view for data population
        TextView tvModel = (TextView) convertView.findViewById(R.id.tvModel);
        TextView tvCapacity = (TextView) convertView.findViewById(R.id.tvCapacity);
        TextView tvSpeed = (TextView) convertView.findViewById(R.id.tvSpeed);
        TextView tvFeatures = (TextView) convertView.findViewById(R.id.tvFeatures);
        ImageView ivBaoBao = (ImageView) convertView.findViewById(R.id.ivBaoBao);
        Button btnBookVehicle;
        btnBookVehicle = convertView.findViewById(R.id.btnBookVehicle);

        tvModel.setText(item.model);
        tvCapacity.setText(item.capacity);
        tvSpeed.setText(item.speed);
        tvFeatures.setText(item.features);
        Picasso.get().load(item.image_url).into(ivBaoBao);



        btnBookVehicle.setOnClickListener(view -> {
            Intent intent = new Intent(parent.getContext(), BookVehicle.class);
            intent.putExtra("document_id",item.id);
            parent.getContext().startActivity(intent);
        });
        return convertView;
    }
}
