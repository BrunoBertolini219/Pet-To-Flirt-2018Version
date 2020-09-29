package com.tcc.bruno.loginactivity.DataModel;

public class UsuarioDataModel {

    private final static String TABELA = "Usuario";
    private final static String id = "id";
    private final static String nome = "nome";
    private final static String email = "email";
    private final static String senha = "senha";

    private static String queryCriarTabela = "";

    public static String criarTabela(){

        queryCriarTabela = "CREATE TABLE "+ TABELA;
        queryCriarTabela += " (";
        queryCriarTabela += id + " INTEGER PRIMARY KEY AUTOINCREMENT, ";
        queryCriarTabela += nome + " TEXT, ";
        queryCriarTabela += email + " TEXT, ";
        queryCriarTabela += senha + " TEXT ";
        queryCriarTabela += ")";

        return queryCriarTabela;
    }


    public UsuarioDataModel() {
    }

    public static String getTABELA() {
        return TABELA;
    }

    public static String getId() {
        return id;
    }

    public static String getNome() {
        return nome;
    }

    public static String getEmail() {
        return email;
    }

    public static String getSenha() {
        return senha;
    }

    public static String getQueryCriarTabela() {
        return queryCriarTabela;
    }

    public static void setQueryCriarTabela(String queryCriarTabela) {
        UsuarioDataModel.queryCriarTabela = queryCriarTabela;
    }
}
