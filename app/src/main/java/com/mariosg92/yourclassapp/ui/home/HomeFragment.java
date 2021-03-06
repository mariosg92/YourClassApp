package com.mariosg92.yourclassapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.mariosg92.yourclassapp.R;

public class HomeFragment extends Fragment {

    private static final String TEXT = "text";

    private CardView cardClases;
    private CardView cardRanking;
    private CardView cardReward;

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
        cardRanking = layout.findViewById(R.id.cardRanking);
        cardReward = layout.findViewById(R.id.cardReward);
        try {
            cardClases.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(v).navigate(R.id.nav_misclases);
                }
            });
            cardRanking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(v).navigate(R.id.nav_ranking);
                }
            });
            cardReward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "Implementación en futuro Release", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
        return layout;
    }

}