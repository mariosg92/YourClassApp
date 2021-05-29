package com.mariosg92.yourclassapp.clases;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mariosg92.yourclassapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AlumnoAdapter extends RecyclerView.Adapter<AlumnoViewHolder> {

    private Context c;
    private List<Alumno> listaAlumnos;
    private LayoutInflater mInflater;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    public AlumnoAdapter(Context c, List<Alumno> listaAlumnos) {
        this.c = c;
        this.listaAlumnos = listaAlumnos;
        mInflater = LayoutInflater.from(c);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    public Context getC() {
        return c;
    }

    public void setC(Context c) {
        this.c = c;
    }

    public List<Alumno> getListaAlumnos() {
        return listaAlumnos;
    }

    public void setListaAlumnos(List<Alumno> listaAlumnos) {
        this.listaAlumnos = listaAlumnos;
        if(this.listaAlumnos == null){
            Log.i("alumnos","List is null");
        }else{
            for(Alumno a: listaAlumnos){
                Log.i("alumnos","alumno: "+a.getNombre()+"\n"+a.getCodigo()+"\n"+a.getClase().getClaseId());
            }
        }
        notifyDataSetChanged();
    }

    public LayoutInflater getmInflater() {
        return mInflater;
    }

    public void setmInflater(LayoutInflater mInflater) {
        this.mInflater = mInflater;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public FirebaseFirestore getDb() {
        return db;
    }

    public void setDb(FirebaseFirestore db) {
        this.db = db;
    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(FirebaseUser currentUser) {
        this.currentUser = currentUser;
    }

    @NonNull
    @NotNull
    @Override
    public AlumnoViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.item_rv_alumno, parent, false);
        return new AlumnoViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AlumnoViewHolder holder, int position) {
        if(listaAlumnos != null){
            Alumno alumnoActual = listaAlumnos.get(position);
            holder.txt_nombreA.setText(alumnoActual.getNombre());
            holder.txt_apellidosA.setText(alumnoActual.getApellido1());
            holder.txt_apellidosA2.setText(alumnoActual.getApellido2());
            holder.txt_points.setText(String.valueOf(alumnoActual.getPuntos()));
            holder.setOnClickerListeners();
        }

    }

    @Override
    public int getItemCount() {
        return listaAlumnos.size();
    }

    public void Add(Alumno a1) {
        this.listaAlumnos.add(a1);
        notifyItemInserted(listaAlumnos.size());
    }
}
