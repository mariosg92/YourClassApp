package com.mariosg92.yourclassapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_ROLE = "com.mariosg92.MainActivity.role";
    private String role = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.Theme_YourClassApp);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void logAsDocente(View view) {
        role = "docente";
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(EXTRA_ROLE,role);
        startActivity(intent);
    }
}