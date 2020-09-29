package com.tcc.bruno.loginactivity.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.tcc.bruno.loginactivity.R;
import com.tcc.bruno.loginactivity.config.ConfiguraçãoFirebase;
import com.tcc.bruno.loginactivity.fragments.AnimalsFragment;
import com.tcc.bruno.loginactivity.helper.Preferencias;
import com.tcc.bruno.loginactivity.model.GatoECachorro;
import com.tcc.bruno.loginactivity.view.MainActivity;

import java.util.List;

public class AdapterRecyclerViewAnimais extends RecyclerView.Adapter<AdapterRecyclerViewAnimais.MyViewHolder> {

    List<GatoECachorro> gatoECachorroslista;
    Context context;

    boolean suc = false;



    public AdapterRecyclerViewAnimais(List<GatoECachorro> gatoECachorroslista, Context context) {
        this.gatoECachorroslista = gatoECachorroslista;
        this.context = context;

    }





    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.animalscardview, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final GatoECachorro gatoECachorro = gatoECachorroslista.get(position);


        holder.nome.setText(gatoECachorro.getNome());
        holder.raca.setText(gatoECachorro.getRaca());
        //Glide.with(context).load(gatoECachorro.getPerfilCompact()).into(holder.foto);
        Picasso.get().load(gatoECachorro.getPerfilCompact()).into(holder.foto);

       /* holder.optiondigit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // TODO Auto-generated method stub
                        switch (item.getItemId()) {
                            case R.id.mnu_item_save:
                                //handle menu1 click
                                return true;
                            case R.id.mnu_item_delete:

                                AnimalsFragment animalsFragment = new AnimalsFragment();
                                //animalsFragment.removeConfirm(gatoECachorro, position);


                                return true;

                            default:
                                return false;
                        }
                    }
                });
                popupMenu.inflate(R.menu.option_menu);
                popupMenu.show();


            }
        });


    }

/*
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.DialogStyle);
                    alert.setTitle("Excluir Animal");
                    alert.setMessage("Você tem certeza que deseja realmente excluir esse animal");
                    alert.setCancelable(true);

                    alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int position = holder.getAdapterPosition();
                            gatoECachorroDelete = gatoECachorroslista.get(position);

                            dataBase.child("animals").child(gatoECachorroDelete.getId()).removeValue();
                            Toast.makeText(context, "Animal deletado", Toast.LENGTH_SHORT).show();
                            notifyItemRemoved(position);


                        }

                    });

                    alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });


                    alert.create();
                    alert.show();
                }

            });
*/


    }


    @Override
    public int getItemCount() {
        return gatoECachorroslista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nome, raca, optiondigit;
        ImageView foto;


        public MyViewHolder(View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.post_nome);
            raca = itemView.findViewById(R.id.post_raca);
            foto = itemView.findViewById(R.id.post_img);





        }

    }


}
