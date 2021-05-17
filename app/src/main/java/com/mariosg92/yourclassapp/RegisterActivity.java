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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {

    public static final String EXTRA_REGISTERDONE = "com.mariosg92.RegisterActivity.registerDone";
    private EditText edt_username;
    private EditText edt_email;
    private EditText edt_password;
    private EditText edt_passwordConfirm;
    private TextView errorText;
    private String[] campos = new String[4];
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        errorText = findViewById(R.id.txt_error);
        edt_username = findViewById(R.id.regUser);
        edt_email = findViewById(R.id.regEmail);
        edt_password = findViewById(R.id.regPass);
        edt_passwordConfirm = findViewById(R.id.regPassConfirm);
        db = FirebaseFirestore.getInstance();

    }

    public void signUp(View view) {
        String email = edt_email.getText().toString();
        String password = edt_password.getText().toString();
        String username = edt_username.getText().toString();
        campos = new String[]{edt_username.getText().toString(), edt_email.getText().toString(),
                edt_password.getText().toString(), edt_passwordConfirm.getText().toString()};
        validation(campos, email, password, username);
    }

    public void validation(String campos[], String email, String password, String username){
        if(campos[0].isEmpty() || campos[1].isEmpty() || campos[2].isEmpty() || campos[3].isEmpty()){
            errorText.setText("Rellene todos los campos.");
        }else if(!campos[2].equals(campos[3])){
            errorText.setText("La contraseña debe coincidir.");
        }else if(!campos[1].matches("(\\S+@\\S+\\.\\S+)")){
            errorText.setText("Introduzca un correo electrónico válido.");
        }else if(!campos[2].matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}$")){
            errorText.setText("La contraseña debe tener un mínimo de 8 caracteres, un número y una mayúscula.");
        }else{
            register(email, password, username);
        }
    }


    public void register(String email, String password, String username){
           mAuth.createUserWithEmailAndPassword(email, password)
                   .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                               if (task.isSuccessful()) {
                                   Log.d("REGISTER", "createUserWithEmail:success");
                                   mAuth.getCurrentUser().sendEmailVerification();
                                   addToFirestore(email, username);
                                   registerDone();
                               } else {
                                   Log.w("REGISTER", "createUserWithEmail:failure", task.getException());
                                   errorText.setText("El correo electrónico ya está en uso.");
                               }
                       }
                   });
    }

    public void addToFirestore(String email, String username) {
        Map<String, Object> user = new HashMap<>();
        user.put("nombre",username);
        user.put("email",email);
        db.collection("docentes").document(mAuth.getCurrentUser().getUid()).set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        Log.i("INSERT-DB","AGREGADO CON EXITO");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.i("ERROR-DB","ERROR AL AÑADIR");
                    }
                });

    }


    public void registerDone(){
        String registerDone = "El registro se ha realizado con éxito. En breves, recibirá un correo para verificar su cuenta";
        Intent intent = new Intent(this, DoneActivity.class);
        intent.putExtra(EXTRA_REGISTERDONE,registerDone);
        startActivity(intent);
        finish();
    }

    public void backToLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}