package com.mariosg92.yourclassapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RecoveryActivity extends AppCompatActivity {

    public static final String EXTRA_MAILRECOVER = "com.mariosg92.RecoveryActivity.mailrecover";
    private FirebaseAuth mAuth;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.edt_recoverMail);
    }

    public void recoverPass(View view) {
        try {
            String emailRecover = email.getText().toString();
            mAuth.sendPasswordResetEmail(emailRecover);
            Intent intent = new Intent(this, DoneActivity.class);
            String message = "Se le ha enviado un correo a la dirección " + emailRecover + " para resetear su contraseña.";
            intent.putExtra(EXTRA_MAILRECOVER, message);
            startActivity(intent);
            finish();
        }catch(Exception e){

        }
    }
}