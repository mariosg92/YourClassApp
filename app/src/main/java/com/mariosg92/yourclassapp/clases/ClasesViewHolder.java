package com.mariosg92.yourclassapp.clases;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mariosg92.yourclassapp.R;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;

import java.io.Serializable;
import java.util.List;

import static com.mariosg92.yourclassapp.ui.clases.ClasesFragment.EXTRA_CLASE_CURSO;

public class ClasesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public static final String EXTRA_CLASEVH_NOMBRE = "com.mariosg92.ClasesVH.nombre";
    public static final String EXTRA_CLASEVH_CURSO = "com.mariosg92.ClasesVH.curso";
    public static final String EXTRA_CLASEVH_CLASE = "com.mariosg92.ClasesVH.clase";
    public TextView txt_rv_misclases;
    public ImageView bt_borrar;
    public Clases clase;
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
        clase = clases.get(mPosition);
        switch (v.getId()) {
            case R.id.bt_borrar:
                DocumentReference dRef = clasesAdapter.getDb().collection("docentes").document(clasesAdapter.getCurrentUser().getUid());
                dRef.collection("clases").document(clase.getClaseId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            AlertDialog.Builder alerta = new AlertDialog.Builder(clasesAdapter.getC());
                            alerta.setTitle("¿Desea borrar esta clase?");
                            alerta.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dRef.collection("clases").document(clase.getClaseId()).delete();
                                    //Borrado de los alumnos que pertenezcan a la clase borrada de la colección de alumnos
                                    clasesAdapter.getDb().collection("alumnos").whereEqualTo("clase.claseId",clase.getClaseId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()){
                                                for(QueryDocumentSnapshot document: task.getResult()){
                                                    String codigo = document.getString("codigo");
                                                    if(codigo != null) {
                                                        clasesAdapter.getDb().collection("alumnos").document(codigo).delete();
                                                    }
                                                }
                                            }
                                        }
                                    });
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
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(v.getContext(),"Hubo un problema en la base de datos",Toast.LENGTH_SHORT).show();
                    }
                });
                clasesAdapter.notifyDataSetChanged();
                break;
            case R.id.txt_clase:
                clasesAdapter.notifyDataSetChanged();
                Bundle bundle = new Bundle();
                bundle.putSerializable(EXTRA_CLASEVH_CLASE, clase);
                Navigation.findNavController(v).navigate(R.id.nav_alumnos, bundle);
                break;
        }
    }

    public void setOnClickListeners() {
        bt_borrar.setOnClickListener(this);
        txt_rv_misclases.setOnClickListener(this);
    }


}
