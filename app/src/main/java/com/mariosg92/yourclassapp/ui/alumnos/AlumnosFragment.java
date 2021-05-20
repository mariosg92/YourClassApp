package com.mariosg92.yourclassapp.ui.alumnos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mariosg92.yourclassapp.R;
import com.mariosg92.yourclassapp.clases.Alumno;
import com.mariosg92.yourclassapp.clases.AlumnoAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.mariosg92.yourclassapp.clases.ClasesViewHolder.EXTRA_CLASEVH_CURSO;
import static com.mariosg92.yourclassapp.clases.ClasesViewHolder.EXTRA_CLASEVH_NOMBRE;
import static com.mariosg92.yourclassapp.ui.clases.ClasesFragment.EXTRA_CLASE_CURSO;
import static com.mariosg92.yourclassapp.ui.clases.ClasesFragment.EXTRA_CLASE_NOMBRE;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlumnosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlumnosFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private AlumnoAdapter mAdapter;
    private ArrayList<Alumno> alumnosList;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    public AlumnosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlumnosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlumnosFragment newInstance(String param1, String param2) {
        AlumnosFragment fragment = new AlumnosFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_alumnos, container, false);

        alumnosList = new ArrayList<>();
        mRecyclerView = layout.findViewById(R.id.rv_alumnos);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
        getAlumnos();
        mAdapter = new AlumnoAdapter(getContext(), alumnosList);
        mRecyclerView.setAdapter(mAdapter);
        return layout;
    }

    private void getAlumnos() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        db.collectionGroup("alumnos").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            alumnosList.clear();
                            for(QueryDocumentSnapshot document: task.getResult()){
                                String nombre = document.getString("nombre");
                                String apellido1 = document.getString("apellido1");
                                String apellido2 = document.getString("apellido2");
                                int puntos = (int) document.get("puntos");
                                Alumno a1 = new Alumno(nombre,apellido1,apellido2,puntos);
                                mAdapter.Add(a1);
                            }
                        }
                    }
                });
    }
}