package com.mariosg92.yourclassapp.clases;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mariosg92.yourclassapp.ClaseActivity;
import com.mariosg92.yourclassapp.R;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

public class ClasesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String EXTRA_CLASE = "com.mariosg92.ViewHolder.clases";
    public TextView txt_rv_misclases;
    public ImageView bt_borrar;
    final ClasesAdapter clasesAdapter;

    public ClasesViewHolder(@NonNull @NotNull View itemView, ClasesAdapter clasesAdapter) {
        super(itemView);
        txt_rv_misclases = itemView.findViewById(R.id.txt_clase);
        bt_borrar = itemView.findViewById(R.id.bt_borrar);
        this.clasesAdapter = clasesAdapter;
    }


    @Override
    public void onClick(View v) {
        int mPosition = getAdapterPosition();
        List<Clases> clases = this.clasesAdapter.getListaClases();
        Clases clase = clases.get(mPosition);
        switch (v.getId()) {
            case R.id.bt_borrar:
                DocumentReference dRef = clasesAdapter.getDb().collection("docentes").document(clasesAdapter.getCurrentUser().getUid());
                Query query = dRef.collection("clases").whereEqualTo("nombre", clase.getNombre()).whereEqualTo("curso", clase.getCurso());
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                AlertDialog.Builder alerta = new AlertDialog.Builder(clasesAdapter.getC());
                                alerta.setTitle("¿Desea borrar esta clase?");
                                alerta.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dRef.collection("clases").document(document.getId()).delete();
                                        clases.remove(mPosition);
                                        clasesAdapter.notifyItemRemoved(mPosition);
                                        Log.i("BORRADO", "BORRADO CORRECTAMENTE");
                                    }
                                });
                                alerta.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                alerta.show();
                            }

                        }
                    }
                });
                clasesAdapter.notifyDataSetChanged();
                break;
            case R.id.txt_clase:
                clasesAdapter.notifyDataSetChanged();
                Bundle bundle = new Bundle();
                bundle.putString("EXTRA_CLASE_NOMBRE",clase.getNombre());
                bundle.putString("EXTRA_CLASE_CURSO",clase.getCurso());
                Navigation.findNavController(v).navigate(R.id.nav_alumnos,bundle);
                break;
        }
    }

    public void setOnClickListeners() {
        bt_borrar.setOnClickListener(this);
        txt_rv_misclases.setOnClickListener(this);
    }


}