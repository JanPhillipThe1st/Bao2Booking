package com.yamatoapps.bao2booking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditVehicle extends AppCompatActivity {
    Uri fileUri;
    Button btnSave;
    ImageView ivBaoBao;
    TextView tvModel,tvSpeed,tvDisplacement,tvCapacity,tvFeatures;
    String document_id = "";
    String image_url = "";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
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
        setContentView(R.layout.activity_edit_vehicle);
        tvModel = findViewById(R.id.tvModel);
        tvSpeed = findViewById(R.id.tvSpeed);
        tvDisplacement = findViewById(R.id.tvDisplacement);
        tvCapacity = findViewById(R.id.tvCapacity);
        tvFeatures = findViewById(R.id.tvFeatures);
        ivBaoBao = findViewById(R.id.ivBaoBao);
         progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.setMessage("Posting your Vehicle...");
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

        btnSave.setOnClickListener(view -> {

            progressDialog.show();
            Map<String, Object> listing = new HashMap<>();
            listing.put("model", tvModel.getText().toString());
            listing.put("speed", tvSpeed.getText().toString());
            listing.put("displacement", tvDisplacement.getText().toString());
            listing.put("capacity", tvCapacity.getText().toString());
            listing.put("features", tvFeatures.getText().toString());
            listing.put("image_url",  image_url);
            db.collection("baobao").document(document_id).update(listing).addOnSuccessListener(unused -> {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Congratulations! The vehicle information is updated!", Toast.LENGTH_SHORT).show();
                finish();
            });
        });

        Intent imageIntent = new Intent();
        ivBaoBao.setOnClickListener(view -> {
            imageIntent.setType("image/*");
            imageIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(imageIntent,"Pick image to upload"),22);

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