package com.yamatoapps.bao2booking;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class VehicleAdapter extends ArrayAdapter<Vehicle>  {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public VehicleAdapter(@NonNull Context context, ArrayList<Vehicle> objects) {
        super(context, 0, objects);
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Vehicle item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bao_bao_card, parent, false);
        }
        // Lookup view for data population
        TextView tvModel = (TextView) convertView.findViewById(R.id.tvModel);
        TextView tvCapacity = (TextView) convertView.findViewById(R.id.tvCapacity);
        TextView tvSpeed = (TextView) convertView.findViewById(R.id.tvSpeed);
        TextView tvFeatures = (TextView) convertView.findViewById(R.id.tvFeatures);
        ImageView ivBaoBao = (ImageView) convertView.findViewById(R.id.ivBaoBao);

        tvModel.setText(item.model);
        tvCapacity.setText(item.capacity);
        tvSpeed.setText(item.speed);
        tvFeatures.setText(item.features);
        Picasso.get().load(item.image_url).into(ivBaoBao);

        Button btnEdit, btnDelete;
        btnEdit = convertView.findViewById(R.id.btnEdit);
        btnDelete = convertView.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(view -> {
            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(parent.getContext());
            alertDialogBuilder.setTitle("Delete Vehicle");
            alertDialogBuilder.setMessage("Are you sure you want to delete this vehicle?");
            alertDialogBuilder.setPositiveButton("NO", (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });
            alertDialogBuilder.setNegativeButton("YES", (dialogInterface, i) -> {

                MaterialAlertDialogBuilder deleteDialogBuilder = new MaterialAlertDialogBuilder(parent.getContext());
                deleteDialogBuilder.setTitle("Delete success");
                deleteDialogBuilder.setMessage("Vehicle deleted successfully!");
                deleteDialogBuilder.setPositiveButton("OK", (deleteDialogBuilderDialogInterface,j)->{
                    deleteDialogBuilderDialogInterface.dismiss();
                    Activity context = (Activity) parent.getContext();
                });
                db.collection("baobao").document(item.id).delete().addOnSuccessListener(unused -> {
                    deleteDialogBuilder.create().show();
                    dialogInterface.dismiss();
                });
            });
            alertDialogBuilder.create().show();
        });

        btnEdit.setOnClickListener(view -> {
            Intent intent = new Intent(parent.getContext(), EditVehicle.class);
            intent.putExtra("document_id",item.id);
            parent.getContext().startActivity(intent);
        });
        return convertView;
    }
}
