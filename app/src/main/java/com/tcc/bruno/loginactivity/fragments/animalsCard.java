package com.tcc.bruno.loginactivity.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tcc.bruno.loginactivity.R;

public class animalsCard extends Fragment {
View view;
TextView idAnimal;
CardView card;
    private ImageView imgDelete, imgEdit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.animalscardview, container, false);




        card = view.findViewById(R.id.cardAnimalsId);
        idAnimal = view.findViewById(R.id.post_id);



        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "idAnimal " + idAnimal, Toast.LENGTH_LONG).show();
            }
        });


        clickDelete();
        return view;
    }

    public  void clickDelete(){


        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(),"Clickou deletar", Toast.LENGTH_SHORT).show();

            }
        });


    }
}