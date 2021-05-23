package com.mariosg92.yourclassapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mariosg92.yourclassapp.clases.Alumno;
import com.mariosg92.yourclassapp.ui.alumnos.AlumnosFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.mariosg92.yourclassapp.ui.alumnos.AlumnosFragment.EXTRA_ALUMNOS_ARRAY;
import static com.mariosg92.yourclassapp.ui.alumnos.AlumnosFragment.EXTRA_CLASE_ALUMNOS;
import static com.mariosg92.yourclassapp.ui.alumnos.AlumnosFragment.EXTRA_CURSO_ALUMNOS;

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
    private String claseAlumno;
    private String cursoAlumno;
    private String claseId;
    private String nombreAlumno;
    private String apellido1Alumno;
    private String apellido2Alumno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alumno);
        bt_AddAlumno = findViewById(R.id.bt_AddAlumno2);
        bt_back = findViewById(R.id.bt_volver);
        edt_nombreAlumno = findViewById(R.id.edt_NombreAlumno);
        edt_primerApellido = findViewById(R.id.edt_PrimerApellidoAlumno);
        edt_segundoApellido = findViewById(R.id.edt_SegundoApellidoAlumno);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        Intent intent = getIntent();
        alumnosList = new ArrayList<>();
        alumnosList = (ArrayList<Alumno>) intent.getSerializableExtra(EXTRA_ALUMNOS_ARRAY);
        claseAlumno = intent.getStringExtra(EXTRA_CLASE_ALUMNOS);
        cursoAlumno = intent.getStringExtra(EXTRA_CURSO_ALUMNOS);



        bt_AddAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombreAlumno = edt_nombreAlumno.getText().toString();
                apellido1Alumno = edt_primerApellido.getText().toString();
                apellido2Alumno = edt_segundoApellido.getText().toString();
                String iniciales = nombreAlumno.substring(0,1).concat(apellido1Alumno.substring(0,1)).concat(apellido2Alumno.substring(0,1));
                String alumnoId = iniciales.concat(currentUser.getUid().substring(0,3)).concat(String.valueOf((int)Math.floor(Math.random()*(100-1))+1)).toUpperCase();
                Alumno a = new Alumno(nombreAlumno,apellido1Alumno,apellido2Alumno,alumnoId);
                addAlumno(a);
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void addAlumno(Alumno a){
        Task<QuerySnapshot> querySnapshotTask = db.collection("docentes").document(currentUser.getUid())
                .collection("clases").whereEqualTo("nombre",claseAlumno)
                .whereEqualTo("curso",cursoAlumno).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            claseId = task.getResult().getDocuments().iterator().next().getId();
                            db.collection("docentes").document(currentUser.getUid()).collection("clases")
                                    .document(claseId).collection("alumnos").document(a.getCodigo()).set(a);
                            db.collection("alumnos").document(a.getCodigo()).set(a);
                            alumnosList.add(a);
                            Intent intent = new Intent();
                            intent.putExtra(EXTRA_ADDALUMNO_LIST,alumnosList);
                            setResult(RESULT_OK, intent);
                        }
                    }
                });

    }
}