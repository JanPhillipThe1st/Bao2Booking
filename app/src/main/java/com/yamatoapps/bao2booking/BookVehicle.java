package com.yamatoapps.bao2booking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BookVehicle extends AppCompatActivity {
    Uri fileUri;
    Button btnSave,btnBookingDate;
    ImageView ivBaoBao;
    TextView tvModel,tvSpeed,tvDisplacement,tvCapacity,tvFeatures;
    String document_id = "";
    String image_url = "";
    final Calendar c = Calendar.getInstance();
    ProgressDialog progressDialog;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        ImageView iv = findViewById(R.id.ivBaoBao);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 22 && resultCode == RESULT_OK && data != null && data != null) {
            fileUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),fileUri);
                iv.setImageBitmap(bitmap);
                uploadImage();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_vehicle);
        tvModel = findViewById(R.id.tvModel);
        tvSpeed = findViewById(R.id.tvSpeed);
        tvDisplacement = findViewById(R.id.tvDisplacement);
        tvCapacity = findViewById(R.id.tvCapacity);
        tvFeatures = findViewById(R.id.tvFeatures);
        btnBookingDate = findViewById(R.id.btnBookingDate);
        ivBaoBao = findViewById(R.id.ivBaoBao);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Booking vehicle...");
        progressDialog.setMessage(" Adding your booking...");
        document_id = getIntent().getStringExtra("document_id");

        db.collection("baobao").document(document_id).get().addOnSuccessListener(documentSnapshot -> {
            tvModel.setText(documentSnapshot.getString("model"));
            tvSpeed.setText(documentSnapshot.getString("speed"));
            tvDisplacement.setText(documentSnapshot.getString("displacement"));
            tvCapacity.setText(documentSnapshot.getString("capacity"));
            tvFeatures.setText(documentSnapshot.getString("features"));
            Picasso.get().load(documentSnapshot.getString("image_url")).into(ivBaoBao);
            image_url = documentSnapshot.getString("image_url");



        });

        btnSave = findViewById(R.id.btnSave);
        btnBookingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.

                // on below line we are getting
                // our day, month and year.
                int month1,date1,year1;
                month1 = c.get(Calendar.MONTH);
                date1 = c.get(Calendar.DAY_OF_MONTH);
                year1 = c.get(Calendar.YEAR);
                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        BookVehicle.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                c.set(Calendar.DATE,dayOfMonth);
                                c.set(Calendar.MONTH,monthOfYear);
                                c.set(Calendar.YEAR,year);
                                TimePickerDialog timePickerDialog = new TimePickerDialog(
                                        // on below line we are passing context.
                                        BookVehicle.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                                c.set(Calendar.HOUR_OF_DAY,i);
                                                c.set(Calendar.MINUTE,i1);
                                                c.set(Calendar.SECOND,0);
                                                c.set(Calendar.MILLISECOND,0);
                                                btnBookingDate.setText(c.getTime().toLocaleString());
                                            }
                                        },0,0,true);
                                // at last we are calling show to
                                // display our date picker dialog.
                                timePickerDialog.show();
                            }
                        },year1,month1,date1);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });
        btnSave.setOnClickListener(view -> {

            progressDialog.show();
            Map<String, Object> listing = new HashMap<>();
            listing.put("model", tvModel.getText().toString());
            listing.put("speed", tvSpeed.getText().toString());
            listing.put("displacement", tvDisplacement.getText().toString());
            listing.put("capacity", tvCapacity.getText().toString());
            listing.put("features", tvFeatures.getText().toString());
            listing.put("image_url",  image_url);
            listing.put("booking_date",  c.getTime());
            db.collection("baobao_bookings").add(listing).addOnSuccessListener(unused -> {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Congratulations! This vehicle is now booked!", Toast.LENGTH_SHORT).show();
                finish();
            });
        });

        Intent imageIntent = new Intent();
        ivBaoBao.setOnClickListener(view -> {
        });
    }
    public  void uploadImage(){
        if (fileUri != null){

            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child(UUID.randomUUID().toString());
            UploadTask uploadTask = (UploadTask) storageReference.putFile(fileUri).addOnSuccessListener(taskSnapshot -> {

            }).addOnFailureListener(listener->{
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Fail to Upload Image..", Toast.LENGTH_SHORT)
                        .show();
            });
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        image_url = task.getResult().toString();
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });

        }
    }
}