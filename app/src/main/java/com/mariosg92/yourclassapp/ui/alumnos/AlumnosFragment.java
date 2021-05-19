package com.mariosg92.yourclassapp.ui.alumnos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mariosg92.yourclassapp.R;

import static com.mariosg92.yourclassapp.ui.clases.ClasesFragment.EXTRA_CLASE_CURSO;
import static com.mariosg92.yourclassapp.ui.clases.ClasesFragment.EXTRA_CLASE_NOMBRE;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlumnosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlumnosFragment extends Fragment {


    private TextView txtClase;

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
        txtClase = layout.findViewById(R.id.txtClaseA);
        if(bundle != null) {
            String nombre = bundle.getString("EXTRA_CLASE_NOMBRE");
            String curso = bundle.getString("EXTRA_CLASE_CURSO");
            txtClase.setText(curso + " " + nombre);
        }else{
        }
        return layout;
    }
}