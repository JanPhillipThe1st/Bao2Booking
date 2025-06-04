package com.yamatoapps.bao2booking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    Button btnSignup;
    Toolbar toolbar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView tvUsername, tvPassword, tvFullname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnSignup = findViewById(R.id.btnSignup);
        toolbar = findViewById(R.id.tb);
        tvFullname = findViewById(R.id.tvFullName);
        tvUsername = findViewById(R.id.tvUsername);
        tvPassword = findViewById(R.id.tvPassword);
        Map<String,Object> user_data = new HashMap<String,Object>();


        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(Register.this);
        btnSignup.setOnClickListener( view -> {
                    alertDialogBuilder.setTitle("Signing up");
                    alertDialogBuilder.setMessage("Are you sure you want to proceed?");
                    alertDialogBuilder.setNegativeButton("NO",(dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    });
                    alertDialogBuilder.setPositiveButton("YES",(dialogInterface, i) -> {
                        ProgressDialog progressDialog  = new ProgressDialog(Register.this);
                        progressDialog.setTitle("Saving");
                        progressDialog.setMessage("Saving your data. Please wait.");
                        progressDialog.show();
                        user_data.put("username",tvUsername.getText().toString());
                        user_data.put("password",tvPassword.getText().toString());
                        user_data.put("full_name",tvFullname.getText().toString());
                        user_data.put("type","customer");

                        MaterialAlertDialogBuilder signupAlertDialogBuilder = new MaterialAlertDialogBuilder(Register.this);
                        signupAlertDialogBuilder.setTitle("Registration Success");
                        signupAlertDialogBuilder.setMessage("Your registration has been successfully completed!");
                        signupAlertDialogBuilder.setPositiveButton("OK", (signupDialogInterface,o) ->{
                            signupDialogInterface.dismiss();
                            finish();
                        });
                        db.collection("bao_bao_users").add(user_data).addOnSuccessListener(documentReference -> {
                            signupAlertDialogBuilder.create().show();
                        });

                    });
                    alertDialogBuilder.create().show();
                }
        );
        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });
    }
}