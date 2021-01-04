package com.example.secondchance;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class newsFeedFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_news_feed, container, false);
        Button addAPostBtn= view.findViewById(R.id.addApost_newsFeed);
        addAPostBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_newsFeedFragment_to_addPostFragment));

        Button visitProfile_newsfeedBtn= view.findViewById(R.id.visitProfileFrom_newsFeed);
        visitProfile_newsfeedBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_newsFeedFragment_to_profileFragment));

        Button newsFeedToSelf= view.findViewById(R.id.homeFrom_newsFeed);
        newsFeedToSelf.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_newsFeedFragment_self));

        Button messagesFromNewsfeedBtn= view.findViewById(R.id.MessagesFrom_newsFeed);
        messagesFromNewsfeedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_newsFeedFragment_to_inboxFragment);
            }
        }); // Example for the long clickListener - If there is logic involved in the click

        return view;
    }
}