package com.example.secondchance;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.secondchance.Model.Model;
import com.example.secondchance.Model.Post;

import java.util.List;

public class PostListViewModel extends ViewModel {
    private LiveData<List<Post>> postList= Model.instance.getAllPosts();

    public LiveData<List<Post>> getPostList() {
        return postList;
    }
}
