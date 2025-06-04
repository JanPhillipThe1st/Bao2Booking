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

public class VehicleBookingAdapter extends ArrayAdapter<VehicleBooking>  {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public VehicleBookingAdapter(@NonNull Context context, ArrayList<VehicleBooking> objects) {
        super(context, 0, objects);
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        VehicleBooking item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.booking_item, parent, false);
        }
        // Lookup view for data population
        TextView tvModel = (TextView) convertView.findViewById(R.id.tvModel);
        TextView tvCapacity = (TextView) convertView.findViewById(R.id.tvCapacity);
        TextView tvSpeed = (TextView) convertView.findViewById(R.id.tvSpeed);
        TextView tvFeatures = (TextView) convertView.findViewById(R.id.tvFeatures);
        TextView tvDateBooked = (TextView) convertView.findViewById(R.id.tvDateBooked);
        ImageView ivBaoBao = (ImageView) convertView.findViewById(R.id.ivBaoBao);

        tvModel.setText(item.model);
        tvCapacity.setText(item.capacity);
        tvSpeed.setText(item.speed);
        tvFeatures.setText(item.features);
        tvDateBooked.setText(item.date_booked.toString());
        Picasso.get().load(item.image_url).into(ivBaoBao);


        return convertView;
    }
}
