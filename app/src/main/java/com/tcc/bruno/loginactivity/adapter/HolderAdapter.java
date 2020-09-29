package com.tcc.bruno.loginactivity.adapter;

import android.app.LauncherActivity;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcc.bruno.loginactivity.R;
import com.tcc.bruno.loginactivity.fragments.AnimalsFragment;
import com.tcc.bruno.loginactivity.model.GatoECachorro;

import java.util.List;

public class HolderAdapter extends RecyclerView.Adapter<HolderAdapter.ViewHolder> {

    private List<GatoECachorro> listItems;
    private Context context;

    public HolderAdapter(List<GatoECachorro> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.animalscardview, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GatoECachorro listItem = listItems.get(position);

        holder.textViewNome.setText(listItem.getNome());
        holder.textViewRaca.setText(listItem.getRaca());
        //holder.imgAnimal.setImageURI(Uri.parse(listItem.getImg()));
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewNome;
        public TextView textViewRaca;
        public ImageView imgAnimal;


        public ViewHolder(View itemView) {
            super(itemView);

            textViewRaca = itemView.findViewById(R.id.post_raca);
            textViewNome = itemView.findViewById(R.id.post_nome);
            imgAnimal = itemView.findViewById(R.id.post_img);

        }
    }


    public interface RecyclerViewClickListener{
        public void recuclerViewListClicked(View v, int position);
    }

}
