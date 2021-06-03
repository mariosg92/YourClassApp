package com.mariosg92.yourclassapp.ui.ranking;

import android.icu.text.Edits;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mariosg92.yourclassapp.R;
import com.mariosg92.yourclassapp.clases.Alumno;
import com.mariosg92.yourclassapp.clases.Clases;
import com.mariosg92.yourclassapp.clases.RankingAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;


public class RankingFragment extends Fragment implements AdapterView.OnItemSelectedListener {


    private RecyclerView rv_ranking;
    private Spinner sp_clases = null;
    private ArrayList<Clases> clases = null;
    private ArrayAdapter<Clases> adapterClases = null;
    private Clases claseSeleccionada = null;
    private RankingAdapter mAdapter;




    public RankingFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_ranking, container, false);
        sp_clases = (Spinner) layout.findViewById(R.id.spinner_clases);
        rv_ranking = (RecyclerView) layout.findViewById(R.id.rv_ranking);
        rv_ranking.setLayoutManager(new LinearLayoutManager(getContext()));

        clases = new ArrayList<>();
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
                            if(sp_clases != null){
                                sp_clases.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        Clases c1 = (Clases) sp_clases.getItemAtPosition(position);
                                        claseSeleccionada = c1;
                                        db.collection("docentes").document(currentUser.getUid()).collection("clases")
                                                .document(claseSeleccionada.getClaseId()).collection("alumnos").orderBy("puntos", Query.Direction.DESCENDING)
                                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                               if(task.isSuccessful()){
                                                   ArrayList<Alumno> alumnos = new ArrayList<>();
                                                   for(QueryDocumentSnapshot document: task.getResult()){
                                                       String nombre = document.getString("nombre");
                                                       String apellido1 = document.getString("apellido1");
                                                       String apellido2 = document.getString("apellido2");
                                                       long puntos = document.getLong("puntos");
                                                       Alumno a1 = new Alumno(nombre, apellido1, apellido2, puntos);
                                                       alumnos.add(a1);
                                                   }
                                                   mAdapter = new RankingAdapter(getContext(), alumnos);
                                                   rv_ranking.setAdapter(mAdapter);
                                               }
                                            }
                                        });

                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });

                                if(clases != null){
                                    adapterClases = new ArrayAdapter<Clases>(layout.getContext(),R.layout.sp_clases, clases);
                                    sp_clases.setAdapter(adapterClases);
                                }
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.i("FALLO", "FALLO FIREBASE");
            }
        });




        return layout;


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Clases c1 = (Clases) sp_clases.getItemAtPosition(position);
        claseSeleccionada = c1;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}