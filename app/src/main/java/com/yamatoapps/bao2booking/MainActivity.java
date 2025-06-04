package com.yamatoapps.bao2booking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    Button btnLogin,btnRegister;
    TextView tvUsername,tvPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         btnLogin  = findViewById(R.id.btnLogin);
         tvUsername  = findViewById(R.id.tvUsername);
         tvPassword  = findViewById(R.id.tvPassword);
        btnRegister  = findViewById(R.id.btnRegister);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Logging in...");
        progressDialog.setMessage("Please wait");
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this);

        btnRegister.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, Register.class));
        });

        btnLogin.setOnClickListener(view -> {
            progressDialog.show();
            if (tvUsername.getText().toString() == "admin"){
                //Admin
                //Proceed to main screen
                alertDialogBuilder.setMessage("Logged in as Admin.\nPress 'OK' to proceed");
                alertDialogBuilder.setTitle("Login Success");
                alertDialogBuilder.setPositiveButton("OK", (dialogInterface, i) -> {
                    startActivity(new Intent(MainActivity.this, Admin.class));

                    progressDialog.dismiss();
                });
                alertDialogBuilder.create().show();
            }
            else{
                if (tvUsername.getText().toString() == "customer"){
                    //Proceed to main screen
                    alertDialogBuilder.setMessage("Logged in as Customer.\nPress 'OK' to proceed");
                    alertDialogBuilder.setTitle("Login Success");
                    alertDialogBuilder.setPositiveButton("OK", (dialogInterface, i) -> {
                        startActivity(new Intent(MainActivity.this, Customer.class));
                        progressDialog.dismiss();
                    });
                    alertDialogBuilder.create().show();
                }
                else{
                    alertDialogBuilder.setMessage("No account found. Please try again.");
                    alertDialogBuilder.setTitle("Login Failed");
                    alertDialogBuilder.setPositiveButton("Try again", (dialogInterface, i) -> {
                        progressDialog.dismiss();
                    });
                    alertDialogBuilder.create().show();
                }
            }
        });//btnLogin
        btnRegister.setOnClickListener(view -> {
            startActivity(new Intent(this,Register.class));
        });
    }
}