package com.example.secondchance.Model;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class PostModel {

    public final static PostModel instance= new PostModel();
    ModelFirebase modelFirebase;
    MutableLiveData<List<Post>> postList;

    private PostModel(){
        modelFirebase = new ModelFirebase();
        postList =new MutableLiveData<List<Post>>();
    }


    public MutableLiveData<List<Post>> getAllPosts(){
        modelFirebase.getAllPosts(new Model.getAllPostsListener() {
            @Override
            public void onComplete(List<Post> result) {

                postList.setValue(result);
            }
        });
        Log.d("TAG","users from db");
        return postList;


    }


    public void addPost(Post post, Model.addPostListener listener) {
        modelFirebase.addPost(post,listener);
    }


    public void getPost(String id, Model.GetPostListener listener) {
        modelFirebase.getPost( id,  listener);
    }
}
