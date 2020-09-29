package com.tcc.bruno.loginactivity.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tcc.bruno.loginactivity.R;
import com.tcc.bruno.loginactivity.model.GatoECachorro;

import java.util.List;

public class arrayAdapterAnimal extends ArrayAdapter<GatoECachorro> {

    Context context;
    List<GatoECachorro> gatoECachorroslista;



    public arrayAdapterAnimal(Context context, int resourceId, List<GatoECachorro> items){
        super(context, resourceId, items);
        this.gatoECachorroslista = items;
        this.context = context;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        GatoECachorro card_item = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.helloText);
        TextView idade = (TextView) convertView.findViewById(R.id.aniIdadeid);
        TextView raca = (TextView) convertView.findViewById(R.id.aniRacaId);
        ImageView image = (ImageView) convertView.findViewById(R.id.imgUsuAnimalId);

        name.setText(card_item.getNome());
        idade.setText(card_item.getIdade());
        raca.setText(card_item.getRaca());
        Picasso.get().load(card_item.getPerfilCompact()).into(image);
        Log.e("Imagem", "PerfilImg:" + card_item.getPerfilCompact());
/*
        switch(card_item.getPerfilCompact()){
            case "default":
                Glide.with(convertView.getContext()).load(R.mipmap.ic_launcher).into(image);
                Picasso.get().load(card_item.getPerfilCompact()).into(image);
                break;
            default:
                Picasso.get().load(card_item.getPerfilCompact()).into(image);
                //Glide.with(convertView.getContext()).load(card_item.getPerfilCompact()).into(image);
                break;
        }

*/
        return convertView;

    }
}

