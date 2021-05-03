package com.mariosg92.yourclassapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.mariosg92.yourclassapp.RecoveryActivity.EXTRA_MAILRECOVER;
import static com.mariosg92.yourclassapp.RegisterActivity.EXTRA_REGISTERDONE;

public class DoneActivity extends AppCompatActivity {

    private TextView txt_message;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        txt_message = findViewById(R.id.txt_message);
        Intent intent = getIntent();
        try{
            String registerMessage = intent.getStringExtra(EXTRA_REGISTERDONE);
            txt_message.setText(registerMessage);
        }catch(Exception e){
            String registerMessage = intent.getStringExtra(EXTRA_MAILRECOVER);
            txt_message.setText(registerMessage);
        }
/*
        if(!registerMessage.isEmpty()){
            txt_message.setText(registerMessage);
        }
        */
        mAuth = FirebaseAuth.getInstance();
    }

    public void goLogin(View view) {
        mAuth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}