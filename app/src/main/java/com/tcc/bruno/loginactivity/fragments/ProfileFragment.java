package com.tcc.bruno.loginactivity.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.tcc.bruno.loginactivity.R;
import com.tcc.bruno.loginactivity.config.ConfiguraçãoFirebase;
import com.tcc.bruno.loginactivity.controller.UsuarioControllerFirebase;
import com.tcc.bruno.loginactivity.helper.Base64Custom;
import com.tcc.bruno.loginactivity.helper.Preferencias;
import com.tcc.bruno.loginactivity.model.Usuario;
import com.tcc.bruno.loginactivity.view.MainActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {


    TextView fieldEmailidi;
    TextView fieldNomeIdi;
    TextView fieldEnderecoIdi;
    TextView fieldSexoIdi;
    TextView fieldidadeIdi;
    TextView fieldTelefone;


    private ProgressDialog progressDialog;
    DatabaseReference databaseReference = ConfiguraçãoFirebase.getFirebase();
    FirebaseAuth firebaseAuth = ConfiguraçãoFirebase.getFirebaseAutenticação();

    private List<Usuario> listaUsuarios = new ArrayList<>();
    Usuario usuario = new Usuario();
    ConstraintLayout constraintLayout;
    ConstraintLayout constraintLayoutEdit;
    Query usuarioo;


    private static final int GALERY_INTENT = 2;
    private ValueEventListener valueEventListenerUsuario;
    private StorageReference storageReference;
    private Bitmap thumb_bitmap = null;

    private EditText editEmail, editNome, editEndereco, editSexo, editIdade, editTelefone;
    public ImageView editItems;
    CircleImageView imgEditProfile, fieldImg;
    private Button salvarButton, cancelarButton;

    View view;
    private DatabaseReference firebase;
    FirebaseAuth usuarioFirebase;
    private StorageReference storageReferencePerfilImg;

    private StorageReference thumbImageRef;
    private ProgressDialog loadingBar;



    public ProfileFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);
        recuperarUsuario();
        MainActivity.ACT = 0;
        //view
        fieldEmailidi = view.findViewById(R.id.fieldRacaAnimal);
        fieldNomeIdi = view.findViewById(R.id.fieldNameAnimal);
        fieldEnderecoIdi = view.findViewById(R.id.fieldCorAnimal);
        fieldidadeIdi = view.findViewById(R.id.fieldIdadeAnimal);
        fieldSexoIdi = view.findViewById(R.id.fieldSexoAnimal);
        fieldTelefone = view.findViewById(R.id.fieldTelefoneId);
        fieldImg = view.findViewById(R.id.imgprofileAnimal);
        editItems = view.findViewById(R.id.editImgAnimal);
        storageReference = FirebaseStorage.getInstance().getReference();


        firebase = ConfiguraçãoFirebase.getFirebase();
        //edit

       // editEmail = view.findViewById(R.id.editEmailId);
        editIdade = view.findViewById(R.id.editIdadeId);
        editNome = view.findViewById(R.id.editNameid);
        editSexo = view.findViewById(R.id.editSexoId);
        editEndereco = view.findViewById(R.id.editEnderecoid);
        editTelefone = view.findViewById(R.id.editTelefoneId);
        imgEditProfile = view.findViewById(R.id.imgprofileidEdit);


        constraintLayout = view.findViewById(R.id.constrant);
        constraintLayoutEdit = view.findViewById(R.id.constrantEdit);

        salvarButton = view.findViewById(R.id.buttonSalvarId);
        cancelarButton = view.findViewById(R.id.buttonCancelarId);


        thumbImageRef = FirebaseStorage.getInstance().getReference().child("Thumb_Images");
        storageReferencePerfilImg = FirebaseStorage.getInstance().getReference().child("UsuarioImagem");

        progressDialog = new ProgressDialog(getActivity());
       Preferencias preferencias = new Preferencias(this.getActivity());


       fieldEmailidi.setText(preferencias.getEmail());
       fieldNomeIdi.setText(preferencias.getNome());

        storageReference = ConfiguraçãoFirebase.getStorageInstance().getReference();
        imgEditProfile.setOnClickListener(new View.OnClickListener() {

                                              @Override
                                              public void onClick(View v) {
                                                  //Intent intent = new Intent();
                                                 // intent.setAction(Intent.ACTION_PICK);

                                                  //intent.setType("image/*");

                                                 // startActivityForResult(intent, GALERY_INTENT);

                                                  Intent intent = new Intent();
                                                  onActivityResult(GALERY_INTENT, RESULT_OK, intent);


                                              }
                                          });

        editar();
        return view;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALERY_INTENT && resultCode == RESULT_OK && data != null) {

            Log.e("Imagem00", "request 01 + uri uri ");

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
                    Log.e("Imagem00", "request 03 + filepath");

                    Uri resultUri = result.getUri();
                    File thumb_filePathUri = new File(resultUri.getPath());

                    String email = firebaseAuth.getCurrentUser().getEmail();
                    final String idUsuario = Base64Custom.codificarBase64(email);



                    try{
                        thumb_bitmap = new Compressor(getContext())
                                .setMaxHeight(200)
                                .setMaxWidth(200)
                                .setQuality(50)
                                .compressToBitmap(thumb_filePathUri);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                    final byte[]  thumb_byte = byteArrayOutputStream.toByteArray();


                    StorageReference filepath = storageReferencePerfilImg.child(idUsuario + ".jpg");
                    final StorageReference thumb_filepath = thumbImageRef.child(idUsuario +".jpg");

                    filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {

                            if (task.isSuccessful()) {
                                Log.e("Imagem00", "request 04 + tasksuc");
                                Toast.makeText(getActivity(), "Salvando sua imagem de perfil...", Toast.LENGTH_LONG).show();

                                final String imagemFull = (task.getResult().getDownloadUrl().toString());

                                UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);

                                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                                        final String thumb_downloadUrl = thumb_task.getResult().getDownloadUrl().toString();

                                        if (task.isSuccessful()){
                                            usuario.setImgusu(thumb_downloadUrl);
                                            final Map update_user_data = new HashMap();
                                            update_user_data.put("imgusu",  thumb_downloadUrl);
                                            update_user_data.put("user_thumb_image", imagemFull);

                                            String userId = firebaseAuth.getCurrentUser().getEmail();
                                            String userCodificado = Base64Custom.codificarBase64(userId);

                                            firebase.child("usuarios").child(userCodificado).updateChildren(update_user_data)
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


/*                    //TODO tratar estrutura (child id user / child id animal / foto)
                    //StorageReference filepath = storageReference.child("FotoUsuario").child(());

                    // fotoPerfil.setImageURI(uri);

                    filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(getActivity(), "Foto salva com sucesso!", Toast.LENGTH_LONG).show();

                        }
                    });
*/







    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void editar(){
        editItems.setOnClickListener(new View.OnClickListener() {




            @Override
            public void onClick(View v) {

                if (!fieldEnderecoIdi.getText().toString().isEmpty())
                editEndereco.setText(fieldEnderecoIdi.getText());

                if (!fieldTelefone.getText().toString().isEmpty())
                    editTelefone.setText(fieldTelefone.getText());

                if (!fieldSexoIdi.getText().toString().isEmpty())
                    editSexo.setText(fieldSexoIdi.getText());

                if (!fieldNomeIdi.getText().toString().isEmpty())
                    editNome.setText(fieldNomeIdi.getText());

                if (!fieldidadeIdi.getText().toString().isEmpty())
                    editIdade.setText(fieldidadeIdi.getText());


                constraintLayout.setVisibility(View.GONE);
                constraintLayoutEdit.setVisibility(View.VISIBLE);




                salvarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String userId = firebaseAuth.getCurrentUser().getEmail();
                        String userCodificado = Base64Custom.codificarBase64(userId);

                        DatabaseReference dataBase = ConfiguraçãoFirebase.getFirebase().child("usuarios");

                        usuario.setId(userCodificado);
                        usuario.setEmail(userId);

                       // if (editNome.getText().toString().isEmpty() && editIdade.getText().toString().isEmpty() && editEndereco.getText().toString().isEmpty()
                 //               && editSexo.getText().toString().isEmpty() && editTelefone.getText().toString().isEmpty()) {

                //            Toast.makeText(getActivity(), "Todos os campos estão vazios", Toast.LENGTH_SHORT).show();
                    //    }else {


                            if (!editNome.getText().toString().isEmpty()) {
                                usuario.setNome(editNome.getText().toString());
                            }else {
                                usuario.setNome(fieldNomeIdi.getText().toString());
                            }

                            if (!editIdade.getText().toString().isEmpty()) {
                                usuario.setIdade(editIdade.getText().toString());
                            }else {

                                if (!fieldidadeIdi.getText().toString().equals("")) {
                                    usuario.setIdade(fieldidadeIdi.getText().toString());
                                }else{

                                }
                            }

                            if (!editEndereco.getText().toString().isEmpty()) {
                                usuario.setEndereco(editEndereco.getText().toString());
                            }else {

                                if (!fieldEnderecoIdi.getText().toString().equals(""))
                                usuario.setEndereco(fieldEnderecoIdi.getText().toString());
                            }

                            if (!editSexo.getText().toString().isEmpty()) {
                                usuario.setSexo(editSexo.getText().toString());
                            }else {

                                if (!fieldSexoIdi.getText().toString().equals(""))
                                    usuario.setSexo(fieldSexoIdi.getText().toString());
                            }

                            if (!editTelefone.getText().toString().isEmpty()) {
                                fieldTelefone.setVisibility(View.VISIBLE);
                                usuario.setTelefone(editTelefone.getText().toString());

                            }else {

                                if (!fieldTelefone.getText().toString().equals(""))
                                    usuario.setTelefone(fieldTelefone.getText().toString());

                            }



                            UsuarioControllerFirebase usuarioControllerFirebase = new UsuarioControllerFirebase();
                            usuarioControllerFirebase.updateUsuario(usuario);


                            fieldEnderecoIdi.setText(usuario.getEndereco());
                            fieldidadeIdi.setText(usuario.getIdade());
                            fieldSexoIdi.setText(usuario.getSexo());
                            fieldNomeIdi.setText(usuario.getNome());
                            fieldEmailidi.setText(usuario.getEmail());
                            fieldTelefone.setText(usuario.getTelefone());
                           // Picasso.get().load(usuario.getImgusu()).into(fieldImg);


                            Toast.makeText(getActivity(), "Usuario alterado com sucesso", Toast.LENGTH_SHORT).show();

                            constraintLayout.setVisibility(View.VISIBLE);
                            constraintLayoutEdit.setVisibility(View.GONE);

                        }


                 //   }
                });

                cancelarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        constraintLayout.setVisibility(View.VISIBLE);
                        constraintLayoutEdit.setVisibility(View.GONE);
                    }
                });

            }
        });
    }

    public void recuperarUsuario(){
        String email = firebaseAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64( email);

        //preferencias = new Preferencias(getActivity());

        // codificar = preferencias.getEmail();

        // user64 = Base64Custom.codificarBase64(codificar);

        DatabaseReference dataBase = FirebaseDatabase.getInstance().getReference("usuarios");

        usuarioo = dataBase.orderByKey().equalTo(idUsuario);

        Log.e("Query", "animal"+ usuarioo);
        valueEventListenerUsuario = usuarioo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaUsuarios.clear();

                for (DataSnapshot usuarioo : dataSnapshot.getChildren()) {

                    Usuario usuario = usuarioo.getValue(Usuario.class);

                    assert usuario != null;
                    fieldidadeIdi.setText(usuario.getIdade());
                    fieldSexoIdi.setText(usuario.getSexo());
                    fieldNomeIdi.setText(usuario.getNome());
                    fieldEnderecoIdi.setText(usuario.getEndereco());
                    Log.e("imgg", ": "+usuario.getImgusu());

                    if (usuario.getImgusu() != null) {
                        Picasso.get().load(usuario.getImgusu()).into(fieldImg);
                        Picasso.get().load(usuario.getImgusu()).into(imgEditProfile);
                    }



                    if (!(usuario.getTelefone() == null)) {
                        fieldTelefone.setVisibility(View.VISIBLE);
                        fieldTelefone.setText(usuario.getTelefone());
                    }
                    listaUsuarios.add(usuario);


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

                @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();
    }



}


          /*      fieldEmailidi.setVisibility(View.GONE);
                fieldNomeIdi.setVisibility(View.GONE);
                fieldEnderecoIdi.setVisibility(View.GONE);
                fieldidadeIdi.setVisibility(View.GONE);
                fieldSexoIdi.setVisibility(View.GONE);
                fieldTelefone.setVisibility(View.GONE);

                editEmail.setVisibility(View.VISIBLE);
                editNome.setVisibility(View.VISIBLE);
                editEndereco.setVisibility(View.VISIBLE);
                editIdade.setVisibility(View.VISIBLE);
                editSexo.setVisibility(View.VISIBLE);
                editTelefone.setVisibility(View.VISIBLE);

                linearLayoutButton.setVisibility(View.VISIBLE);


*/