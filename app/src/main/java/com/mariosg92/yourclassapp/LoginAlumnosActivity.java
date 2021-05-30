package com.mariosg92.yourclassapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mariosg92.yourclassapp.clases.Alumno;
import com.mariosg92.yourclassapp.clases.Clases;

import org.jetbrains.annotations.NotNull;

public class LoginAlumnosActivity extends AppCompatActivity {

    private static final String EXTRA_ALUMNO = "com.mariosg92.LoginAlumnoActivity.Alumno";
    private Button bt_login;
    private Button bt_back;
    private EditText edt_codigo;
    private TextView errorText;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_alumnos);
        bt_login = findViewById(R.id.bt_loginAlumno);
        bt_back = findViewById(R.id.bt_backMain);
        edt_codigo = findViewById(R.id.edt_codigoAlumno);
        errorText = findViewById(R.id.txt_ErrorLoginA);
        db = FirebaseFirestore.getInstance();

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo = edt_codigo.getText().toString();
                validationCode(codigo);
            }
        });
    }

    public void validationCode(String code) {
        db.collection("alumnos").whereEqualTo("codigo", code)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Alumno a = new Alumno();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        a.setNombre((String) document.get("nombre"));
                        a.setApellido1((String) document.get("apellido1"));
                        a.setApellido2((String) document.get("apellido2"));
                        a.setPuntos((Long) document.get("puntos"));
                        a.setClase(new Clases((String) document.get("clase.nombre"),(String) document.get("clase.curso"),(String) document.get("clase.claseId")));
                        a.setCodigo((String) document.get("codigo"));
                    }
                    try {
                        if (a.getCodigo().equals(code)) {
                            Intent intent = new Intent(LoginAlumnosActivity.this, AlumnoTabActivity.class);
                            intent.putExtra(EXTRA_ALUMNO, a);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        errorText.setText("Introduzca un código válido");
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(LoginAlumnosActivity.this, "Hubo un problema en la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
        ;
    }
}