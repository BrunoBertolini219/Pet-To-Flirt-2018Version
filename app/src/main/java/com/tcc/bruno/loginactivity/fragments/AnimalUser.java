package com.tcc.bruno.loginactivity.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tcc.bruno.loginactivity.R;


public class AnimalUser extends Fragment {

    View view;
    String nome, telefone, endereco, imagemUsuario;
    ImageView imagemUsu;
    TextView nomeUsu, telefoneUsu, enderecoUsu;

    public AnimalUser() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_animal_user, container, false);

        nome = getArguments().getString("nome");
        telefone = getArguments().getString("telefone");
        endereco = getArguments().getString("endereco");
        imagemUsuario = getArguments().getString("imagemUsuario");

        imagemUsu = view.findViewById(R.id.imgUsuAnimalId);
        nomeUsu = view.findViewById(R.id.usuNomeId);
        telefoneUsu = view.findViewById(R.id.usuTelefoneId);
        enderecoUsu = view.findViewById(R.id.usuCidadeId);


        nomeUsu.setText(nome);
        telefoneUsu.setText(telefone);
        enderecoUsu.setText(endereco);
        Picasso.get().load(imagemUsuario).into(imagemUsu);

        return view;
    }




}
