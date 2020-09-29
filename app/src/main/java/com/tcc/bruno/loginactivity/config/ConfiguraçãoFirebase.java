package com.tcc.bruno.loginactivity.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public final class ConfiguraçãoFirebase {

    private static DatabaseReference referenciaFirebase;
    private static FirebaseAuth autenticacao;
    private static FirebaseStorage firebaseStorage;


    public static DatabaseReference getFirebase() {

        if (referenciaFirebase == null) {
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaFirebase;
    }

    public static FirebaseAuth getFirebaseAutenticação() {

        if (autenticacao == null) {
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }

    public static FirebaseStorage getStorageInstance(){
        if (firebaseStorage == null) firebaseStorage = FirebaseStorage.getInstance();

        return firebaseStorage;
    }



}
