package com.tcc.bruno.loginactivity.config;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;

public class UsuarioFirebase {

    public static FirebaseUser getUsuarioAtual(){
        FirebaseAuth usuario = ConfiguraçãoFirebase.getFirebaseAutenticação();

        return usuario.getCurrentUser();
    }


    public static void atualizarNomeUsuario(String nome){
        try{
            FirebaseUser user = getUsuarioAtual();

            UserProfileChangeRequest profile = new UserProfileChangeRequest
                    .Builder()
                    .setDisplayName(nome)
                    .build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (!task.isSuccessful()){
                        Log.d("Perfil", "Erro ao atualizar dados perfil");
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void atualizarDadosLocalizacao(double lat, double log){

    }
}
