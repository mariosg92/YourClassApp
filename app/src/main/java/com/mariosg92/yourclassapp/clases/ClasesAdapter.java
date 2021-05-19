package com.mariosg92.yourclassapp.clases;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mariosg92.yourclassapp.R;
import com.mariosg92.yourclassapp.ui.clases.ClasesFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ClasesAdapter extends RecyclerView.Adapter<ClasesViewHolder>{

    private Context c;
    private List<Clases> listaClases;
    private LayoutInflater mInflater;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    public ClasesAdapter(Context c, List<Clases> listaClases){
        this.c = c;
        this.listaClases = listaClases;
        mInflater = LayoutInflater.from(c);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
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
    public ClasesViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.item_rv_misclases, parent, false);
        return new ClasesViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ClasesViewHolder holder, int position) {
        if(listaClases != null){
            Clases claseActual = listaClases.get(position);
            holder.txt_rv_misclases.setText(claseActual.getCurso()+" "+claseActual.getNombre());
            holder.setOnClickListeners();
        }
    }



    @Override
    public int getItemCount() {
        return listaClases.size();
    }

    public Context getC() {
        return c;
    }

    public void setC(Context c) {
        this.c = c;
    }

    public List<Clases> getListaClases() {
        return listaClases;
    }

    public void setListaClases(List<Clases> listaClases) {
        this.listaClases = listaClases;
        if(this.listaClases == null){
            Log.i("clases","La lista es nula");
        }else{
            for(Clases c:listaClases){
                Log.i("clases","clases: "+c.getNombre());
            }
        }
        notifyDataSetChanged();
    }

    public void Add(Clases c){
        this.listaClases.add(c);
        notifyItemInserted(listaClases.size());
    }

}
