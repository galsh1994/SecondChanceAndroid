package com.example.secondchance;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.secondchance.Model.User;

public class IndexFragment extends Fragment {

    String currentUserID;
    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_index, container, false);



        Button GoToLogIn = view.findViewById(R.id.log_in_from_index_btn);
        GoToLogIn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_index_to_login));

        Button GoToRegister = view.findViewById(R.id.register_from_index_btn);
        GoToRegister.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_index_to_register));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.non_buttons_menu,menu);
    }

    @Override
    public void onStart() {

        super.onStart();

        SharedPreferences sp= MyApplicaion.context.getSharedPreferences("Users", Context.MODE_PRIVATE);
            currentUserID = sp.getString("currentUserID", "0");
            if (!currentUserID.equals("0")) {

                //TODO navigate to news feed

                Navigation.findNavController(view).navigate(R.id.action_indexFragment_to_newsFeedFragment);
            }

    }
}
