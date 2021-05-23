package com.mariosg92.yourclassapp.clases;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mariosg92.yourclassapp.AlumnoDetalleActivity;
import com.mariosg92.yourclassapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;



public class AlumnoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public static final String EXTRA_ALUMNOVH_NOMBRE = "com.mariosg92.AlumnoVH.nombre";
    public static final String EXTRA_ALUMNOVH_APELLIDO1 = "com.mariosg92.AlumnoVH.apellido1";
    public static final String EXTRA_ALUMNOVH_APELLIDO2 = "com.mariosg92.AlumnoVH.apellido2";
    public static final String EXTRA_ALUMNOVH_PUNTOS = "com.mariosg92.AlumnoVH.puntos";
    public TextView txt_nombreA;
    public TextView txt_apellidosA;
    public TextView txt_points;
    public ImageView img_alumno;
    public ImageView bt_addPoints;
    public ImageView bt_removePoints;
    final AlumnoAdapter alumnoAdapter; 
    
    public AlumnoViewHolder(@NonNull @NotNull View itemView, AlumnoAdapter alumnoAdapter) {
        super(itemView);
        txt_nombreA = itemView.findViewById(R.id.txt_nombreA);
        txt_apellidosA = itemView.findViewById(R.id.txt_apellidosA);
        txt_points = itemView.findViewById(R.id.txt_points);
        bt_addPoints = itemView.findViewById(R.id.bt_addPoints);
        bt_removePoints = itemView.findViewById(R.id.bt_removePoints);
        img_alumno = itemView.findViewById(R.id.img_Alumno);
        this.alumnoAdapter = alumnoAdapter;
    }

    @Override
    public void onClick(View v) {
        int mPosition = getAdapterPosition();
        List<Alumno> alumnoList = this.alumnoAdapter.getListaAlumnos();
        Alumno alumno = alumnoList.get(mPosition);
        switch(v.getId()){
            case R.id.img_Alumno:
                alumnoAdapter.notifyDataSetChanged();
                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_ALUMNOVH_NOMBRE,alumno.getNombre());
                bundle.putString(EXTRA_ALUMNOVH_APELLIDO1,alumno.getApellido1());
                bundle.putString(EXTRA_ALUMNOVH_APELLIDO2,alumno.getApellido2());
                bundle.putLong(EXTRA_ALUMNOVH_PUNTOS,alumno.getPuntos());
                Intent intent = new Intent(v.getContext(), AlumnoDetalleActivity.class);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
                break;
            case R.id.bt_addPoints:
                break;
            case R.id.bt_removePoints:
                break;
        }
    }

    public void setOnClickerListeners() {
        bt_addPoints.setOnClickListener(this);
        bt_removePoints.setOnClickListener(this);
        img_alumno.setOnClickListener(this);
    }
}
