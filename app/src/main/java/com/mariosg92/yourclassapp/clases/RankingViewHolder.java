package com.mariosg92.yourclassapp.clases;

import android.renderscript.ScriptGroup;
import android.text.InputFilter;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mariosg92.yourclassapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class RankingViewHolder extends RecyclerView.ViewHolder {


    public TextView txt_nombre;
    public TextView txt_points;
    private RankingAdapter rankingAdapter;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;


    public RankingViewHolder(@NonNull @NotNull View itemView, RankingAdapter rankingAdapter) {
        super(itemView);
        txt_nombre = itemView.findViewById(R.id.txt_rankingNombre);
        txt_nombre.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
        txt_points = itemView.findViewById(R.id.txt_rankingPuntos);
        this.rankingAdapter = rankingAdapter;
        db = rankingAdapter.getDb();
        mAuth = rankingAdapter.getmAuth();
        currentUser = rankingAdapter.getCurrentUser();
    }
}
