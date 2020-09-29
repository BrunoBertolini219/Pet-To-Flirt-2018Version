package com.tcc.bruno.loginactivity.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.service.autofill.SaveCallback;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import com.tcc.bruno.loginactivity.R;
import com.tcc.bruno.loginactivity.adapter.arrayAdapterAnimal;
import com.tcc.bruno.loginactivity.config.ConfiguraçãoFirebase;
import com.tcc.bruno.loginactivity.helper.Base64Custom;
import com.tcc.bruno.loginactivity.model.GatoECachorro;
import com.tcc.bruno.loginactivity.model.Usuario;
import com.tcc.bruno.loginactivity.view.MainActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.FormatFlagsConversionMismatchException;
import java.util.List;


public class HomeFragment extends Fragment {

    String nome,id,raca,idade,sexo;
    private FirebaseAuth autenticacao = ConfiguraçãoFirebase.getFirebaseAutenticação();
    private DatabaseReference DataBase, DataUsu;
    private ValueEventListener valueEventListenerAnimal;
    private ValueEventListener valueEventListenerUsuario;
    private GatoECachorro gatoECachorro;
    private List<GatoECachorro> listaAnimais = new ArrayList<>();
    private List<Usuario> listaUsuario = new ArrayList<>();
    private List<ImageView> listaFotos = new ArrayList<>();
    Query animal, sex, user;
    List<String> idListaUsuario = new ArrayList<String>();
    private String sexOposto=null;
    Spinner spKilo;
    int x = 0;
    Usuario usuario, usurs;

    private arrayAdapterAnimal arrayAdapterAnimal;
    View view;
    Usuario usuarioLogado = new Usuario();
    Query usuarioo;
    Query animalLocalidade[]= {};
    ImageView buttonGostei, buttonNaoGostei;

    public HomeFragment() {

    }


    private ArrayList<GatoECachorro> rowItems = new ArrayList<>();
    private ArrayList<GatoECachorro> lista = new ArrayList<>();

    private ArrayAdapter<GatoECachorro> arrayAdapterAnimais;
    private int i;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);

        id = getArguments().getString("id");
        nome = getArguments().getString("nome");
        raca = getArguments().getString("raca");
        idade = getArguments().getString("idade");
        sexo = getArguments().getString("sexo");

        recuperarUsuarioLocal();
        MainActivity.ACT = 1;


        buttonGostei = view.findViewById(R.id.pet_gostei_id);
        buttonNaoGostei = view.findViewById(R.id.pet_naogostei_id);

        imgcheck();

        Log.e("Funciona", "Nome:: "+ usuarioLogado.getNome());
        Log.e("Funciona", "Idade: "+ usuarioLogado.getIdade());
        Log.e("Funciona", "Latitude: "+ usuarioLogado.getLatitude());
        Log.e("Funciona", "Longitude: "+ usuarioLogado.getLongitude());










        //recuperarAnimalsListar();

        arrayAdapterAnimal = new arrayAdapterAnimal(getActivity(), R.layout.item, rowItems);

        //adapterAnimals = new AdapterRecyclerViewAnimais(listaAnimais, this.getContext());
        //arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.item, R.id.helloText, rowItems);

        SwipeFlingAdapterView flingContainer = view.findViewById(R.id.frame);
        final MainActivity mainActivity = new MainActivity();

        flingContainer.setAdapter(arrayAdapterAnimal);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapterAnimal.notifyDataSetChanged();



            }

            @Override
            public void onLeftCardExit(Object dataObject) {



                buttonNaoGostei.setVisibility(View.INVISIBLE);

            //    Toast.makeText(getActivity(), "Left!", LENGTH_LONG).show();
            }

            @Override
            public void onRightCardExit( Object dataObject) {
                //  Toast.makeText(getActivity(), "Right!", LENGTH_LONG).show();

                buttonNaoGostei.setVisibility(View.INVISIBLE);
                int indexAnimal =arrayAdapterAnimal.getCount();
                if (!rowItems.isEmpty()) {
                    Log.e("Teste Click", "Arrastando para direita:" + rowItems.get(0).getNome());
                    Log.e("Teste Click", "dataObject: " + dataObject.toString());
                    Log.e("Test", "dataHash "+ dataObject.hashCode());
                }

               // Log.e("Gostei: ", "Animal"+ arrayAdapterAnimal.getCount());

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

                }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                usuario = null;

            //    Toast.makeText(getActivity(), "Clicked!", LENGTH_LONG).show();


                buttonGostei.setVisibility(View.INVISIBLE);
                DataUsu = FirebaseDatabase.getInstance().getReference("usuarios");

                user = DataUsu.orderByKey().equalTo(rowItems.get(itemPosition).getUsuarioId());

                Log.e("Query", "animal" + user);
                valueEventListenerAnimal = user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot animals : dataSnapshot.getChildren()) {

                            usuario = animals.getValue(Usuario.class);

                            Log.e("animal", "dataSnapshot: " + dataSnapshot);
                            Log.e("animal", "animals: " + animals);


                            listaUsuario.add(usuario);
                        }
                        usuarioAnimal();

                    }



                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Log.e("Teste Click", "Clicando: " + rowItems.get(itemPosition).getNome());
                Log.e("Teste Click", "Clicando: " + rowItems.get(itemPosition).getUsuarioId());
                Log.e("Teste Click", "dataObject: " + dataObject.toString());



                Log.e("animal", "id: " + id);
                Log.e("animal", "Nome: " + nome);
                Log.e("animal", "Sexo: " + sexo);

            }

        });
            return view;
        }

    public void escolhaKilometragem() {
        spKilo = view.findViewById(R.id.spinner_kilo_id);


        ArrayAdapter<CharSequence> adapter =  ArrayAdapter.createFromResource(getActivity().getBaseContext(),R.array.Quilometros_usuario, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spKilo.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        spKilo.setSelected(true);

        spKilo.setSelection(6);


        spKilo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView selectedText = (TextView) parent.getChildAt(0);
                if (selectedText != null) {
                    selectedText.setTextColor(Color.BLACK);
                    selectedText.setTypeface(null, Typeface.BOLD);

                }


                switch (position) {


                    case 0:
                        Log.e("UsuarioLogadao", "usu: " + usuarioLogado.getLatitude());
                        iniciarMonitoramentoArea(2);
                        break;

                    case 1:

                        iniciarMonitoramentoArea(5);
                        break;

                    case 2:
                        iniciarMonitoramentoArea(10);
                        break;

                    case 3:
                        iniciarMonitoramentoArea(50);
                        break;

                    case 4:
                        iniciarMonitoramentoArea(100);
                        break;

                    case 5:
                        iniciarMonitoramentoArea(250);
                        break;

                    case 6:
                        iniciarMonitoramentoArea(1000);
                        break;

                    case 7:
                        //recuperarAnimalsListar();
                        arrayAdapterAnimal.notifyDataSetChanged();
                        break;


                    default:
                        Log.d("Nenhuma das opçãos", "não escolheu o raio");

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

        public void usuarioAnimal(){
            AnimalUser animalUser = new AnimalUser();

            Bundle bundle = new Bundle();
            bundle.putString("nome", usuario.getNome());
            bundle.putString("telefone", usuario.getTelefone());
            bundle.putString("endereco", usuario.getEndereco());
            bundle.putString("imagemUsuario", usuario.getImgusu());

                animalUser.setArguments(bundle);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_animal, animalUser);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

    public void recuperarAnimalsListar() {
        animal = null;
        sex = null;

        String email = autenticacao.getCurrentUser().getEmail();
        final String idUsuario = Base64Custom.codificarBase64( email);


        DataBase = FirebaseDatabase.getInstance().getReference("animals");



        animal = DataBase.orderByChild("raca").equalTo(raca);
        sex = DataBase.orderByChild("sexo").equalTo(sexOposto);

        Log.e("Query", "animal"+ animal);
        valueEventListenerAnimal = animal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rowItems.clear();
                listaAnimais.clear();
                Log.d( "Recuperar Animal", "Lista animais: "+ listaAnimais.size());

                        for (DataSnapshot animals : dataSnapshot.getChildren()) {

                            GatoECachorro gatoECachorro = animals.getValue(GatoECachorro.class);
                            if (sexOposto.equals(gatoECachorro.getSexo())) {

                                if (!idUsuario.equals(gatoECachorro.getUsuarioId())) {

                                rowItems.add(gatoECachorro);
                                listaAnimais.add(gatoECachorro);




                            }

                               // Log.e("animal", "dataSnapshot: " + dataSnapshot);
                                Log.e("animal", "ROW: " + rowItems  );
                                Log.e("animal", "animals: " +gatoECachorro );
                            }


                        }
                arrayAdapterAnimal.notifyDataSetChanged();
                    }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("firebasee3", "usuarioId: onCancelled");
            }
        });

    }
    public void recuperarUsuarioLocal(){
        String email = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64( email);
        Log.d("Id", "id do usuario recuperado: "+idUsuario );
        //DatabaseReference usuloca = FirebaseDatabase.getInstance().getReference("usuarios").child(idUsuario);

        DatabaseReference dataBase = FirebaseDatabase.getInstance().getReference("usuarios");

        usuarioo = dataBase.orderByKey().equalTo(idUsuario);

        Log.d("Query do usuario", "consulta da busca: "+ usuarioo);

        valueEventListenerUsuario = usuarioo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("Resultado", "Log DataSnapshot "+ dataSnapshot);
                for (DataSnapshot usuarioo : dataSnapshot.getChildren()) {

                    usuarioLogado = usuarioo.getValue(Usuario.class);


                    Log.e("resultado", "usuario dentro do metodo(for): " + usuarioLogado.getNome());

                }

                escolhaKilometragem();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("resultado", "usuario dentro do metodo(error): " + databaseError);

            }

            });





    }


    public void iniciarMonitoramentoArea (int raio){
        DatabaseReference localUsuario = ConfiguraçãoFirebase.getFirebase().child("local_usuario");
        x =0;
        idListaUsuario.clear();
       Log.e("Monitoramento", "Usuario Lat: " + usuarioLogado.getLatitude());
  //      Log.e("Monitoramento", "Usuario Long: " + userLogado.getLongitude());
         Log.e("Monitoramento", "Raio : " + raio);

        final GeoFire geoFire = new GeoFire(localUsuario);

        Float latitude = Float.valueOf(usuarioLogado.getLatitude());
        Float longitude = Float.valueOf(usuarioLogado.getLongitude());

        final GeoQuery geoQuery = geoFire.queryAtLocation(
                new GeoLocation(latitude, longitude), raio
                );


        rowItems.clear();
        arrayAdapterAnimal.notifyDataSetChanged();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {

            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                if (key != null && !key.isEmpty()) {
                    idListaUsuario.add(key);

                    Log.e("Lista Proximidade", "Animais Proximos:" + key);
                    Log.e("Lista Proximidade", "x posicao:" + x);
                    Log.e("Lista Proximidade", "x posicao:" + location);


                    arrayAdapterAnimal.notifyDataSetChanged();


                }
            }


            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

               listaIdanimaals(idListaUsuario);
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });

        Log.e("Lista Proximidade", "x posicao: asasas");



    }

    public void listaIdanimaals(List<String> idLista) {

        String email = autenticacao.getCurrentUser().getEmail();
        final String idUsuario = Base64Custom.codificarBase64(email);

        Log.d("Id", "id do usuario recuperado: " + idUsuario);
        //DatabaseReference usuloca = FirebaseDatabase.getInstance().getReference("usuarios").child(idUsuario);

        DatabaseReference dataBase = FirebaseDatabase.getInstance().getReference("animals");

        animalLocalidade = new Query[idLista.size()];

        rowItems.clear();
        listaAnimais.clear();

        for (int i = 0; i < idLista.size(); i++) {
            animalLocalidade[i] = dataBase.child(idLista.get(i));
            Log.e("For Lista Animal", "aaa");

        Log.e("Query animalLocalidade", "Query:  " + animalLocalidade);
        //animal = animalLocalidade.orderByChild("raca").equalTo(raca);
        //sex = animalLocalidade.orderByChild("sexo").equalTo(sexOposto);

        valueEventListenerAnimal = animalLocalidade[i].addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                //gatoECachorro = null;

                for (DataSnapshot animals : dataSnapshot.getChildren()) {

                    gatoECachorro = animals.getValue(GatoECachorro.class);
                    if (!sexo.equals(gatoECachorro.getSexo())) {

                    if (!idUsuario.equals(gatoECachorro.getUsuarioId())) {

                        rowItems.add(gatoECachorro);
                        listaAnimais.add(gatoECachorro);


                    }

                    }


                }
                arrayAdapterAnimal.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("firebasee3", "usuarioId: onCancelled");
            }
        });
    }

    }


    public void imgcheck() {

        if (MainActivity.TUT == 0){
            buttonNaoGostei.setVisibility(View.INVISIBLE);
            buttonGostei.setVisibility(View.INVISIBLE);
        }

        if (MainActivity.TUT == 1){
            buttonNaoGostei.setVisibility(View.VISIBLE);
            buttonGostei.setVisibility(View.VISIBLE);
        }

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void raioDeDistancia(Usuario user){



    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("AAAAA", "Log Start ");
        rowItems.clear();
        arrayAdapterAnimal.notifyDataSetChanged();


    }

    @Override
    public void onStop() {
        super.onStop();
        rowItems.clear();
        arrayAdapterAnimal.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("AAAAA", "Log Resume ");
        arrayAdapterAnimal.notifyDataSetChanged();



    }

    @Override
    public void onPause() {
        super.onPause();
        SavedState.CREATOR.newArray(arrayAdapterAnimal.getViewTypeCount());




    }
}


