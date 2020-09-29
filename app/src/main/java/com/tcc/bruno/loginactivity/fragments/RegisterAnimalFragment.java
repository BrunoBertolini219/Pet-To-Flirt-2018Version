package com.tcc.bruno.loginactivity.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tcc.bruno.loginactivity.R;
import com.tcc.bruno.loginactivity.config.ConfiguraçãoFirebase;
import com.tcc.bruno.loginactivity.controller.UsuarioControllerFirebase;
import com.tcc.bruno.loginactivity.helper.Base64Custom;
import com.tcc.bruno.loginactivity.helper.Preferencias;
import com.tcc.bruno.loginactivity.model.GatoECachorro;
import com.tcc.bruno.loginactivity.view.MainActivity;

import static android.app.Activity.RESULT_OK;


public class RegisterAnimalFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    GatoECachorro gatoECachorro = new GatoECachorro();
    View view;
    Spinner spTipo, spRaca;
    private EditText fieldNomeAnimal, fieldIdadeAnimal, fieldCor;
    private RadioButton radioButton;
    private RadioGroup sexoRadio;
    private Button registraAnimal, fotoAnimal;
    FirebaseAuth autenticacao;
    DatabaseReference database;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    private ImageView fotoPerfil;
    String user64 = null;
    Preferencias preferencias;
    private static final int GALERY_INTENT = 2;





    public RegisterAnimalFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.register_animal_fragment, container, false);
        preferencias = new Preferencias(this.getActivity());
        Log.e("pref", "RegisterAnimals onCreate :" + preferencias.getEmail());
        MainActivity.ACT = 1;

        fotoPerfil = view.findViewById(R.id.imgperfilId);
        fieldCor = view.findViewById(R.id.fieldCorId);
        fieldNomeAnimal = view.findViewById(R.id.fieldNomeAnimalId);
        fieldIdadeAnimal = view.findViewById(R.id.fieldIdadeAnimalId);
        registraAnimal = (Button) view.findViewById(R.id.cadAnimalId);
        spRaca = view.findViewById(R.id.spinnerRacaId);
        sexoRadio = view.findViewById(R.id.radioGroupSexoId);
        spTipo = view.findViewById(R.id.spinnerTipoId);

        progressDialog = new ProgressDialog(getActivity());


        ArrayAdapter<CharSequence> adapter =  ArrayAdapter.createFromResource(getActivity().getBaseContext(),R.array.Tipo_Animais, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipo.setAdapter(adapter);
        adapter.notifyDataSetChanged();


            spTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 1){
                        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.Raca_Cachorro, android.R.layout.simple_spinner_item);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spRaca.setAdapter(adapter2);

                    }else if (position == 2){
                        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.Raca_Gato, android.R.layout.simple_spinner_item);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spRaca.setAdapter(adapter2);

                    }else if (position == 3){
                        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.Raca_Passaro, android.R.layout.simple_spinner_item);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spRaca.setAdapter(adapter2);
                }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            String codificar = preferencias.getEmail();
        Log.e("pref", "RegisterAnimals codificar :" + codificar);
            user64 = Base64Custom.codificarBase64(codificar);

        registraAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferencias pref = new Preferencias(getActivity());
                int radioId = sexoRadio.getCheckedRadioButtonId();
                radioButton = view.findViewById(radioId);
                database = ConfiguraçãoFirebase.getFirebase();

                if (!(fieldNomeAnimal.getText().toString().isEmpty()||fieldIdadeAnimal.getText().toString().isEmpty()||
                        fieldCor.getText().toString().isEmpty()||radioButton.getText().toString().isEmpty()
                        ||spTipo.getSelectedItem().toString().isEmpty()||spRaca.getSelectedItem().toString().isEmpty()) ){

                    gatoECachorro.setNome(fieldNomeAnimal.getText().toString());
                    gatoECachorro.setIdade(fieldIdadeAnimal.getText().toString());
                    gatoECachorro.setCor(fieldCor.getText().toString());
                    gatoECachorro.setSexo(radioButton.getText().toString());
                    gatoECachorro.setTipoAnimal(spTipo.getSelectedItem().toString());
                    gatoECachorro.setRaca(spRaca.getSelectedItem().toString());

                    gatoECachorro.setUsuarioId(user64);

                    String id = cadastroAnimal();
                    myAnimalsFragment(id);



                }else {
                    Toast.makeText(getActivity(),"Preencha Todos os Campos.", Toast.LENGTH_LONG).show();
                }
            }
        });

        storageReference = ConfiguraçãoFirebase.getStorageInstance().getReference();

       /* fotoAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);

                intent.setType("image/*");

                startActivityForResult(intent,GALERY_INTENT);
            }
        });
*/
        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALERY_INTENT && resultCode == RESULT_OK){

            progressDialog.setMessage("Carregando a Foto...");
            progressDialog.show();
            Uri uri = data.getData();


            //TODO tratar estrutura (child id user / child id animal / foto)
            StorageReference filepath = storageReference.child("Fotos Animal").child((uri.getLastPathSegment()));

           // fotoPerfil.setImageURI(uri);

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(getActivity(), "Foto salva com sucesso!", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });

        }

    }

    public String cadastroAnimal(){
        String key = null;
        try {
            autenticacao = ConfiguraçãoFirebase.getFirebaseAutenticação();
            UsuarioControllerFirebase usuarioControllerFirebase = new UsuarioControllerFirebase();
            key = usuarioControllerFirebase.salvarAnimal(gatoECachorro);
            Log.e("keyr", ": "+ key);
            Toast.makeText(getActivity(), "Animal cadastrado com sucesso!", Toast.LENGTH_LONG).show();
            return key;

        }catch (Exception e){
        e.printStackTrace();
        Toast.makeText(getActivity(), "Erro ao Cadastrar o Animal.",Toast.LENGTH_SHORT).show();
        return key;
        }

    }

    public void myAnimalsFragment(String id){

/*
        Bundle bundle = new Bundle();
        bundle.putString("id", id);

        Log.e("01test", "pq meu deus");


        AnimalsFragment registerAnimalImgFragment = new AnimalsFragment();
        registerAnimalImgFragment.setArguments(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        //fragmentManager.beginTransaction().replace(R.id.register_animal_frag, registerAnimalImgFragment).commit();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.register_animal_frag, registerAnimalImgFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
*/

        Bundle bundle = new Bundle();
        bundle.putString("id", id);

        RegisterAnimalImgFragment fragment = new RegisterAnimalImgFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_animal, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();

        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
