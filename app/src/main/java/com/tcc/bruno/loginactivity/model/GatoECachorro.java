package com.tcc.bruno.loginactivity.model;

import android.net.Uri;

public class GatoECachorro extends Animal {

    private String id;
    private String usuarioId;
    private String raca;
    private String nome;
    private String idade;
    private String sexo;
    private String cor;
    private String perfilCompact, foto01Compact, foto02Compact, foto03Compact;


    public GatoECachorro(String raca, String nome, String perfilCompact, String foto01Compact, String foto02Compact, String foto03Compact) {
        this.raca = raca;
        this.nome = nome;
        this.perfilCompact = perfilCompact;
        this.foto01Compact = foto01Compact;
        this.foto02Compact = foto02Compact;
        this.foto03Compact = foto03Compact;
    }

    public GatoECachorro() {
    }

    public GatoECachorro(String id, String nome, String perfilCompact) {
        this.id = id;
        this.nome = nome;
        this.perfilCompact = perfilCompact;
    }

    public String getPerfilCompact() {
        return perfilCompact;
    }

    public void setPerfilCompact(String perfilCompact) {
        this.perfilCompact = perfilCompact;
    }

    public String getFoto01Compact() {
        return foto01Compact;
    }

    public void setFoto01Compact(String foto01Compact) {
        this.foto01Compact = foto01Compact;
    }

    public String getFoto02Compact() {
        return foto02Compact;
    }

    public void setFoto02Compact(String foto02Compact) {
        this.foto02Compact = foto02Compact;
    }

    public String getFoto03Compact() {
        return foto03Compact;
    }

    public void setFoto03Compact(String foto03Compact) {
        this.foto03Compact = foto03Compact;
    }

    public GatoECachorro(String s, String lorem_ipaum_dummy_text) {
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getId() {
        return id;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

}
