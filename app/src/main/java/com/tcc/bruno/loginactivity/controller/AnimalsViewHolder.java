package com.tcc.bruno.loginactivity.controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.tcc.bruno.loginactivity.R;

import org.w3c.dom.Text;

import java.util.List;

public class AnimalsViewHolder extends RecyclerView.ViewHolder {
    View mView;

    public AnimalsViewHolder(View itemView) {
        super(itemView);
        this.mView = itemView;
    }


    public void setNome(String nome){

        TextView post_nome=mView.findViewById(R.id.post_nome);
        post_nome.setText(nome);

    }

    public void setRaca(String raca){

        TextView post_raca=mView.findViewById(R.id.post_raca);
        post_raca.setText(raca);

    }
    public void setImg (Context ctx, String img){

        ImageView post_img=mView.findViewById(R.id.post_img);
        Glide.with(ctx).load(img).into(post_img);

    }

    public void setId(String id){
        TextView post_nome=mView.findViewById(R.id.post_id);
        post_nome.setText(id);

    }
}

