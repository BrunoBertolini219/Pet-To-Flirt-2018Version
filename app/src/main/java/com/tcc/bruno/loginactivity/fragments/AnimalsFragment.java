package com.tcc.bruno.loginactivity.fragments;


import android.Manifest;
import android.animation.TimeAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.browse.MediaBrowser;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tcc.bruno.loginactivity.R;
import com.tcc.bruno.loginactivity.adapter.AdapterRecyclerViewAnimais;
import com.tcc.bruno.loginactivity.adapter.HolderAdapter;
import com.tcc.bruno.loginactivity.adapter.RecyclerItemClickListener;
import com.tcc.bruno.loginactivity.config.ConfiguraçãoFirebase;

import com.tcc.bruno.loginactivity.config.UsuarioFirebase;
import com.tcc.bruno.loginactivity.controller.AnimalsViewHolder;
import com.tcc.bruno.loginactivity.helper.Base64Custom;
import com.tcc.bruno.loginactivity.helper.Preferencias;
import com.tcc.bruno.loginactivity.model.GatoECachorro;
import com.tcc.bruno.loginactivity.view.MainActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AnimalsFragment extends Fragment implements LocationCallback {


    android.support.v7.widget.CardView card;

    private View view;
    private RecyclerView recyclerView;
    private GatoECachorro gatoECachorroDelete;
    private DatabaseReference mDataBase = ConfiguraçãoFirebase.getFirebase();
    private DatabaseReference DataBase;
    private List<GatoECachorro> listaAnimais = new ArrayList<>();
    private AdapterRecyclerViewAnimais adapterAnimals;
    private FirebaseAuth autenticacao = ConfiguraçãoFirebase.getFirebaseAutenticação();
    String user64 = null;
    private DatabaseReference deletarRef;
    private ValueEventListener valueEventListenerAnimal;
    String animalskey[] = null;
    Query animal;
    ImageView dele;
    String codificar;
    Preferencias preferencias;

    private LocationManager locationManager;
    private LocationListener locationListener;


    public AnimalsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.animals_fragment, container, false);
        MainActivity.ACT = 0;



        card = view.findViewById(R.id.cardViewid);
        recyclerView = view.findViewById(R.id.myRecycleView);
        swipe();
        //Configurar Adapter
        adapterAnimals = new AdapterRecyclerViewAnimais(listaAnimais, this.getContext());

        //Configurar Recycler
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterAnimals);


        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new RegisterAnimalFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_animal, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }


        });


       // UsuarioFirebase.atualizarDadosLocalizacao(latitude, longitude);




        clickRecyclerView();



        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

    }


    public void swipe() {
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                excluirMovimentacao(viewHolder);

            }


        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);
    }

    public void excluirMovimentacao(final RecyclerView.ViewHolder viewHolder) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.DialogStyle);
        alert.setTitle("Excluir Animal");
        alert.setMessage("Você tem certeza que deseja realmente excluir esse animal");
        alert.setCancelable(false);

        alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                gatoECachorroDelete = listaAnimais.get(position);

                Log.e("Id Recuperado", "posição do viewHolder:  "+ position);
                Log.e("Id Recuperado", "Id animal viewHolder ID:  "+ gatoECachorroDelete.getId());
                Log.e("Id Recuperado", "Id animal viewHolder NOME:  "+ gatoECachorroDelete.getNome());


                String email = autenticacao.getCurrentUser().getEmail();
                String idUsuario = Base64Custom.codificarBase64( email);

                deletarRef = mDataBase.child("animals").child(idUsuario);
                deletarRef.child(gatoECachorroDelete.getId()).removeValue();
                adapterAnimals.notifyItemRemoved(position);

                Toast.makeText(getActivity(), "Animal deletado", Toast.LENGTH_SHORT).show();
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
                adapterAnimals.notifyDataSetChanged();
            }
        });


        alert.create();
        alert.show();
    }

    public void clickRecyclerView() {


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                GatoECachorro gatoECachorro = listaAnimais.get(position);
                HomeFragment homeFragment = new HomeFragment();

                Bundle bundle = new Bundle();
                bundle.putString("id", gatoECachorro.getId());
                bundle.putString("nome", gatoECachorro.getNome());
                bundle.putString("raca", gatoECachorro.getRaca());
                bundle.putString("idade", gatoECachorro.getIdade());
                bundle.putString("sexo", gatoECachorro.getSexo());
                bundle.putString("imgPerfil", gatoECachorro.getPerfilCompact());
                bundle.putString("tipo", gatoECachorro.getTipoAnimal());

                Log.e("id0", ": " + gatoECachorro.getId());
                homeFragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_animal, homeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }

            @Override
            public void onLongItemClick(final View view, final int position) {

                RecyclerView.ViewHolder viewHolder;

                PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                popupMenu.inflate(R.menu.option_menu);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    popupMenu.setGravity(-5);
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.mnu_item_save:
                                Toast.makeText(getActivity(), "Ir alterar animal", Toast.LENGTH_SHORT).show();

                                GatoECachorro gatoECachorro = listaAnimais.get(position);
                                PerfilAnimalFragment perfilAnimalFragment = new PerfilAnimalFragment();

                                Bundle bundle = new Bundle();
                                bundle.putString("id", gatoECachorro.getId());
                                bundle.putString("nome", gatoECachorro.getNome());
                                bundle.putString("raca", gatoECachorro.getRaca());
                                bundle.putString("idade", gatoECachorro.getIdade());
                                bundle.putString("sexo", gatoECachorro.getSexo());
                                bundle.putString("cor", gatoECachorro.getCor());
                                bundle.putString("imgPerfil", gatoECachorro.getPerfilCompact());
                                bundle.putString("img01", gatoECachorro.getFoto01Compact());
                                bundle.putString("img02", gatoECachorro.getFoto02Compact());
                                bundle.putString("img03", gatoECachorro.getFoto03Compact());
                                bundle.putString("tipo", gatoECachorro.getTipoAnimal());

                                Log.e("id0", ": " + gatoECachorro.getId());
                                perfilAnimalFragment.setArguments(bundle);
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.nav_animal, perfilAnimalFragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                                break;



                            case R.id.mnu_item_delete:

                                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(), R.style.DialogStyle);
                                alert.setTitle("Excluir Animal");
                                alert.setMessage("Você tem certeza que deseja realmente excluir esse animal");
                                alert.setCancelable(true);

                                alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        String email = autenticacao.getCurrentUser().getEmail();
                                        String idUsuario = Base64Custom.codificarBase64( email);


                                        gatoECachorroDelete = listaAnimais.get(position);
                                        mDataBase.child("animals").child(idUsuario).child(gatoECachorroDelete.getId()).removeValue();

                                        adapterAnimals.notifyItemRemoved(position);
                                        recyclerView.requestLayout();

                                        Toast.makeText(getActivity(), "Animal deletado", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
                                        adapterAnimals.notifyDataSetChanged();
                                    }
                                });


                                alert.create();
                                alert.show();

                                break;
                        }
                        return false;

                    }


                });
                popupMenu.show();

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }
        ));
    }



    public void recuperarAnimaisUsuario() {


        String email = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64( email);

        DataBase = FirebaseDatabase.getInstance().getReference("animals");

       animal = DataBase.child(idUsuario).orderByKey();

       Log.e("Query", "animal"+ animal);
        valueEventListenerAnimal = animal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              listaAnimais.clear();
                int animalss = 0;

                for (DataSnapshot animals : dataSnapshot.getChildren()) {

                    GatoECachorro gatoECachorro = animals.getValue(GatoECachorro.class);

                    Log.e("animal", "lista animais: "+gatoECachorro);

                    listaAnimais.add(gatoECachorro);


                }
                adapterAnimals.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("firebasee3", "usuarioId: onCancelled");
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        MainActivity.ACT = 0;
        recuperarAnimaisUsuario();

    }

  /*  private void recuperarLocalizacaoUsuario() {
        Log.e("Localização Animals", "AnimalsFragment recuperarLocalizacao");
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                //recuperar latitude e longitude
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng meuLocal = new LatLng(latitude, longitude);

                Log.e("Localização Animals", "AnimalsFragment "+ meuLocal);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        //Solicitar atualizações de localização
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    100,
                    0,
                    locationListener
            );

            Log.e("Localização Animals", "Solicitar atualização");
        }


    }
*/


    @Override
        public void onStop () {
            super.onStop();

            DataBase.removeEventListener(valueEventListenerAnimal);
        }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.ACT = 0;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        MainActivity.ACT = 0;
    }

    @Override
    public void onLocationResult(String key, GeoLocation location) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        MainActivity.ACT = 0;
    }



}