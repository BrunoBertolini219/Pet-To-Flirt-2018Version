package com.tcc.bruno.loginactivity.view;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.MenuPopupWindow;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;
import com.tcc.bruno.loginactivity.R;
import com.tcc.bruno.loginactivity.config.ConfiguraçãoFirebase;
import com.tcc.bruno.loginactivity.config.UsuarioFirebase;
import com.tcc.bruno.loginactivity.controller.UsuarioController;
import com.tcc.bruno.loginactivity.controller.UsuarioControllerFirebase;
import com.tcc.bruno.loginactivity.fragments.AboutFragment;
import com.tcc.bruno.loginactivity.fragments.AnimalsFragment;
import com.tcc.bruno.loginactivity.fragments.HomeFragment;
import com.tcc.bruno.loginactivity.fragments.MessengerFragment;
import com.tcc.bruno.loginactivity.fragments.ProfileFragment;
import com.tcc.bruno.loginactivity.fragments.RegisterAnimalFragment;
import com.tcc.bruno.loginactivity.helper.Base64Custom;
import com.tcc.bruno.loginactivity.helper.Preferencias;
import com.tcc.bruno.loginactivity.model.Usuario;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {
    public static int ACT = 0;
    public static int TUT = 1;

    android.support.v4.app.FragmentManager fragmentManager;
    View logout;
    FirebaseAuth autenticacao;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseAuth.AuthStateListener getFirebaseAuthListener;
    private static final String COMMON_TAG = "OrintationChange";

    private FirebaseAuth firebaseAuth;

    public Usuario usuario = new Usuario();
    private LocationManager locationManager;
    private LocationListener locationListener;



    private long backPressedTime = 0;

    DatabaseReference firebase;

    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        autenticacao = ConfiguraçãoFirebase.getFirebaseAutenticação();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_fragment, new AnimalsFragment()).commit();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        autenticacao = ConfiguraçãoFirebase.getFirebaseAutenticação();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    setUserData(user);
                } else {
                    goLogInScreen();
                }
            }
        };



    }


    private void recuperarLocalizacaoUsuario() {

        String email = autenticacao.getCurrentUser().getEmail();
        final String idUsuario = Base64Custom.codificarBase64( email);

        Log.e("Localizacao ", "recuperarLocalização entrou no metodo!");
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                String latitude = String.valueOf(location.getLatitude());
                String longitude = String.valueOf(location.getLongitude());

                Usuario usuario = new Usuario();
                usuario.setId(idUsuario);
                usuario.setLatitude(latitude);
                usuario.setLongitude(longitude);

                UsuarioControllerFirebase usuarioControllerFirebase = new UsuarioControllerFirebase();
                usuarioControllerFirebase.updateUsuario(usuario);


                usuarioControllerFirebase.atualizarDadosLocalizacao(location.getLatitude(), location.getLongitude(), idUsuario);

                Log.e("Localizacao ", "Latitude: "+ latitude);
                Log.e("Localizacao ", "Longitude: "+ longitude);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.e("Localizacao ", "Mudou status");
            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.e("Localizacao ", "dentro do checkselfPermission");

            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    100,
                    10,
                    locationListener);

        }


    }

    @Override
    protected void onStart() {
        super.onStart();

        recuperarLocalizacaoUsuario();
        autenticacao.addAuthStateListener(firebaseAuthListener);
    }

    private void setUserData(FirebaseUser user) {


        Preferencias pref = new Preferencias(MainActivity.this);
        pref.salvarDados(user.getEmail(), user.getDisplayName(), user.getUid());

     //   Glide.with(MainActivity.this).load(user.getPhotoUrl()).into(re.fieldImg);

    }




 /*   @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);



        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
                super.onBackPressed();
        }
    }

*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }







    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (item.getItemId()){
            case R.id.logoutId:
                deslogarUsuario();
                return true;

            case R.id.action_settings:

                return true;


            case R.id.tutorialId:
                item.setChecked(!item.isChecked());
                ImageView naoGostei = findViewById(R.id.pet_naogostei_id);
                ImageView gostei = findViewById(R.id.pet_gostei_id);

                if (item.isChecked()) {
                    TUT = 1;

                }

                if (!item.isChecked()) {
                    TUT = 0;

                }


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void revoke(View view) {
        firebaseAuth.signOut();

        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    goLogInScreen();
                } else {
                    Toast.makeText(getApplicationContext(), "Acesso não efetuado com sucesso.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void logOut(View view) {
        firebaseAuth.signOut();

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    goLogInScreen();
                } else {
                    Toast.makeText(getApplicationContext(), "Não foi possivel encerrar o acesso.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void deslogarUsuario(){
        autenticacao.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }



    private void goLogInScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sobre) {
            setTitle("Sobre");
            fragmentManager.beginTransaction().replace(R.id.content_fragment, new AboutFragment()).commit();
        } else if (id == R.id.nav_animal) {
            setTitle("Meus Animais");
            fragmentManager.beginTransaction().replace(R.id.content_fragment, new AnimalsFragment()).commit();
        } else if (id == R.id.nav_profile) {
            setTitle("Perfil");
            fragmentManager.beginTransaction().replace(R.id.content_fragment, new ProfileFragment()).commit();
        } else if (id == R.id.nav_messenger) {
            setTitle("Messenger");
            fragmentManager.beginTransaction().replace(R.id.content_fragment, new MessengerFragment()).commit();
        } else if (id == R.id.nav_share) {
            shareAPP();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void shareAPP(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBody = "Estou utilizando um aplicativo maravilhoso de relacionamento para o meu animal, o nome do aplicativo é PetToFlirt, venha conhecer você também!!!";
        String shareSub = "Aplicativo perfeito para encontrar o par ideal para seu animal";
        intent.putExtra(intent.EXTRA_SUBJECT, shareSub);
        intent.putExtra(intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(intent, "Compartilhar usando"));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (firebaseAuthListener != null) {
            autenticacao.removeAuthStateListener(firebaseAuthListener);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            Log.i(COMMON_TAG, "landscape");
        }else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Log.i(COMMON_TAG, "Portrait");
        }

    }

    @Override
    public void onBackPressed() {        // para previnir saídas inesperadas irritantes
        long t = System.currentTimeMillis();
        if ((t - backPressedTime > 2000) && (ACT == 0)) {    // 2 segundos para sair
            backPressedTime = t;
            Toast.makeText(this, "Para encerrar a aplicação click novamente",
                    Toast.LENGTH_SHORT).show();
        } else {    // se pressionado novamente encerrar app
            // clean up
            super.onBackPressed();       // bye
        }


    }

}
