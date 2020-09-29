package com.tcc.bruno.loginactivity.controller;


import android.util.Log;
import android.widget.TextView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.tcc.bruno.loginactivity.config.ConfiguraçãoFirebase;
import com.tcc.bruno.loginactivity.helper.Base64Custom;
import com.tcc.bruno.loginactivity.model.GatoECachorro;
import com.tcc.bruno.loginactivity.model.Usuario;

import java.util.HashMap;
import java.util.Map;


public class UsuarioControllerFirebase {
    private FirebaseAuth autenticacao;
    private DatabaseReference firebase;
    Usuario usuario;
    GatoECachorro gatoECachorro;

    String resultado = null;


    public UsuarioControllerFirebase() {
    }

    public void salvarUsuario(Usuario obj) {

        obj.setSenha("");
        DatabaseReference referenciaFirebase = ConfiguraçãoFirebase.getFirebase();

        Log.e("resultado usuario", "salv: "+obj);

        referenciaFirebase.child("usuarios").child(obj.getId()).setValue(obj);




    }

    public String salvarAnimal(GatoECachorro obj) {
        DatabaseReference referenciaFirebase = ConfiguraçãoFirebase.getFirebase();
        obj.setId(referenciaFirebase.child("animals").push().getKey());



        String key = obj.getId();

        referenciaFirebase.child("animals").child(obj.getUsuarioId()).child(obj.getId()).setValue(obj);


        Log.e("key", "chave animal " + obj.getId());
        return key;
    }

    public String updateAnimal(GatoECachorro obj) {
        DatabaseReference referenciaFirebase = ConfiguraçãoFirebase.getFirebase();

        final Map animal = new HashMap();

        if (obj.getNome() != null)
            animal.put("nome", obj.getNome());

        if (obj.getIdade() != null)
            animal.put("idade", obj.getIdade());

        if (obj.getRaca() != null)
            animal.put("raca", obj.getRaca());

        if (obj.getSexo() != null)
            animal.put("sexo", obj.getSexo());

        if (obj.getCor() != null)
            animal.put("cor", obj.getCor());

        if (obj.getTipoAnimal() != null)
            animal.put("tipoAnimal", obj.getTipoAnimal());


        referenciaFirebase.child("animals").child(obj.getUsuarioId()).child(obj.getId()).updateChildren((animal));

        return "Perfil editado com sucesso";
    }

    public String updateUsuario(Usuario obj) {

        DatabaseReference referenciaFirebase = ConfiguraçãoFirebase.getFirebase();

        final Map usuario = new HashMap();

        if (obj.getNome() != null)
            usuario.put("nome", obj.getNome());

        if (obj.getTelefone() != null)
            usuario.put("telefone", obj.getTelefone());

        if (obj.getIdade() != null)
            usuario.put("idade", obj.getIdade());

        if (obj.getEndereco() != null)
            usuario.put("endereco", obj.getEndereco());

        if (obj.getSexo() != null)
            usuario.put("sexo", obj.getSexo());

        if (obj.getImgusu() != null)
            usuario.put("imgusu", obj.getImgusu());

        if (obj.getEmail() != null)
            usuario.put("email", obj.getEmail());

        if (obj.getLatitude() != null)
            usuario.put("latitude", obj.getLatitude());
            usuario.put("latitude", obj.getLatitude());
            usuario.put("latitude", obj.getLatitude());

        if (obj.getLongitude() != null)
            usuario.put("longitude", obj.getLongitude());


        referenciaFirebase.child("usuarios").child(obj.getId()).updateChildren((usuario));


        return null;
    }


    public void atualizarDadosLocalizacao(double lat, double lon, String idUsuario) {


        DatabaseReference localUsuario = ConfiguraçãoFirebase.getFirebase()
                .child("local_usuario");

        GeoFire geoFire = new GeoFire(localUsuario);

        geoFire.setLocation(idUsuario, new GeoLocation(lat, lon),
                new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if (error != null) {
                            Log.d("Erro", "Erro ao salvar local!");
                        }
                    }
                });
    }


    public void cadastrarUsuario() {

        autenticacao = ConfiguraçãoFirebase.getFirebaseAutenticação();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        );
    }

}