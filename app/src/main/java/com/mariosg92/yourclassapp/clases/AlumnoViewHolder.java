package com.mariosg92.yourclassapp.clases;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mariosg92.yourclassapp.AlumnoDetalleActivity;
import com.mariosg92.yourclassapp.R;
import com.mariosg92.yourclassapp.ui.alumnos.AlumnosFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;



public class AlumnoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public static final String EXTRA_ALUMNOVH_ALUMNO = "com.mariosg92.AlumnoVH.alumno";
    public static final String EXTRA_ALUMNOVH_POSITION = "com.mariosg92.AlumnoVH.position";
    public TextView txt_nombreA;
    public TextView txt_apellidosA;
    public TextView txt_apellidosA2;
    public TextView txt_points;
    public ImageView img_alumno;
    public ImageView bt_addPoints;
    public ImageView bt_removePoints;
    private AlumnoAdapter alumnoAdapter;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private List<Alumno> alumnoList;
    private Alumno alumno;

    
    public AlumnoViewHolder(@NonNull @NotNull View itemView, AlumnoAdapter alumnoAdapter) {
        super(itemView);
        txt_nombreA = itemView.findViewById(R.id.txt_nombreA);
        txt_nombreA.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
        txt_apellidosA = itemView.findViewById(R.id.txt_apellidosA);
        txt_apellidosA.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        txt_apellidosA2 = itemView.findViewById(R.id.txt_apellidosA2);
        txt_apellidosA2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        txt_points = itemView.findViewById(R.id.txt_points);
        bt_addPoints = itemView.findViewById(R.id.bt_addPoints);
        bt_removePoints = itemView.findViewById(R.id.bt_removePoints);
        img_alumno = itemView.findViewById(R.id.img_Alumno);
        this.alumnoAdapter = alumnoAdapter;
        db = alumnoAdapter.getDb();
        mAuth = alumnoAdapter.getmAuth();
        currentUser = alumnoAdapter.getCurrentUser();
    }

    @Override
    public void onClick(View v) {
        int mPosition = getAdapterPosition();
        alumnoList = alumnoAdapter.getListaAlumnos();
        alumno = alumnoList.get(mPosition);
        switch(v.getId()){
            case R.id.img_Alumno:
                alumnoAdapter.notifyDataSetChanged();
                Bundle bundle = new Bundle();
                bundle.putSerializable(EXTRA_ALUMNOVH_ALUMNO,alumno);
                bundle.putInt(EXTRA_ALUMNOVH_POSITION,mPosition);
                Intent intent = new Intent(v.getContext(), AlumnoDetalleActivity.class);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
                break;
            case R.id.bt_addPoints:
                txt_points.setText(String.valueOf(Integer.valueOf(txt_points.getText().toString())+1));
                long points = Long.valueOf(txt_points.getText().toString());
                alumno.setPuntos(points);
                db.collection("docentes").document(currentUser.getUid())
                        .collection("clases").document(alumno.getClase().getClaseId())
                        .collection("alumnos").document(alumno.getCodigo())
                        .update("puntos",points).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(v.getContext(),"Hubo un problema en la base de datos",Toast.LENGTH_SHORT).show();
                    }
                });
                db.collection("alumnos").document(alumno.getCodigo())
                        .update("puntos",points).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(v.getContext(),"Hubo un problema en la base de datos",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.bt_removePoints:
                if(Integer.valueOf(txt_points.getText().toString()) > 0){
                    txt_points.setText(String.valueOf(Integer.valueOf(txt_points.getText().toString())-1));
                    long points2 = Long.valueOf(txt_points.getText().toString());
                    alumno.setPuntos(points2);
                    db.collection("docentes").document(currentUser.getUid())
                            .collection("clases").document(alumno.getClase().getClaseId())
                            .collection("alumnos").document(alumno.getCodigo())
                            .update("puntos",points2).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(v.getContext(),"Hubo un problema en la base de datos",Toast.LENGTH_SHORT).show();
                        }
                    });
                    db.collection("alumnos").document(alumno.getCodigo())
                            .update("puntos",points2).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(v.getContext(),"Hubo un problema en la base de datos",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
        }
    }

    public void setOnClickerListeners() {
        bt_addPoints.setOnClickListener(this);
        bt_removePoints.setOnClickListener(this);
        img_alumno.setOnClickListener(this);
    }
}
