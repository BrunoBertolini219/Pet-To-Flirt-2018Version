package com.tcc.bruno.loginactivity.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.tcc.bruno.loginactivity.R;
import com.tcc.bruno.loginactivity.adapter.AdapterRecyclerViewAnimais;
import com.tcc.bruno.loginactivity.config.ConfiguraçãoFirebase;
import com.tcc.bruno.loginactivity.controller.UsuarioControllerFirebase;
import com.tcc.bruno.loginactivity.helper.Base64Custom;
import com.tcc.bruno.loginactivity.model.GatoECachorro;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditarPerfilAnimalFragment extends Fragment {

    Bundle bd;
    TextInputEditText nomeAnimal,  idadeAnimal, corAnimal;
    CircleImageView perfilImagem;
    ImageView imagem01, imagem02, imagem03;
    RadioButton machoAnimal, femeaAnimal, selectedButton;
    RadioGroup sexoGroup;
    Button salvarEdit, cancelarEdit;
    String id, nome, raca, idade, sexo, cor, img01, img02, imgPerfil, img03, tipo = null;
    Spinner tipoSpinner, racaSpinner;
    FirebaseAuth auth = ConfiguraçãoFirebase.getFirebaseAutenticação();
    View view;
    public EditarPerfilAnimalFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_editar_perfil_animal, container, false);



        nomeAnimal = view.findViewById(R.id.editNameAnimal);
        idadeAnimal = view.findViewById(R.id.editIdadeAnimal);
        corAnimal = view.findViewById(R.id.editCorAnimal);

        tipoSpinner = view.findViewById(R.id.spinnerTipoEditId);
        racaSpinner = view.findViewById(R.id.spinnerRacaEditId);

        sexoGroup = view.findViewById(R.id.radioGroupSexoEditId);
        machoAnimal = view.findViewById(R.id.radioMachoEditId);
        femeaAnimal = view.findViewById(R.id.radioFemeaEditId);

        perfilImagem = view.findViewById(R.id.editimgprofileAnimal);


        salvarEdit = view.findViewById(R.id.buttonSalvarAnimalId);
        cancelarEdit = view.findViewById(R.id.buttonCancelarAnimalId);




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
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_animal, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        return view;
    }

    public void recuperarDados() {


         /*   id = (String) bd.get("id");
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
            */

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
        Log.e("id", "idedit"+id);
        Log.e("nome", "nomeedit"+nome);



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

          /*  if (img01 != null)
                Picasso.get().load(img01).into(imagem01);

            if (img02 != null)
                Picasso.get().load(img02).into(imagem02);

            if (img03 != null)
                Picasso.get().load(img03).into(imagem03);*/

        }


    public void salvarDados() {
        String email = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64( email);


        GatoECachorro gatoECachorro = new GatoECachorro();

        int radioId = sexoGroup.getCheckedRadioButtonId();
        selectedButton = view.findViewById(radioId);

        if (nomeAnimal.getText().toString().isEmpty()||idadeAnimal.getText().toString().isEmpty()||corAnimal.getText().toString().isEmpty()
                ||racaSpinner.getSelectedItem().toString().isEmpty()||tipoSpinner.getSelectedItem().toString().isEmpty()||selectedButton.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Preencha todos os campos para finalizar a alteração", Toast.LENGTH_LONG).show();

        }else {

            Log.e("racasalva", "raca: "+racaSpinner.getSelectedItem().toString());
            Log.e("racasalva", "Tipo: "+tipoSpinner.getSelectedItem().toString());
            gatoECachorro.setId(id);
            gatoECachorro.setNome(nomeAnimal.getText().toString());
            gatoECachorro.setIdade(idadeAnimal.getText().toString());
            gatoECachorro.setCor(corAnimal.getText().toString());
            gatoECachorro.setRaca(racaSpinner.getSelectedItem().toString());
            gatoECachorro.setTipoAnimal(tipoSpinner.getSelectedItem().toString());
            gatoECachorro.setSexo(selectedButton.getText().toString());
            gatoECachorro.setUsuarioId(idUsuario);

            UsuarioControllerFirebase usuarioControllerFirebase = new UsuarioControllerFirebase();
            String resultado = usuarioControllerFirebase.updateAnimal(gatoECachorro);
            Toast.makeText(getActivity(), ""+resultado, Toast.LENGTH_LONG).show();

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

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.editPerfilAnimal, perfilAnimalFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

//AnimalsFragment.class.getName()
        }
    }

    public void spinners() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplication(), R.array.Tipo_Animais, android.R.layout.simple_spinner_item);
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
                TextView selectedText = (TextView) parent.getChildAt(0);
                if (selectedText != null) {
                    selectedText.setTextColor(Color.BLACK);
                }


                if (position == 1) {
                    ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.Raca_Cachorro, android.R.layout.simple_spinner_item);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    racaSpinner.setAdapter(adapter2);
                    Log.e("Raça", ": "+raca);
                    if (raca != null) {
                        int spinnerPosition = adapter2.getPosition(raca);
                        racaSpinner.setSelection(spinnerPosition);

                        racaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                TextView selectedText = (TextView) parent.getChildAt(0);
                                if (selectedText != null) {
                                    selectedText.setTextColor(Color.BLACK);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }

                } else if (position == 2) {
                    ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.Raca_Gato, android.R.layout.simple_spinner_item);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    racaSpinner.setAdapter(adapter2);
                    Log.e("Raça", ": "+raca);
                    if (raca != null) {
                        int spinnerPosition = adapter2.getPosition(raca);
                        racaSpinner.setSelection(spinnerPosition);

                        racaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                TextView selectedText = (TextView) parent.getChildAt(0);
                                if (selectedText != null) {
                                    selectedText.setTextColor(Color.BLACK);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }


}


