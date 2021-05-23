package com.mariosg92.yourclassapp.ui.alumnos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mariosg92.yourclassapp.AddAlumnoActivity;
import com.mariosg92.yourclassapp.R;
import com.mariosg92.yourclassapp.clases.Alumno;
import com.mariosg92.yourclassapp.clases.AlumnoAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.mariosg92.yourclassapp.AddAlumnoActivity.EXTRA_ADDALUMNO_LIST;
import static com.mariosg92.yourclassapp.clases.ClasesViewHolder.EXTRA_CLASEVH_CURSO;
import static com.mariosg92.yourclassapp.clases.ClasesViewHolder.EXTRA_CLASEVH_NOMBRE;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlumnosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlumnosFragment extends Fragment {

    public static final String EXTRA_ALUMNOS_ARRAY = "com.mariosg92.AlumnosFragment.alumnosList";
    public static final int ACTIVITY_REQUEST_CODE_2 = 0;
    public static final String EXTRA_CLASE_ALUMNOS = "com.mariosg92.AlumnosFragment.clase";
    public static final String EXTRA_CURSO_ALUMNOS = "com.mariosg92.AlumnosFragment.curso";
    private RecyclerView mRecyclerView;
    private AlumnoAdapter mAdapter;
    private ArrayList<Alumno> alumnosList;
    private FirebaseFirestore db;
    private Button bt_AddAlumno;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private String nombreClase;
    private String cursoClase;

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

        Bundle bundle = getArguments();
        nombreClase = bundle.getString(EXTRA_CLASEVH_NOMBRE);
        cursoClase = bundle.getString(EXTRA_CLASEVH_CURSO);

        alumnosList = new ArrayList<>();
        mRecyclerView = layout.findViewById(R.id.rv_alumnos);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
        getAlumnos();
        mAdapter = new AlumnoAdapter(getContext(), alumnosList);
        mRecyclerView.setAdapter(mAdapter);
        bt_AddAlumno = layout.findViewById(R.id.bt_AddAlumno);
        bt_AddAlumno.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddAlumnoActivity.class);
                intent.putExtra(EXTRA_ALUMNOS_ARRAY,alumnosList);
                intent.putExtra(EXTRA_CLASE_ALUMNOS,nombreClase);
                intent.putExtra(EXTRA_CURSO_ALUMNOS,cursoClase);
                startActivityForResult(intent, ACTIVITY_REQUEST_CODE_2);
            }
        });
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ACTIVITY_REQUEST_CODE_2){
            if(resultCode == AddAlumnoActivity.RESULT_OK){
                alumnosList = (ArrayList<Alumno>) data.getSerializableExtra(EXTRA_ADDALUMNO_LIST);
                mAdapter.setListaAlumnos(alumnosList);
                mAdapter.notifyDataSetChanged();
                mAdapter.notifyItemRangeInserted(0, alumnosList.size());
            }
        }
    }

    private void getAlumnos() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        try {
            Task<QuerySnapshot> querySnapshotTask = db.collection("docentes").document(currentUser.getUid()).collection("clases").whereEqualTo("nombre", nombreClase)
                    .whereEqualTo("curso", cursoClase).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                String claseId = task.getResult().getDocuments().iterator().next().getId();
                                db.collection("docentes").document(currentUser.getUid()).collection("clases").document(claseId).collection("alumnos").get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    alumnosList.clear();
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        String nombre = document.getString("nombre");
                                                        String apellido1 = document.getString("apellido1");
                                                        String apellido2 = document.getString("apellido2");
                                                        long puntos = (long) document.get("puntos");
                                                        Alumno a1 = new Alumno(nombre, apellido1, apellido2, puntos);
                                                        mAdapter.Add(a1);
                                                    }
                                                }
                                            }
                                        });
                            }
                        }
                    });
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}