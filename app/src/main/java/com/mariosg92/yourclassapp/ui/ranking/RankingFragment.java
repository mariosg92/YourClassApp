package com.mariosg92.yourclassapp.ui.ranking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.mariosg92.yourclassapp.R;
import com.mariosg92.yourclassapp.clases.Clases;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RankingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RankingFragment extends Fragment implements AdapterView.OnItemSelectedListener {


    private RecyclerView rv_ranking;
    private Spinner sp_clases = null;
    private ArrayList<Clases> clases = null;
    private ArrayAdapter<Clases> adapterClases = null;
    private Clases claseSeleccionada = null;




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

        if(sp_clases != null){
            sp_clases.setOnItemSelectedListener(this);
            clases = Clases.getClases();
            if(clases != null){
                adapterClases = new ArrayAdapter<Clases>(layout.getContext(),R.layout.sp_clases, clases);
                sp_clases.setAdapter(adapterClases);
            }
        }


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