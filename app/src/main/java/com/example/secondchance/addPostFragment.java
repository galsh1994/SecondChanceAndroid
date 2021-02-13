package com.example.secondchance;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.secondchance.Model.Model;
import com.example.secondchance.Model.Post;

public class addPostFragment extends Fragment {

    EditText description;
    EditText location;
    EditText condition;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_add_post, container, false);

        String userID= profileFragmentArgs.fromBundle(getArguments()).getUserID();
        Log.d("TAG","user id is:"+userID);

        Button savePost= view.findViewById(R.id.save_post);
        savePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                description = view.findViewById(R.id.addPostDescription);
                location = view.findViewById(R.id.addPostLocation);
                condition = view.findViewById(R.id.addPostCondition);
                Post post=new Post();
                post.setPostID(String.valueOf(Math.random()));
                post.setDescription(description.getText().toString());
                post.setLocation(location.getText().toString());
                post.setCondition(condition.getText().toString());
                post.setUserID(userID);
                post.setPhotoUrl("test");

                Model.instance.addPost(post, new Model.addPostListener() {
                    @Override
                    public void onComplete() {

                        Navigation.findNavController(view).popBackStack();

                    }
                });



            }
        });


        return view;
    }
}