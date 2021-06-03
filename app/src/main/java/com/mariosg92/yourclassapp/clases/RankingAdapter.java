package com.mariosg92.yourclassapp.clases;

import android.content.Context;
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

public class RankingAdapter extends RecyclerView.Adapter<RankingViewHolder> {
    private Context c;
    private List<Alumno> listaAlumnos;
    private LayoutInflater mInflater;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    public RankingAdapter(Context c, List<Alumno> listaAlumnos) {
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
    @Override
    public RankingViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.item_ranking, parent, false);
        return new RankingViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RankingViewHolder holder, int position) {
        if(listaAlumnos != null){
            holder.txt_nombre.setText(listaAlumnos.get(position).getNombre()+" "
                    +listaAlumnos.get(position).getApellido1()+" "+listaAlumnos.get(position).getApellido2());
            holder.txt_points.setText(String.valueOf(listaAlumnos.get(position).getPuntos()));
        }
    }


    @Override
    public int getItemCount() {
        return listaAlumnos.size();
    }
}
