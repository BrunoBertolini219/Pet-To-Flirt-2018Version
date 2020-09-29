package com.tcc.bruno.loginactivity.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
;

import com.tcc.bruno.loginactivity.R;
import com.tcc.bruno.loginactivity.adapter.TabAdapter;
import com.tcc.bruno.loginactivity.helper.SlidingTabLayout;


public class MessengerFragment extends Fragment {

    View view;

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    public MessengerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.messenger_fragment, container, false);


        slidingTabLayout = view.findViewById(R.id.stl_tabs);
        viewPager = view.findViewById(R.id.vppagina);

        TabAdapter tabAdapter = new TabAdapter(getFragmentManager());
        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setDistributeEvenly(true);

        slidingTabLayout.setViewPager(viewPager);
        return view;
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
