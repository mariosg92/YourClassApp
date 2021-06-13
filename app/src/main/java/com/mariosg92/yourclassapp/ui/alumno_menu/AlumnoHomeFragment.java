package com.mariosg92.yourclassapp.ui.alumno_menu;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mariosg92.yourclassapp.R;
import com.mariosg92.yourclassapp.clases.Alumno;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static com.mariosg92.yourclassapp.LoginAlumnosActivity.EXTRA_ALUMNO;


public class AlumnoHomeFragment extends Fragment {

    private ImageView avatar;
    private TextView nombre_alumno;
    private TextView nombre_clase;
    private FloatingActionButton fab_avatar;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Alumno a;
    private FirebaseFirestore db;
    AlertDialog.Builder builder;
    AlertDialog progressDialog;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_alumno_home,container, false);

        avatar = layout.findViewById(R.id.avatarAlumno);
        nombre_alumno = layout.findViewById(R.id.txt_nombreAlumnoTab);

        fab_avatar = layout.findViewById(R.id.fab_editAvatar);

        db = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReferenceFromUrl("gs://yourclassapp.appspot.com");

        Intent intent = getActivity().getIntent();
        a = (Alumno) intent.getSerializableExtra(EXTRA_ALUMNO);

        nombre_alumno.setText(a.getNombre());
        nombre_alumno.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        Glide.with(layout.getContext()).load(a.getAvatarURL()).into(avatar);


        fab_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    cargarImagen(v);
                    progressDialog = getDialogProgressBar().create();
                    progressDialog.show();
                }catch(Exception e){
                    Toast.makeText(getContext(),"Error al subir archivo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return layout;
    }

    public void cargarImagen(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Selecciona la Aplicación"),10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Uri path = data.getData();
            StorageReference avatarRef = storageReference.child("avatar/"+path.getLastPathSegment());
            UploadTask uploadTask = avatarRef.putFile(path);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(getContext(),"Error al subir archivo", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String avatarName = taskSnapshot.getMetadata().getName();
                    storageReference.child("avatar/"+avatarName).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Uri> task) {
                            String avatarURL = task.getResult().toString();
                            db.collection("alumnos").document(a.getCodigo()).update("avatarURL",avatarURL).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    a.setAvatarURL(avatarURL);
                                    Glide.with(getContext()).load(avatarURL).into(avatar);
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    });
                }
            });

        }
    }


    public AlertDialog.Builder getDialogProgressBar() {

        if (builder == null) {
            builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Cargando...");

            final ProgressBar progressBar = new ProgressBar(getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            progressBar.setLayoutParams(lp);
            builder.setView(progressBar);
        }
        return builder;
    }
}
