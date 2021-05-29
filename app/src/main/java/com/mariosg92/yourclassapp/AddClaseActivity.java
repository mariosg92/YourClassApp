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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mariosg92.yourclassapp.clases.Clases;
import com.mariosg92.yourclassapp.clases.ClasesAdapter;
import com.mariosg92.yourclassapp.ui.clases.ClasesFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

import static com.mariosg92.yourclassapp.ui.clases.ClasesFragment.EXTRA_CLASES_ARRAY;

public class AddClaseActivity extends AppCompatActivity {

    public static final String EXTRA_ADDCLASE_LIST = "com.mariosg92.AddClaseActivity.clasesList";
    private Button btn_add;
    private Button btn_back;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText edt_clase;
    private EditText edt_curso;
    private String nombre_clase;
    private String curso;
    private FirebaseUser currentUser;
    private ArrayList<Clases> clasesList;
    private TextView textError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clase);

        btn_add = findViewById(R.id.button);
        btn_back = findViewById(R.id.bt_volver2);
        edt_clase = findViewById(R.id.edt_clasenombre);
        edt_curso = findViewById(R.id.edt_curso);
        textError = findViewById(R.id.txt_errorAddClase);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        Intent intent = getIntent();
        clasesList = new ArrayList<>();
        clasesList = (ArrayList<Clases>) intent.getSerializableExtra(EXTRA_CLASES_ARRAY);


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation()) {
                    nombre_clase = String.valueOf(edt_clase.getText());
                    curso = String.valueOf(edt_curso.getText());
                    Clases c = new Clases(nombre_clase, curso);
                    addToFirestore(c);
                    finish();
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public boolean validation(){
        if((edt_curso.length() == 0) || (edt_clase.length() == 0)){
            textError.setText("Complete todos los campos");
            return false;
        }else if(edt_curso.getText().length() <= 2){
            textError.setText("El nombre del curso debe contener almenos 3 caracteres");
            return false;
        }else{
            return true;
        }
    }

    public void addToFirestore(Clases c){
        //String claseId = nombre_clase.concat(curso.substring(0,3).concat(String.valueOf((int)Math.floor(Math.random()*(1000-1))+1)));
        db.collection("docentes").document(currentUser.getUid())
                .collection("clases").document(c.getClaseId()).set(c).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(AddClaseActivity.this,"Hubo un problema en la base de datos",Toast.LENGTH_SHORT).show();
            }
        });
        clasesList.add(c);
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ADDCLASE_LIST,clasesList);
        setResult(RESULT_OK, intent);
    }
}