package com.example.secondchance;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class RegisterFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_register, container, false);

        Button saveRegister = view.findViewById(R.id.register_btn);
        saveRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save the new user to database
                Navigation.findNavController(v).navigate(R.id.action_register_to_login);
            }
        });

return view;
    }
}