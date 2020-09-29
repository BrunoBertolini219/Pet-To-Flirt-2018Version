package com.tcc.bruno.loginactivity.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tcc.bruno.loginactivity.R;
import com.tcc.bruno.loginactivity.config.ConfiguraçãoFirebase;
import com.tcc.bruno.loginactivity.controller.UsuarioControllerFirebase;
import com.tcc.bruno.loginactivity.helper.Base64Custom;
import com.tcc.bruno.loginactivity.helper.Preferencias;
import com.tcc.bruno.loginactivity.model.GatoECachorro;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;


public class RegisterAnimalImgFragment extends Fragment {

    GatoECachorro gatoECachorro = new GatoECachorro();
    View view;
    Spinner spTipo, spRaca;


    private Button finalizarRegistro;
    FirebaseAuth autenticacao;
    DatabaseReference database;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    private ImageView fotoPerfil, fotoadd01, fotoadd02, fotoadd03;
    String user64 = null;
    Preferencias preferencias;
    private static final int GALERY_INTENT = 2;
    String idAnimalCadastrado = null;
    private Bitmap thumb_bitmap = null;
    FirebaseAuth firebaseAuth = ConfiguraçãoFirebase.getFirebaseAutenticação();
    String nomeCaminhoFinal = null;
    String nomeCaminhoFinalCompact = null;
    String fotoClick = null;
    Boolean flagImagem = false;
    String email, idUsuario;


    public RegisterAnimalImgFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.register_animal_img_fragment, container, false);
        preferencias = new Preferencias(this.getActivity());
        Log.e("pref", "RegisterAnimals onCreate :" + preferencias.getEmail());

        idAnimalCadastrado = getArguments().getString("id");
        database = ConfiguraçãoFirebase.getFirebase();


        Log.e("id", "\n Chave: " + idAnimalCadastrado);

        fotoPerfil = view.findViewById(R.id.imgprofileAnimalid);
        fotoadd01 = view.findViewById(R.id.imageanimal01);
        fotoadd02 = view.findViewById(R.id.imageanimal02);
        fotoadd03 = view.findViewById(R.id.imageanimal03);
        finalizarRegistro = view.findViewById(R.id.cadAnimalId);


        progressDialog = new ProgressDialog(getActivity());


        String codificar = preferencias.getEmail();
        user64 = Base64Custom.codificarBase64(codificar);


        storageReference = ConfiguraçãoFirebase.getStorageInstance().getReference();

        finalizarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flagImagem) {
                    myAnimalsFragment();
                }else {
                    Toast.makeText(getActivity(), "Adicione uma imagem principal antes de finalizar o cadastro", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomeCaminhoFinal = "perfi";
                nomeCaminhoFinalCompact = "perfilCompact";
                fotoClick = "fotoPerfil";
                Intent intent = new Intent();
                onActivityResult(GALERY_INTENT, RESULT_OK, intent);
            }
        });

        fotoadd01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomeCaminhoFinal = "foto01";
                nomeCaminhoFinalCompact = "foto01Compact";
                fotoClick = "fotoadd01";
                Intent intent = new Intent();
                onActivityResult(GALERY_INTENT, RESULT_OK, intent);
            }
        });

        fotoadd02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomeCaminhoFinal = "foto02";
                nomeCaminhoFinalCompact = "foto02Compact";
                fotoClick = "fotoadd02";
                Intent intent = new Intent();
                onActivityResult(GALERY_INTENT, RESULT_OK, intent);
            }
        });

        fotoadd03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomeCaminhoFinal = "foto03";
                nomeCaminhoFinalCompact = "foto03Compact";
                fotoClick = "fotoadd03";
                Intent intent = new Intent();
                onActivityResult(GALERY_INTENT, RESULT_OK, intent);
            }
        });




        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALERY_INTENT && resultCode == RESULT_OK && data != null) {

            Uri uri = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(getContext(), this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                progressDialog.setMessage("Carregando Foto...");
                progressDialog.show();

                Uri resultUri = result.getUri();
                final File thumb_filePathUri = new File(resultUri.getPath());

                try {
                    thumb_bitmap = new Compressor(getContext())
                            .setMaxHeight(200)
                            .setMaxWidth(200)
                            .setQuality(50)
                            .compressToBitmap(thumb_filePathUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                final byte[] thumb_byte = byteArrayOutputStream.toByteArray();

                StorageReference filepath = storageReference.child("Fotos_Animais").child(idAnimalCadastrado).child(nomeCaminhoFinal+".jpg");
                final StorageReference thumb_filepath = storageReference.child("Fotos_Animais").child(idAnimalCadastrado).child(nomeCaminhoFinalCompact+".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(getActivity(), "Salvando sua imagem de perfil...", Toast.LENGTH_LONG).show();

                            final String imagemFull = (task.getResult().getDownloadUrl().toString());

                            UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);

                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                                    final String thumb_downloadUrl = thumb_task.getResult().getDownloadUrl().toString();

                                    if (task.isSuccessful()) {

                                        switch (fotoClick){

                                            case "fotoPerfil":
                                                fotoPerfil.setImageBitmap(thumb_bitmap);
                                                flagImagem = true;
                                                break;

                                            case "fotoadd01":
                                                fotoadd01.setImageBitmap(thumb_bitmap);
                                                break;

                                            case "fotoadd02":
                                                fotoadd02.setImageBitmap(thumb_bitmap);
                                                break;

                                            case "fotoadd03":
                                                fotoadd03.setImageBitmap(thumb_bitmap);
                                                break;
                                        }

                                        final Map update_user_data = new HashMap();
                                        update_user_data.put(nomeCaminhoFinalCompact, thumb_downloadUrl);
                                        update_user_data.put(nomeCaminhoFinal, imagemFull);


                                        database.child("animals").child(user64).child(idAnimalCadastrado).updateChildren(update_user_data)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        Toast.makeText(getActivity(), "Imagem salva com sucesso", Toast.LENGTH_LONG).show();
                                                        progressDialog.dismiss();
                                                    }
                                                });
                                    }
                                }
                            });


                        } else {

                            Toast.makeText(getActivity(), "Erro ao salvar sua imagem", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();

            }

        }

    }




    public void myAnimalsFragment() {
        Fragment fragment = new AnimalsFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_animal, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }




}

