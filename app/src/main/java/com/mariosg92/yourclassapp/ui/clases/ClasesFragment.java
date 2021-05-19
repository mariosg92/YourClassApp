package com.mariosg92.yourclassapp.ui.clases;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mariosg92.yourclassapp.AddClaseActivity;
import com.mariosg92.yourclassapp.HomeActivity;
import com.mariosg92.yourclassapp.R;
import com.mariosg92.yourclassapp.clases.Clases;
import com.mariosg92.yourclassapp.clases.ClasesAdapter;
import com.mariosg92.yourclassapp.utils.RecyclerItemClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mariosg92.yourclassapp.AddClaseActivity.EXTRA_ADDCLASE_LIST;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClasesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClasesFragment extends Fragment {

    public static final String EXTRA_CLASE_NOMBRE = "com.mariosg92.ClasesFragment.claseNombre";
    public static final String EXTRA_CLASE_CURSO = "com.mariosg92.ClasesFragment.claseCurso";
    public static final String EXTRA_CLASES_ARRAY = "com.mariosg92.ClasesFragment.ArrayList";
    public static final int ACTIVITY_REQUEST_CODE = 0;
    private RecyclerView mRecyclerView;
    private ClasesAdapter mAdapter;
    private ArrayList<Clases> clasesList;
    private CardView cardAddClass;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClasesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClasesFragment newInstance(String param1, String param2) {
        ClasesFragment fragment = new ClasesFragment();
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
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_clases, container, false);

        clasesList = new ArrayList<>();
        mRecyclerView = layout.findViewById(R.id.rv_misclases);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getMisClases();
        mAdapter = new ClasesAdapter(getContext(), clasesList);
        mRecyclerView.setAdapter(mAdapter);
        cardAddClass = layout.findViewById(R.id.cardAddClass);
        cardAddClass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddClaseActivity.class);
                intent.putExtra(EXTRA_CLASES_ARRAY,clasesList);
                startActivityForResult(intent,ACTIVITY_REQUEST_CODE);
            }
        });

        return layout;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ACTIVITY_REQUEST_CODE){
            if(resultCode == AddClaseActivity.RESULT_OK){
                clasesList = (ArrayList<Clases>) data.getSerializableExtra(EXTRA_ADDCLASE_LIST);
                mAdapter.setListaClases(clasesList);
                mAdapter.notifyDataSetChanged();
                mAdapter.notifyItemRangeInserted(0, clasesList.size());

            }
        }
    }




    private void getMisClases() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        db.collection("docentes").document(currentUser.getUid()).collection("clases").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            clasesList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nombre = document.getString("nombre");
                                String curso = document.getString("curso");
                                Clases c1 = new Clases(nombre, curso);
                                mAdapter.Add(c1);
                            }
                        }
                    }
                });
    }

}