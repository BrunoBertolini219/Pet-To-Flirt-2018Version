package com.tcc.bruno.loginactivity.view;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tcc.bruno.loginactivity.R;
import com.tcc.bruno.loginactivity.config.ConfiguraçãoFirebase;
import com.tcc.bruno.loginactivity.controller.UsuarioControllerFirebase;
import com.tcc.bruno.loginactivity.helper.Base64Custom;
import com.tcc.bruno.loginactivity.helper.Permissoes;
import com.tcc.bruno.loginactivity.helper.Preferencias;
import com.tcc.bruno.loginactivity.model.Usuario;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private DatabaseReference referenciaFirebase;
    private EditText emailLogar, senhaLogar;
    private Button btLogar, btCadastrar;
    private SignInButton signInButton;
    Usuario usuario;
    private FirebaseAuth autenticacao;
    private DatabaseReference firebase;
    private String identificadorContato;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private ProgressBar ProgressBar;
    private DatabaseReference dataBase = ConfiguraçãoFirebase.getFirebase().child("usuarios");
    String usuarioEmailCod;
    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    public static final int SIGN_IN_CODE = 777;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //validarUsuarioLogado();
        Permissoes.validarPermissoes(permissoes, this, 1);


        ProgressBar = findViewById(R.id.progressBar);
        emailLogar = findViewById(R.id.fieldEmailLogarId);
        senhaLogar = findViewById(R.id.fieldPassLogarId);
        btLogar = findViewById(R.id.buttonLogarId);
        btCadastrar = findViewById(R.id.buttonRegistrarLogarId);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

         googleApiClient = new GoogleApiClient.Builder(this)
                 .enableAutoManage(this, this)
                 .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                 .build();

        signInButton = (SignInButton) findViewById(R.id.signInButton);

        signInButton.setSize(SignInButton.SIZE_WIDE);

        signInButton.setColorScheme(SignInButton.COLOR_DARK);

        signInButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                                                startActivityForResult(intent, SIGN_IN_CODE);

                                            }
                                        });
                btCadastrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent abreCadastro = new Intent(LoginActivity.this, RegisterActivity.class);
                        startActivity(abreCadastro);
                    }
                });

                btLogar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        usuario = new Usuario();
                        usuario.setEmail(emailLogar.getText().toString());
                        usuario.setSenha(senhaLogar.getText().toString());
                        validarLogin();


                    }
                });


                autenticacao = FirebaseAuth.getInstance();
                firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth autenticacao) {
                        FirebaseUser user = autenticacao.getCurrentUser();
                        final Usuario usu = new Usuario();

                        final UsuarioControllerFirebase usuarioControllerFirebase = new UsuarioControllerFirebase();


                        if (user != null) {


                            usu.setNome(user.getDisplayName());
                            usu.setEmail(user.getEmail());
                            //dataBase.child("image_perfil").setValue("default");
                            String identificadorUsuario = Base64Custom.codificarBase64(user.getEmail());
                            usu.setId(identificadorUsuario);


                            DatabaseReference key = dataBase.child(identificadorUsuario);

                            dataBase.orderByKey().equalTo(identificadorUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {


                                    if (dataSnapshot.getChildrenCount()==0)
                                        usuarioControllerFirebase.salvarUsuario(usu);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            Log.e("key", ""+key.getKey());



                                goMainScreen();

                        }

                    }

                };

    }

    @Override
    protected void onStart() {
        super.onStart();
        autenticacao.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if (result.isSuccess()){
            if (result.getSignInAccount() != null) {
                firebaseAuthWithGoogle(result.getSignInAccount());
            }else{
                Log.e("Resultado:", "result é nulo");
            }
        }else
            Toast.makeText(this, "Erro ao logar com a conta google", Toast.LENGTH_SHORT).show();

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount signInAccount){

        ProgressBar.setVisibility(View.VISIBLE);
        signInButton.setVisibility(View.GONE);

        AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(),null);
        autenticacao.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                ProgressBar.setVisibility(View.GONE);
                signInButton.setVisibility(View.VISIBLE);

                if (!task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Não foi possivel logar com o google.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean emptyValidation() {
        if (TextUtils.isEmpty(emailLogar.getText().toString()) || TextUtils.isEmpty(senhaLogar.getText().toString())) {
            return true;
        }else {
            return false;
        }
    }

    private void validarUsuarioLogado(){
        autenticacao = ConfiguraçãoFirebase.getFirebaseAutenticação();
        if (autenticacao.getCurrentUser() != null){
            goMainScreen();
        }
    }

    private void validarLogin(){

        autenticacao = ConfiguraçãoFirebase.getFirebaseAutenticação();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    UsuarioControllerFirebase usuarioControllerFirebase = new UsuarioControllerFirebase();
                  //  Log.e("salvando usuario", "usuario Validar: "+usuario);
                   // usuarioControllerFirebase.salvarUsuario(usuario);
                    Toast.makeText(LoginActivity.this, "Login Efetuado.", Toast.LENGTH_SHORT).show();
                    goMainScreen();
                } else {
                    String erroExcecao = "";

                    try {
                        throw task.getException();


                    } catch (FirebaseAuthInvalidUserException e) {
                        erroExcecao = "E-mail não cadastrado, digite um e-mail válido.";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "Senha incorreta, digite novamente.";
                    } catch (Exception e) {
                        erroExcecao = "Erro ao cadastrar Usuário.";
                        e.printStackTrace();
                    }

                    Toast.makeText(LoginActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_SHORT).show();

                }

            }
        });

    }
    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuthListener != null)
        autenticacao.removeAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int permissaoResultado : grantResults){
            if( permissaoResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();

            }
        }

    }

    private void alertaValidacaoPermissao(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogStyle);
        builder.setTitle("Permissões Negadas");

        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

}



