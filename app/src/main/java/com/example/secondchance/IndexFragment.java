package com.example.secondchance;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.secondchance.Model.User;

public class IndexFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_index, container, false);

        Button GoToLogIn = view.findViewById(R.id.log_in_from_index_btn);
        GoToLogIn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_index_to_login));

        Button GoToRegister = view.findViewById(R.id.register_from_index_btn);
        GoToRegister.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_index_to_register));

        return view;
    }

    }
