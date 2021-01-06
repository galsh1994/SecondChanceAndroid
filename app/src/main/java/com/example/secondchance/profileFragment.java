package com.example.secondchance;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
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
        String userID= profileFragmentArgs.fromBundle(getArguments()).getUserID();
        Log.d("TAG","user id is:"+userID);

        Button newsFeedFromProfile= view.findViewById(R.id.homeFrom_profile);
        newsFeedFromProfile.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_profileFragment_pop));

        Button profileToself= view.findViewById(R.id.visitProfileFrom_profile);
        profileToself.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_profileFragment_self));

        Button addAPostProfileBtn= view.findViewById(R.id.addApost_profile);
        addAPostProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stID="123";
                profileFragmentDirections.ActionProfileFragmentToAddPostFragment actionAdd = profileFragmentDirections.actionProfileFragmentToAddPostFragment(stID);
                Navigation.findNavController(view).navigate(actionAdd);
            }
        });
        Button messagesFromProfile= view.findViewById(R.id.messagesFrom_profile);
        messagesFromProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stID="123";
                profileFragmentDirections.ActionProfileFragmentToMessagesFragment actionMessages = profileFragmentDirections.actionProfileFragmentToMessagesFragment(stID);
                Navigation.findNavController(view).navigate(actionMessages);
            }
        });
       /* ImageButton itemDetailsFromProfile= view.findViewById(R.id.itemDetailsFrom_profile);
        itemDetailsFromProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_profile_to_item_details);
            }
        });
*/

        return view;
    }
}