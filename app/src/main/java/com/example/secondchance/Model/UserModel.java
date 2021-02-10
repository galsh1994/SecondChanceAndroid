package com.example.secondchance.Model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class UserModel {
    public final static UserModel instance= new UserModel();
    ModelFirebase modelFirebase;
    MutableLiveData<List<User>> userList;

    private UserModel(){
        modelFirebase = new ModelFirebase();
        userList =new MutableLiveData<List<User>>();
    }


    public MutableLiveData<List<User>> getAllUsers(){
        modelFirebase.getAllUsers(new Model.getAllUsersListener() {
            @Override
            public void onComplete(List<User> result) {

                userList.setValue(result);
            }
        });
        Log.d("TAG","users from db");
        return userList;


     }

}
