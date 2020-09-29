package com.tcc.bruno.loginactivity.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.tcc.bruno.loginactivity.DataSource.DataSource;
import com.tcc.bruno.loginactivity.R;
import com.tcc.bruno.loginactivity.controller.UsuarioController;
import com.tcc.bruno.loginactivity.helper.Permissoes;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 5000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        apresentarTelaSplash();

    }

    private void apresentarTelaSplash(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                UsuarioController usuarioController = new UsuarioController(getBaseContext());


                
                DataSource ds = new DataSource(getBaseContext());

             Intent login
                        = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(login);
                finish();



            }



        },SPLASH_TIME_OUT);



    }



}
