package com.tcc.bruno.loginactivity.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tcc.bruno.loginactivity.R;
import com.tcc.bruno.loginactivity.view.MainActivity;


public class AboutFragment extends Fragment {

    View view;
    String nome, telefone, endereco, imagemUsuario;
    ImageView imagemUsu;
    TextView nomeUsu, telefoneUsu, enderecoUsu;

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about, container, false);
        MainActivity.ACT = 0;
        return view;
    }




}
