package com.anstudios.ecommerseadmin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.anstudios.ecommerseadmin.R;

public class HomeFragment extends Fragment {

    private View view;
    private TextView about;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        about=view.findViewById(R.id.home_about);

        return view;
    }
}