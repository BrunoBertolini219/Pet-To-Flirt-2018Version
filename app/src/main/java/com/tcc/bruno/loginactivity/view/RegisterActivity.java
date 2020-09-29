package com.tcc.bruno.loginactivity.view;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.tcc.bruno.loginactivity.R;
import com.tcc.bruno.loginactivity.config.ConfiguraçãoFirebase;
import com.tcc.bruno.loginactivity.controller.UsuarioControllerFirebase;
import com.tcc.bruno.loginactivity.helper.Base64Custom;
import com.tcc.bruno.loginactivity.model.Usuario;

public class RegisterActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText password;
    private EditText rePassword;
    private Button registrar;
    private Button termosPrivacidade;
    private Usuario usuario;
    private Cursor cursor;
    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nome = findViewById(R.id.fieldNomeId);
        email = findViewById(R.id.fieldRacaAnimal);
        password = findViewById(R.id.fieldPassId);
        rePassword = findViewById(R.id.fieldRePassId);
        registrar = findViewById(R.id.buttonRegistrarId);



        registrar.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if (!(password.getText().toString().isEmpty() || rePassword.getText().toString().isEmpty()
                        || email.getText().toString().isEmpty() || nome.getText().toString().isEmpty())) {


                        if (password.getText().toString().equals(rePassword.getText().toString())) {



                            usuario = new Usuario();
                            usuario.setNome(nome.getText().toString());
                            usuario.setEmail(email.getText().toString());
                            usuario.setSenha(password.getText().toString());
                            cadastrarUsuario();
                           //



                        } else {
                            Toast.makeText(RegisterActivity.this, "Senha incorreta, digite novamente.", Toast.LENGTH_LONG).show();
                        }

                } else {
                    Toast.makeText(RegisterActivity.this, "Existe campos vazios,favor preencher todos os campos.", Toast.LENGTH_LONG).show();

                }
            }

        });
        }

    private void cadastrarUsuario(){

        autenticacao = ConfiguraçãoFirebase.getFirebaseAutenticação();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful() ){
                    String identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmail());

                    UsuarioControllerFirebase usuarioControllerFirebase = new UsuarioControllerFirebase();

                    usuario.setId(identificadorUsuario );


                    DatabaseReference dataBase = ConfiguraçãoFirebase.getFirebase().child("usuarios");


                    Query comparauser = dataBase.orderByChild("email").equalTo(identificadorUsuario);

                    if (comparauser.getRef().toString().isEmpty()){

                        usuarioControllerFirebase.salvarUsuario(usuario);
                    }

                    Log.e("Cod usu", ""+identificadorUsuario);




                    //autenticacao.signOut();

                    Toast.makeText(RegisterActivity.this, "Usuario cadastrado com sucesso.", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    String erroExcecao = "";
                    try{
                        throw task.getException();



                    } catch (FirebaseAuthWeakPasswordException e) {
                        erroExcecao = "Digite uma senha mais forte, contendo letras, números e mais de 6 dígitos";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erroExcecao = "O e-mail digitado é invalido, digite um novo e-mail.";
                    }catch (FirebaseAuthUserCollisionException e){
                        erroExcecao = "Esse e-mail já está em uso.";
                    }catch (Exception e){
                        erroExcecao = "Erro ao cadastrar Usuário";
                        e.printStackTrace();
                    }

                    //TODO tratar email repetido e senha.
                    Toast.makeText(RegisterActivity.this, "Erro: "+ erroExcecao, Toast.LENGTH_LONG).show();
                }

            }

        });
    }
}
