package com.example.secondchance;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class profileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_profile, container, false);

        Button newsFeedFromProfile= view.findViewById(R.id.homeFrom_profile);
        newsFeedFromProfile.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_profileFragment_to_newsFeedFragment));

        Button profileToself= view.findViewById(R.id.visitProfileFrom_profile);
        profileToself.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_profileFragment_self));

        Button addAPostProfileBtn= view.findViewById(R.id.addApost_profile);
        addAPostProfileBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_profileFragment_to_addPostFragment));

        Button messagesFromProfile= view.findViewById(R.id.messagesFrom_profile);
        messagesFromProfile.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_profileFragment_to_inboxFragment));

        ImageButton itemDetailsFromProfile= view.findViewById(R.id.itemDetailsFrom_profile);
        itemDetailsFromProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_profile_to_item_details);
            }
        });


        return view;
    }
}