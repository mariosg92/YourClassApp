package com.mariosg92.yourclassapp.clases;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Clases implements Serializable {

    private String nombre;
    private String curso;
    private String claseId;

    public Clases(String nombre, String curso) {
        this.nombre = nombre;
        this.curso = curso;
        if(claseId == null){
            this.claseId = generateClaseId();
            System.out.println("SE GENERA CLASE ID "+this.claseId);
        }

    }

    public Clases() {
    }

    public Clases(String nombre, String curso, String claseId) {
        this.nombre = nombre;
        this.curso = curso;
        this.claseId = claseId;
    }

    public String generateClaseId(){
        return nombre.concat(curso.substring(0,3).concat(String.valueOf((int)Math.floor(Math.random()*(1000-1))+1)));
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getClaseId() {
        return claseId;
    }

    public void setClaseId(String claseId) {
        this.claseId = claseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clases clases = (Clases) o;
        return Objects.equals(nombre, clases.nombre) &&
                Objects.equals(curso, clases.curso);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, curso);
    }

    @Override
    public String toString() {
        return curso+" "+nombre;
    }

    public static ArrayList<Clases> getClases(){
        ArrayList<Clases> clases = new ArrayList<>();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("docentes").document(currentUser.getUid()).collection("clases").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            clases.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nombre = document.getString("nombre");
                                String curso = document.getString("curso");
                                String claseId = document.getString("claseId");
                                Clases c1 = new Clases(nombre, curso, claseId);
                                clases.add(c1);
                            }
                        }
                    }
                });
        return clases;
    }
}
