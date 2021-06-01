package com.mariosg92.yourclassapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mariosg92.yourclassapp.clases.Alumno;
import com.mariosg92.yourclassapp.clases.Clases;
import com.mariosg92.yourclassapp.utils.TinyDB;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

import static com.mariosg92.yourclassapp.ui.alumnos.AlumnosFragment.EXTRA_ALUMNOS_ARRAY;
import static com.mariosg92.yourclassapp.ui.alumnos.AlumnosFragment.EXTRA_CLASE_ALUMNOS;

public class AddAlumnoActivity extends AppCompatActivity {

    public static final String EXTRA_ADDALUMNO_LIST = "com.mariosg92.AddAlumnoActivity.list";
    private Button bt_AddAlumno;
    private Button bt_back;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private EditText edt_nombreAlumno;
    private EditText edt_primerApellido;
    private EditText edt_segundoApellido;
    private ArrayList<Alumno> alumnosList;
    private Clases claseAlumno;
    private String nombreAlumno;
    private String apellido1Alumno;
    private String apellido2Alumno;
    private TextView edt_errorText;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alumno);
        bt_AddAlumno = findViewById(R.id.bt_AddAlumno2);
        bt_back = findViewById(R.id.bt_volver);
        edt_nombreAlumno = findViewById(R.id.edt_NombreAlumno);
        edt_primerApellido = findViewById(R.id.edt_PrimerApellidoAlumno);
        edt_segundoApellido = findViewById(R.id.edt_SegundoApellidoAlumno);
        edt_errorText = findViewById(R.id.textErrorA);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReferenceFromUrl("gs://yourclassapp.appspot.com");



        Intent intent = getIntent();
        alumnosList = new ArrayList<>();
        alumnosList = (ArrayList<Alumno>) intent.getSerializableExtra(EXTRA_ALUMNOS_ARRAY);
        claseAlumno = (Clases) intent.getSerializableExtra(EXTRA_CLASE_ALUMNOS);


        bt_AddAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    edt_errorText.setText("");
                    nombreAlumno = edt_nombreAlumno.getText().toString();
                    apellido1Alumno = edt_primerApellido.getText().toString();
                    apellido2Alumno = edt_segundoApellido.getText().toString();
                    String iniciales = nombreAlumno.substring(0, 1).concat(apellido1Alumno.substring(0, 1)).concat(apellido2Alumno.substring(0, 1));
                    String alumnoId = iniciales.concat(currentUser.getUid().substring(0, 3)).concat(String.valueOf((int) Math.floor(Math.random() * (100 - 1)) + 1)).toUpperCase();
                    String[] avatarList = {"1_red.png","3_green.png","4_pink.png","6_yellow.png","7_black.png","11_cyan.png"};
                    Random r = new Random();
                    String avatar = avatarList[r.nextInt(avatarList.length)];
                    storageReference.child("avatar/"+avatar).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Uri> task) {
                            String avatarURL = task.getResult().toString();
                            Alumno a = new Alumno(nombreAlumno, apellido1Alumno, apellido2Alumno, alumnoId, claseAlumno, avatarURL);
                            addAlumno(a);
                        }
                    });
                }
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public boolean validation() {
        if ((edt_nombreAlumno.length() == 0) || (edt_primerApellido.length() == 0) || (edt_segundoApellido.length() == 0)) {
            edt_errorText.setText("Complete todos los campos");
            return false;
        } else {
            return true;
        }
    }

    public void addAlumno(Alumno a) {
        db.collection("docentes").document(currentUser.getUid())
                .collection("clases").document(claseAlumno.getClaseId())
                .collection("alumnos").document(a.getCodigo()).set(a)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            alumnosList.add(a);
                            db.collection("alumnos").document(a.getCodigo()).set(a);
                            Intent intent = new Intent();
                            intent.putExtra(EXTRA_ADDALUMNO_LIST, alumnosList);
                            setResult(RESULT_OK, intent);
                            Toast.makeText(AddAlumnoActivity.this, "ALUMNO AÑADIDO", Toast.LENGTH_LONG).show();
                            edt_nombreAlumno.setText("");
                            edt_primerApellido.setText("");
                            edt_segundoApellido.setText("");

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(AddAlumnoActivity.this, "Hubo un problema en la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}