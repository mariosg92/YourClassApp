package com.mariosg92.yourclassapp.ui.alumno_menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mariosg92.yourclassapp.R;
import com.mariosg92.yourclassapp.clases.Alumno;
import com.mariosg92.yourclassapp.clases.RankingAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.mariosg92.yourclassapp.LoginAlumnosActivity.EXTRA_ALUMNO;


public class AlumnoRankingFragment extends Fragment {


    private FirebaseFirestore db;
    private Alumno a;
    private RecyclerView mRecyclerView;
    private RankingAdapter mAdapter;
    private ArrayList<Alumno> alumnos;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_alumno_ranking, container, false);

        db = FirebaseFirestore.getInstance();
        Intent intent = getActivity().getIntent();
        a = (Alumno) intent.getSerializableExtra(EXTRA_ALUMNO);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.rv_alumno_rank);
        alumnos = new ArrayList<>();
        mAdapter = new RankingAdapter(layout.getContext(), alumnos);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(layout.getContext()));
        mRecyclerView.setAdapter(mAdapter);


        db.collection("alumnos").whereEqualTo("clase.claseId",a.getClase().getClaseId())
                .orderBy("puntos", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    alumnos = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String nombre = document.getString("nombre");
                        String apellido1 = document.getString("apellido1");
                        String apellido2 = document.getString("apellido2");
                        long puntos = document.getLong("puntos");
                        Alumno a1 = new Alumno(nombre, apellido1, apellido2, puntos);
                        alumnos.add(a1);
                    }
                    mAdapter.setListaAlumnos(alumnos);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        return layout;
    }
}
