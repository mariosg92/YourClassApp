package com.mariosg92.yourclassapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mariosg92.yourclassapp.clases.Alumno;
import com.mariosg92.yourclassapp.ui.alumnos.AlumnosFragment;
import com.mariosg92.yourclassapp.utils.TinyDB;

import org.jetbrains.annotations.NotNull;

import static com.mariosg92.yourclassapp.clases.AlumnoViewHolder.EXTRA_ALUMNOVH_ALUMNO;
import static com.mariosg92.yourclassapp.clases.AlumnoViewHolder.EXTRA_ALUMNOVH_POSITION;

public class AlumnoDetalleActivity extends AppCompatActivity {

    private TextView txt_nombre;
    private TextView txt_apellidos;
    private TextView txt_puntos;
    private TextView txt_codigo;
    private Button btn_back;
    private ImageButton btn_borrar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private Alumno alumno;
    private boolean isDeleted = false;
    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno_detalle);


        txt_apellidos = findViewById(R.id.txt_apellidosDetalle);
        txt_nombre = findViewById(R.id.txt_nombreDetalle);
        txt_puntos = findViewById(R.id.txt_puntosDetalle);
        txt_codigo = findViewById(R.id.txt_codigoDetalle);

        Intent intent = getIntent();
        alumno = (Alumno) intent.getSerializableExtra(EXTRA_ALUMNOVH_ALUMNO);
        position = intent.getIntExtra(EXTRA_ALUMNOVH_POSITION,0);

        txt_nombre.setText(alumno.getNombre());
        txt_nombre.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        txt_apellidos.setText(alumno.getApellido1() + " " + alumno.getApellido2());
        txt_apellidos.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        txt_puntos.setText(String.valueOf(alumno.getPuntos()));
        txt_codigo.setText(alumno.getCodigo());

        btn_back = findViewById(R.id.bt_volverDetalle);
        btn_borrar = findViewById(R.id.bt_borrarAlumno);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TinyDB tinyDB = new TinyDB(AlumnoDetalleActivity.this);
                tinyDB.putBoolean("detalleActivity",isDeleted);
                tinyDB.putInt("position",position);
                finish();
            }
        });
        btn_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(AlumnoDetalleActivity.this);
                alerta.setTitle("¿Desea borrar esta entrada?");
                alerta.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("docentes").document(currentUser.getUid()).collection("clases").document(alumno.getClase().getClaseId())
                                .collection("alumnos").document(alumno.getCodigo()).delete()
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        Toast.makeText(AlumnoDetalleActivity.this, "Hubo un error en la base de datos", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        db.collection("alumnos").document(alumno.getCodigo()).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(AlumnoDetalleActivity.this, "EL ALUMNO HA SIDO ELIMINADO", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(AlumnoDetalleActivity.this, "Hubo un error en la base de datos", Toast.LENGTH_SHORT).show();
                            }
                        });
                        isDeleted = true;
                    }
                });
                alerta.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alerta.show();
            }
        });
    }



}