package com.example.secondchance;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.secondchance.Model.Model;
import com.example.secondchance.Model.User;

import java.util.List;

public class UserListViewModel extends ViewModel {
    private MutableLiveData<List<User>> userList= Model.instance.getAllUsers();

    public MutableLiveData<List<User>> getUserList(){
        return userList;
    }

}
