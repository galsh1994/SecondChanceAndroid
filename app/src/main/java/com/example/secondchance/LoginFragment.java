package com.example.secondchance;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class LoginFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        Button LogInCheck = view.findViewById(R.id.save_and_login_btn);
        LogInCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // we need to do the login's check process here
                // if (true)
                Navigation.findNavController(v).navigate(R.id.action_login_to_newsFeed);
            }
        });

        TextView GoToRegister = view.findViewById(R.id.go_to_register_from_login);
        GoToRegister.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_login_to_register));

        return view;
    }
}