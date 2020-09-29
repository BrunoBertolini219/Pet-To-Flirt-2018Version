package com.tcc.bruno.loginactivity.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ImageView;

import java.util.HashMap;


public class Preferencias {


    private Context contexto;
    private SharedPreferences preferences;
    private  String NOME_ARQUIVO = "usuario.preferencias";
    private final int MODE = 0;
    private SharedPreferences.Editor editor;

    private String  CHAVE_UID = "identificadorUsuarioLogado";
    private  String CHAVE_EMAIL = "emailUsuarioLogado";
    private  String CHAVE_NOME = "nomeUsuarioLogado";

    private  int CHAVE_INSERT = 0;
    private  String CHAVE_DELETE = "deleta";



    public Preferencias(Context contextoParametro) {

        contexto = contextoParametro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
    }

    public void salvarCard(ImageView delete ){
        editor.putString(CHAVE_DELETE, String.valueOf(delete));
        editor.commit();
    }

    public void salvarDados( String email, String nome, String uid){

        editor.putString(CHAVE_EMAIL, email);
        editor.putString(CHAVE_NOME, nome);
        editor.putString(CHAVE_UID, uid);
        editor.commit();
    }

    public String getNome(){
        return preferences.getString(CHAVE_NOME, null);
    }
    public String getEmail(){
        return preferences.getString(CHAVE_EMAIL, null);
    }
    public String getUid(){return preferences.getString(CHAVE_UID, null);}
    public String getDelete(){return preferences.getString(CHAVE_DELETE, null);}

    public HashMap<String, String> getDadosUsuario(){
        HashMap<String, String> dadosUsuario = new HashMap<>();
        dadosUsuario.put(CHAVE_NOME, preferences.getString(CHAVE_NOME, null));
        dadosUsuario.put(CHAVE_EMAIL, preferences.getString(CHAVE_EMAIL, null));

        return dadosUsuario;
    }
}

