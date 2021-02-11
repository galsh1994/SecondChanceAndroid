package com.example.secondchance;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.secondchance.Model.Model;
import com.example.secondchance.Model.Post;

import java.util.List;

public class PostListViewModel extends ViewModel {
    private MutableLiveData<List<Post>> postList= Model.instance.getAllPosts();

    public MutableLiveData<List<Post>> getPostList() {
        return postList;
    }
}
