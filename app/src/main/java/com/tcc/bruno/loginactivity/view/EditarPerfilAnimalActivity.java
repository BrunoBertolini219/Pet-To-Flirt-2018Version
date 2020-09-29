package com.tcc.bruno.loginactivity.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.tcc.bruno.loginactivity.R;
import com.tcc.bruno.loginactivity.config.ConfiguraçãoFirebase;
import com.tcc.bruno.loginactivity.controller.UsuarioControllerFirebase;
import com.tcc.bruno.loginactivity.fragments.AnimalsFragment;
import com.tcc.bruno.loginactivity.fragments.HomeFragment;
import com.tcc.bruno.loginactivity.fragments.PerfilAnimalFragment;
import com.tcc.bruno.loginactivity.helper.Base64Custom;
import com.tcc.bruno.loginactivity.model.GatoECachorro;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarPerfilAnimalActivity extends AppCompatActivity {

    Bundle bd;
    TextInputEditText nomeAnimal, racaAnimal, idadeAnimal, corAnimal;
    CircleImageView perfilImagem;
    ImageView imagem01, imagem02, imagem03;
    RadioButton machoAnimal, femeaAnimal, selectedButton;
    RadioGroup sexoGroup;
    Button salvarEdit, cancelarEdit;
    String id, nome, raca, idade, sexo, cor, img01, img02, imgPerfil, img03, tipo = null;
    Spinner tipoSpinner, racaSpinner;
    FirebaseAuth auth = ConfiguraçãoFirebase.getFirebaseAutenticação();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_editar_perfil_animal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        final Intent intent = getIntent();
        bd = intent.getExtras();

        nomeAnimal = findViewById(R.id.editNameAnimal);
        idadeAnimal = findViewById(R.id.editIdadeAnimal);
        corAnimal = findViewById(R.id.editCorAnimal);

        tipoSpinner = findViewById(R.id.spinnerTipoEditId);
        racaSpinner = findViewById(R.id.spinnerRacaEditId);

        sexoGroup = findViewById(R.id.radioGroupSexoEditId);
        machoAnimal = findViewById(R.id.radioMachoEditId);
        femeaAnimal = findViewById(R.id.radioFemeaEditId);

        perfilImagem = findViewById(R.id.editimgprofileAnimal);


        salvarEdit = findViewById(R.id.buttonSalvarAnimalId);
        cancelarEdit = findViewById(R.id.buttonCancelarAnimalId);



        Log.e("id", "idedit"+id);
        recuperarDados();
        spinners();
        salvarEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarDados();

                }
        });

        cancelarEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AnimalsFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_animal, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

    }


    public void recuperarDados() {

        if (bd != null) {
            id = (String) bd.get("id");
            nome = (String) bd.get("nome");
            raca = (String) bd.get("raca");
            idade = (String) bd.get("idade");
            sexo = (String) bd.get("sexo");
            cor = (String) bd.get("cor");
            imgPerfil = (String) bd.get("imgPerfil");
            img01 = (String) bd.get("img01");
            img02 = (String) bd.get("img02");
            img03 = (String) bd.get("img03");
            tipo = (String) bd.get("tipo");


            assert sexo != null;
            if (sexo.equals("Macho")) {
                machoAnimal.setChecked(true);
            } else {
                femeaAnimal.setChecked(true);
            }

            nomeAnimal.setText(nome);
            idadeAnimal.setText(idade);
            corAnimal.setText(cor);


            if (imgPerfil != null)
                Picasso.get().load(imgPerfil).into(perfilImagem);

            if (img01 != null)
                Picasso.get().load(img01).into(imagem01);

            if (img02 != null)
                Picasso.get().load(img02).into(imagem02);

            if (img03 != null)
                Picasso.get().load(img03).into(imagem03);

        }
    }

    public void salvarDados() {
        String email = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64( email);


        GatoECachorro gatoECachorro = new GatoECachorro();

        int radioId = sexoGroup.getCheckedRadioButtonId();
        selectedButton = findViewById(radioId);

        if (nomeAnimal.getText().toString().isEmpty()||idadeAnimal.getText().toString().isEmpty()||corAnimal.getText().toString().isEmpty()
                ||racaSpinner.getSelectedItem().toString().isEmpty()||tipoSpinner.getSelectedItem().toString().isEmpty()||selectedButton.getText().toString().isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos para finalizar a alteração", Toast.LENGTH_LONG).show();

        }else {
            gatoECachorro.setId(id);
            gatoECachorro.setNome(nomeAnimal.getText().toString());
            gatoECachorro.setIdade(idadeAnimal.getText().toString());
            gatoECachorro.setCor(corAnimal.getText().toString());
            gatoECachorro.setRaca(racaSpinner.getSelectedItem().toString());
            gatoECachorro.setTipoAnimal(tipoSpinner.getSelectedItem().toString());
            gatoECachorro.setSexo(selectedButton.getText().toString());
            gatoECachorro.setUsuarioId(idUsuario);


      /*      if (imgPerfil!=null)
                gatoECachorro.setPerfilCompact(imgPerfil);
            if (img01!=null)
            gatoECachorro.setFoto01Compact(img01);
            if (img01!=null)
            gatoECachorro.setFoto02Compact(img02);
            if (img01!=null)
            gatoECachorro.setFoto03Compact(img03);
*/
            UsuarioControllerFirebase usuarioControllerFirebase = new UsuarioControllerFirebase();
            String resultado = usuarioControllerFirebase.updateAnimal(gatoECachorro);
            Toast.makeText(this, ""+resultado, Toast.LENGTH_LONG).show();

            PerfilAnimalFragment perfilAnimalFragment = new PerfilAnimalFragment();

            Bundle bundle = new Bundle();
            bundle.putString("id", gatoECachorro.getId());
            bundle.putString("nome", gatoECachorro.getNome());
            bundle.putString("raca", gatoECachorro.getRaca());
            bundle.putString("idade", gatoECachorro.getIdade());
            bundle.putString("sexo", gatoECachorro.getSexo());
            bundle.putString("cor", gatoECachorro.getCor());
            bundle.putString("tipo", gatoECachorro.getTipoAnimal());
            bundle.putString("imgPerfil", imgPerfil);
            bundle.putString("img01", img01);
            bundle.putString("img02", img02);
            bundle.putString("img03", img03);

            Log.e("id0", ": " + gatoECachorro.getId());
            perfilAnimalFragment.setArguments(bundle);
            FragmentManager fragmentManager = this.getSupportFragmentManager();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.editPerfilAnimal, perfilAnimalFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
    }

    public void spinners() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplication(), R.array.Tipo_Animais, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        tipoSpinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Log.e("Tipo", ": "+tipo);
        if (tipo != null) {
            int spinnerPosition = adapter.getPosition(tipo);
            tipoSpinner.setSelection(spinnerPosition);
        }

        tipoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 1) {
                    ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getBaseContext(), R.array.Raca_Cachorro, android.R.layout.simple_spinner_item);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    racaSpinner.setAdapter(adapter2);
                    Log.e("Raça", ": "+raca);
                    if (raca != null) {
                        int spinnerPosition = adapter2.getPosition(raca);
                        racaSpinner.setSelection(spinnerPosition);
                    }

                } else if (position == 2) {
                    ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getBaseContext(), R.array.Raca_Gato, android.R.layout.simple_spinner_item);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    racaSpinner.setAdapter(adapter2);
                    Log.e("Raça", ": "+raca);
                    if (raca != null) {
                        int spinnerPosition = adapter2.getPosition(raca);
                        racaSpinner.setSelection(spinnerPosition);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    @Override
    public void onBackPressed() {
       super.onBackPressed();

    }
}