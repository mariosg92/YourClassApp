package com.mariosg92.yourclassapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_USERNAME = "com.mariosg92.LoginActivity.username";
    public static final String EXTRA_USERMAIL = "com.mariosg92.LoginActivity.email";

    private EditText edt_username;
    private EditText edt_password;
    private Button bt_back;
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
        bt_back = findViewById(R.id.bt_back_inicio);

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(LoginActivity.this, "Hubo un error en la base de datos", Toast.LENGTH_SHORT).show();
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
            finish();
        }else{
            mAuth.signOut();
            txt_error.setText("Verifique su correo para poder conectar.");
        }
    }
}