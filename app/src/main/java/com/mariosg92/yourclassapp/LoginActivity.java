package com.mariosg92.yourclassapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_USERNAME = "com.mariosg92.LoginActivity.username";
    public static final String EXTRA_USERMAIL = "com.mariosg92.LoginActivity.email";

    private EditText edt_username;
    private EditText edt_password;
    private FirebaseAuth mAuth;
    private TextView txt_error;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_username = findViewById(R.id.loginUser);
        edt_password = findViewById(R.id.loginPassword);
        txt_error = findViewById(R.id.txt_loginError);
        mAuth = FirebaseAuth.getInstance();
    }

     @Override
     protected void onStart() {
         super.onStart();
         FirebaseUser currentUser = mAuth.getCurrentUser();
         try{
             goWelcome(currentUser);
         }catch(NullPointerException e){
         }
     }



    public void signUp(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        String user = edt_username.getText().toString();
        String password = edt_password.getText().toString();
        if(!user.isEmpty() && !password.isEmpty()) {
            mAuth.signInWithEmailAndPassword(user, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("SIGNIN", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                goWelcome(user);
                            } else {
                                Log.w("SIGNIN", "signInWithEmail:failure", task.getException());
                                txt_error.setText("Por favor, introduzca unos datos válidos");
                            }
                        }
                    });
        }else{
            txt_error.setText("Introduzca un correo y contraseña");
        }
    }

    public void goRecoverPass(View view) {
        Intent intent = new Intent(this, RecoveryActivity.class);
        startActivity(intent);
    }

    public void goWelcome(FirebaseUser user){
        if(user.isEmailVerified()) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }else{
            mAuth.signOut();
            txt_error.setText("Verifique su correo para poder conectar.");
        }
    }
}