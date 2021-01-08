package com.example.secondchance;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class newsFeedFragment extends Fragment {

    RecyclerView postList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_news_feed, container, false);

        //postList

        postList=view.findViewById(R.id.postList_newFeed);
        postList.hasFixedSize();

        LinearLayoutManager layoutmaneger = new LinearLayoutManager(this.getContext());
        postList.setLayoutManager(layoutmaneger);

        postListAdapter adapter = new postListAdapter();
        postList.setAdapter(adapter);







        //Redirect to Add a post
        Button addAPostBtn= view.findViewById(R.id.addApost_newsFeed);
        addAPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stID="123";
                newsFeedFragmentDirections.ActionNewsFeedFragmentToAddPostFragment actionAdd = newsFeedFragmentDirections.actionNewsFeedFragmentToAddPostFragment(stID);
                Navigation.findNavController(view).navigate(actionAdd);
            }
        });

        //Redirect to My Profile
        Button visitProfile_newsfeedBtn= view.findViewById(R.id.visitProfileFrom_newsFeed);
        visitProfile_newsfeedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stID="123";
                newsFeedFragmentDirections.ActionNewsFeedFragmentToProfileFragment actionProfile = newsFeedFragmentDirections.actionNewsFeedFragmentToProfileFragment(stID);
                Navigation.findNavController(view).navigate(actionProfile);
            }
        });


        //Redirect to News feed
        Button newsFeedToSelf= view.findViewById(R.id.homeFrom_newsFeed);
        newsFeedToSelf.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_newsFeedFragment_self));

        //Redirect to map mode
        Button mapMode = view.findViewById(R.id.map_mode);
        mapMode.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_newsFeed_to_maps));


        //Redirect to Messages
        Button messagesFromNewsfeedBtn= view.findViewById(R.id.MessagesFrom_newsFeed);
        messagesFromNewsfeedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stID="123";
                newsFeedFragmentDirections.ActionNewsFeedFragmentToInboxFragment actionMessages = newsFeedFragmentDirections.actionNewsFeedFragmentToInboxFragment(stID);
                Navigation.findNavController(view).navigate(actionMessages);
            }
        });
        return view;
    }



}