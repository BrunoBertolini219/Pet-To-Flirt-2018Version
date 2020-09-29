package com.tcc.bruno.loginactivity.controller;

import android.content.ContentValues;
import android.content.Context;

import com.tcc.bruno.loginactivity.DataModel.UsuarioDataModel;
import com.tcc.bruno.loginactivity.DataSource.DataSource;
import com.tcc.bruno.loginactivity.model.Usuario;

import java.util.List;

public class UsuarioController extends DataSource {

    ContentValues dados;


    public UsuarioController(Context context) {
        super(context);
    }


    public boolean salvar(Usuario obj){

        boolean sucesso = true;
        dados = new ContentValues();

        dados.put(UsuarioDataModel.getNome(), obj.getNome());
        dados.put(UsuarioDataModel.getEmail(), obj.getEmail());
        dados.put(UsuarioDataModel.getSenha(), obj.getSenha());

        sucesso = insert(UsuarioDataModel.getTABELA(), dados);

        return sucesso;

    }

    public boolean deletar(Usuario obj){

        boolean sucesso = true;

       // sucesso = deletar(UsuarioDataModel.getTABELA(), (obj.getId()));

        return sucesso;
    }

    public boolean alterar(Usuario obj){

        boolean sucesso = true;
        dados = new ContentValues();

        dados.put(UsuarioDataModel.getId(), obj.getId());
        dados.put(UsuarioDataModel.getNome(), obj.getNome());
        dados.put(UsuarioDataModel.getEmail(), obj.getEmail());
        dados.put(UsuarioDataModel.getSenha(), obj.getSenha());

        sucesso = alterar(UsuarioDataModel.getTABELA(), dados);

        return sucesso;

    }
/*
    public List<Usuario> listar(){

        return getAllUsuario();

    }
    */
}
