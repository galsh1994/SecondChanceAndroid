package com.example.secondchance;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.secondchance.Model.Model;
import com.example.secondchance.Model.Post;

import java.util.List;

public class PostListViewModel extends ViewModel {
    private LiveData<List<Post>> postList= Model.instance.getAllPosts();
    private LiveData<List<Post>> userPosts;
    private String userID;

    public LiveData<List<Post>> getPostList() {
        return postList;
    }

    public LiveData<List<Post>> getUserPosts(String id){
        if(userID==null){
            userID=id;
            userPosts=Model.instance.getAllUserPost(id);
            return userPosts;
        }
        if(!userID.equals(id)){
            userID=id;
            userPosts=Model.instance.getAllUserPost(id);
            return userPosts;
        }
        return userPosts;

    }
}