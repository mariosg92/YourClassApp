package com.mariosg92.yourclassapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.navigation.NavigationView;
import com.mariosg92.yourclassapp.HomeActivity;
import com.mariosg92.yourclassapp.R;
import com.mariosg92.yourclassapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private static final String TEXT = "text";

    private CardView cardClases;
    private CardView cardAlumnos;

    public static HomeFragment newInstance(String text) {
        HomeFragment frag = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(TEXT, text);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_home, container, false);

        cardClases = layout.findViewById(R.id.cardClases);
        cardAlumnos = layout.findViewById(R.id.cardAlumnos);

        cardClases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.nav_misclases);
            }
        });
        cardAlumnos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.nav_alumnos);
            }
        });
        return layout;
    }

}