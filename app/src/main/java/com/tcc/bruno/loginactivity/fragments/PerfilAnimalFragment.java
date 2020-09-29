package com.tcc.bruno.loginactivity.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.tcc.bruno.loginactivity.R;
import com.tcc.bruno.loginactivity.config.ConfiguraçãoFirebase;
import com.tcc.bruno.loginactivity.helper.Base64Custom;
import com.tcc.bruno.loginactivity.model.GatoECachorro;
import com.tcc.bruno.loginactivity.view.EditarPerfilAnimalActivity;
import com.tcc.bruno.loginactivity.view.MainActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;


public class PerfilAnimalFragment extends Fragment {

    View view;
    TextView racaAnimal, nomeAnimal, sexoAnimal, idadeAnimal, corAnimal, editImgAnimal;
    CircleImageView imgPerfilAnimal;
    DatabaseReference database;
    ImageView imgAnimal01, imgAnimal02, imgAnimal03, imgBackground;
    ImageView editPerfilAnimal, editBackgroundAnimal;
    private StorageReference storageReference;
    String id, act, nome, raca, idade, sexo, cor, img01, img02, imgPerfil, img03, tipo = null;
    String nomeCaminhoFinal, nomeCaminhoFinalCompact, fotoClick;
    private static final int GALERY_INTENT = 1;
    private ProgressDialog progressDialog;
    private Bitmap thumb_bitmap = null;
    FirebaseAuth firebaseAuth = ConfiguraçãoFirebase.getFirebaseAutenticação();
    boolean isImageFitToScreen;


    public PerfilAnimalFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.perfil_animal_fragment, container, false);
        MainActivity.ACT = 1;

        //Texto
        racaAnimal = view.findViewById(R.id.fieldRacaAnimal);
        nomeAnimal = view.findViewById(R.id.fieldNameAnimal);
        sexoAnimal = view.findViewById(R.id.fieldSexoAnimal);
        idadeAnimal = view.findViewById(R.id.fieldIdadeAnimal);
        corAnimal = view.findViewById(R.id.fieldCorAnimal);

        //Imagem

        imgAnimal01 = view.findViewById(R.id.imageanimal01);
        imgAnimal02 = view.findViewById(R.id.imageanimal02);
        imgAnimal03 = view.findViewById(R.id.imageanimal03);
        imgBackground = view.findViewById(R.id.imgBackgroundAnimal);

        //FullScreen
        imgPerfilAnimal = view.findViewById(R.id.imgprofileAnimal);

        //Edit Images
        editImgAnimal = view.findViewById(R.id.editImgAnimal);
        editPerfilAnimal = view.findViewById(R.id.editImagePerfilAnimal);

        database = ConfiguraçãoFirebase.getFirebase();
        progressDialog = new ProgressDialog(getActivity());
        //FireBase Ref
        storageReference = ConfiguraçãoFirebase.getStorageInstance().getReference();
        //Pacote itens
        id = getArguments().getString("id");
        nome = getArguments().getString("nome");
        raca = getArguments().getString("raca");
        idade = getArguments().getString("idade");
        sexo = getArguments().getString("sexo");
        cor = getArguments().getString("cor");

        imgPerfil = getArguments().getString("imgPerfil");
        img01 = getArguments().getString("img01");
        img02 = getArguments().getString("img02");
        img03 = getArguments().getString("img03");
        tipo = getArguments().getString("tipo");


        preencherViewText();

        editarPerfil();
        editImgPerfilAnimal();
        return view;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void preencherViewText() {


        Log.e("id", "\n Chave: " + id);
        Log.e("id", "\n Nome: " + nome);
        Log.e("id", "\n Raca: " + raca);
        Log.e("id", "\n Idade: " + idade);
        Log.e("id", "\n Sexo: " + sexo);
        Log.e("id", "\n id: " + id);

        if (imgPerfil != null)
            Picasso.get().load(imgPerfil).into(imgPerfilAnimal);
        if (img01 != null)
            Picasso.get().load(img01).into(imgAnimal01);
        if (img02 != null)
            Picasso.get().load(img02).into(imgAnimal02);
        if (img03 != null)
            Picasso.get().load(img03).into(imgAnimal03);
        racaAnimal.setText(raca);
        nomeAnimal.setText(nome);
        idadeAnimal.setText(idade);
        corAnimal.setText(cor);
        sexoAnimal.setText(sexo);
    }


    public void editarPerfil() {


        editImgAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditarPerfilAnimalFragment editarPerfilAnimalFragment = new EditarPerfilAnimalFragment();

                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putString("nome", nome);
                bundle.putString("raca", raca);
                bundle.putString("idade", idade);
                bundle.putString("sexo", sexo);
                bundle.putString("cor", cor);
                bundle.putString("imgPerfil", imgPerfil);
                bundle.putString("img01", img01);
                bundle.putString("img02", img02);
                bundle.putString("img03", img03);
                bundle.putString("tipo", tipo);


                editarPerfilAnimalFragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_animal, editarPerfilAnimalFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

    }

    public void editImgPerfilAnimal() {

/*
        editPerfilAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomeCaminhoFinal = "perfi";
                nomeCaminhoFinalCompact = "perfilCompact";
                fotoClick = "fotoPerfil";
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALERY_INTENT);
            }
        });

        imgAnimal01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomeCaminhoFinal = "foto01";
                nomeCaminhoFinalCompact = "foto01Compact";
                fotoClick = "fotoadd01";
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALERY_INTENT);
            }
        });

        imgAnimal02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomeCaminhoFinal = "foto02";
                nomeCaminhoFinalCompact = "foto02Compact";
                fotoClick = "fotoadd02";
                Intent intent = new Intent();
                intent.setType("image/*");
                startActivityForResult(intent, GALERY_INTENT);
            }
        });

*/




editPerfilAnimal.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        nomeCaminhoFinal = "perfi";
        nomeCaminhoFinalCompact = "perfilCompact";
        fotoClick = "fotoPerfil";
        Intent intent = new Intent();
        onActivityResult(GALERY_INTENT, RESULT_OK, intent);
        }
});

imgAnimal01.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        nomeCaminhoFinal = "foto01";
        nomeCaminhoFinalCompact = "foto01Compact";
        fotoClick = "fotoadd01";
        Intent intent = new Intent();
        onActivityResult(GALERY_INTENT, RESULT_OK, intent);
        }
});

imgAnimal02.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        nomeCaminhoFinal = "foto02";
        nomeCaminhoFinalCompact = "foto02Compact";
        fotoClick = "fotoadd02";
        Intent intent = new Intent();
        onActivityResult(GALERY_INTENT, RESULT_OK, intent);
    }
});

imgAnimal03.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        nomeCaminhoFinal = "foto03";
        nomeCaminhoFinalCompact = "foto03Compact";
        fotoClick = "fotoadd03";
        Intent intent = new Intent();
        onActivityResult(GALERY_INTENT, RESULT_OK, intent);
        }
});


/*imgBackground.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
            if (isImageFitToScreen) {
                isImageFitToScreen = false;
                imgBackground.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                imgBackground.setAdjustViewBounds(true);
            } else {
                isImageFitToScreen = true;
                imgBackground.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                imgBackground.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }

});

/*
*                 nomeCaminhoFinal = "foto03";
                nomeCaminhoFinalCompact = "foto03Compact";
                fotoClick = "fotoadd03";
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALERY_INTENT);*/
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

                String email = firebaseAuth.getCurrentUser().getEmail();
                final String idUsuario = Base64Custom.codificarBase64(email);

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

                StorageReference filepath = storageReference.child("Fotos_Animais").child(id).child(nomeCaminhoFinal+".jpg");
                final StorageReference thumb_filepath = storageReference.child("Fotos_Animais").child(id).child(nomeCaminhoFinalCompact +".jpg");

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
                                                editPerfilAnimal.setImageBitmap(thumb_bitmap);
                                                break;

                                            case "fotoadd01":
                                                imgAnimal01.setImageBitmap(thumb_bitmap);
                                                break;

                                            case "fotoadd02":
                                                imgAnimal02.setImageBitmap(thumb_bitmap);
                                                break;

                                            case "fotoadd03":
                                                imgAnimal03.setImageBitmap(thumb_bitmap);
                                                break;
                                        }

                                        final Map update_user_data = new HashMap();
                                        update_user_data.put(nomeCaminhoFinalCompact, thumb_downloadUrl);
                                        update_user_data.put(nomeCaminhoFinal, imagemFull);

                                        Log.e("Vamo ver esse ID", "id"+ id);

                                        database.child("animals").child(idUsuario).child(id).updateChildren(update_user_data)
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

}
