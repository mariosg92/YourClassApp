package com.mariosg92.yourclassapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputFilter;
import android.view.View;
import android.widget.TextView;

import com.mariosg92.yourclassapp.clases.Alumno;
import com.mariosg92.yourclassapp.databinding.ActivityAlumnoTabBinding;
import com.mariosg92.yourclassapp.ui.alumno_menu.SectionsPagerAdapter;

import static com.mariosg92.yourclassapp.LoginAlumnosActivity.EXTRA_ALUMNO;


public class AlumnoTabActivity extends AppCompatActivity {

    private ActivityAlumnoTabBinding binding;
    private TextView title;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAlumnoTabBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        Intent intent = getIntent();
        Alumno a = (Alumno) intent.getSerializableExtra(EXTRA_ALUMNO);
        title = findViewById(R.id.title);
        title.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        title.setText(a.getClase().getCurso()+" "+a.getClase().getNombre());
        fab = findViewById(R.id.fab_off);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}