package com.example.secondchance;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.secondchance.Model.Model;
import com.example.secondchance.Model.Post;
import com.example.secondchance.Model.User;

import java.util.List;

public class newsFeedFragment extends Fragment {

    RecyclerView postList;
    UserListViewModel userListViewModel;
    PostListViewModel postListViewModel;
    String currentUserID="0";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_news_feed, container, false);


        postListViewModel=new ViewModelProvider(this).get(PostListViewModel.class);
        userListViewModel=new ViewModelProvider(this).get(UserListViewModel.class);
        SharedPreferences sp= MyApplicaion.context.getSharedPreferences("Users", Context.MODE_PRIVATE);
        currentUserID=sp.getString("currentUserID","0");

        //postList

        postList=view.findViewById(R.id.postList_newFeed);
        postList.hasFixedSize();

        LinearLayoutManager layoutmaneger = new LinearLayoutManager(this.getContext());
        postList.setLayoutManager(layoutmaneger);

        postListAdapter adapter = new postListAdapter(postListViewModel.getPostList(),userListViewModel.getUserList());
        postList.setAdapter(adapter);



        postListViewModel.getPostList().observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                adapter.notifyDataSetChanged();

            }
        });

        userListViewModel.getUserList().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                adapter.notifyDataSetChanged();
            }
        });







        //Redirect to Add a post
        Button addAPostBtn= view.findViewById(R.id.addApost_newsFeed);
        addAPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newsFeedFragmentDirections.ActionNewsFeedFragmentToAddPostFragment actionAdd = newsFeedFragmentDirections.actionNewsFeedFragmentToAddPostFragment(currentUserID);
                Navigation.findNavController(view).navigate(actionAdd);
            }
        });

        //Redirect to My Profile
        Button visitProfile_newsfeedBtn= view.findViewById(R.id.visitProfileFrom_newsFeed);
        visitProfile_newsfeedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsFeedFragmentDirections.ActionNewsFeedFragmentToProfileFragment actionProfile = newsFeedFragmentDirections.actionNewsFeedFragmentToProfileFragment(currentUserID);
                Navigation.findNavController(view).navigate(actionProfile);
            }
        });


        //Redirect to News feed
        Button newsFeedToSelf= view.findViewById(R.id.homeFrom_newsFeed);
        newsFeedToSelf.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_newsFeedFragment_self));

        //Redirect to map mode
        Button mapMode = view.findViewById(R.id.map_mode);
        mapMode.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_newsFeed_to_maps));




       SwipeRefreshLayout swipeRefreshLayout=view.findViewById(R.id.newsFeedSwipe);
       swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {
               swipeRefreshLayout.setRefreshing(true);
             Model.instance.refreshData(new Model.refreshListener() {
                 @Override
                 public void onComplete() {
                     swipeRefreshLayout.setRefreshing(false);
                 }
             });
           }
       });
        adapter.setOnItemClickListener(new postListAdapter.onItemClickListener() {
            @Override
            public void onClick(int position) {
                int size= postListViewModel.getPostList().getValue().size();
                String postId = postListViewModel.getPostList().getValue().get(size-position-1).getPostID() ;
                Log.d("dd",postId);
                newsFeedFragmentDirections.ActionNewsFeedFragmentToSinglePostFragment actionToSinglePost =
                        newsFeedFragmentDirections.actionNewsFeedFragmentToSinglePostFragment(postId);

                Navigation.findNavController(view).navigate(actionToSinglePost);
            }
        });
        return view;
    }



}