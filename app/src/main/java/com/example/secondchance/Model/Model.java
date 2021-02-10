package com.example.secondchance.Model;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class Model {

    public final static Model instance= new Model();
    ModelFirebase modelFirebase;
    UserModel userModel;

    // not sure if modelSql is needed
    ModelSql modelSql = new ModelSql();


    private Model(){

        modelFirebase = new ModelFirebase();
        userModel=UserModel.instance;
    }

    public interface Listener<T>{
        void onComplete(T result);
    }

    public interface getAllUsersListener extends Listener<List<User>>{}

    public MutableLiveData<List<User>> getAllUsers() {
        return userModel.getAllUsers();
    }

    public interface GetUserListener{
        void onComplete(User user);
    }
    public void getUser(String id, GetUserListener listener){
        modelFirebase.getUser( id,  listener);
    }

    public interface addUserListener{
        void onComplete();
    }

    public void addUser(User user, addUserListener listener) {
        modelFirebase.addUser(user,listener);
    }

    public interface UpdateUserListener extends addUserListener{}
    public void updateUser(final User user, final addUserListener listener){
        modelFirebase.updateUser(user,listener);
    }

    interface  DeleteListener extends addUserListener{}
    public void deleteUser(User user, DeleteListener listener){
        modelFirebase.delete(user, listener);
    }
    public interface UploadImageListener extends Listener<String>{ }

    public void uploadImage(Bitmap imageBmp, String name, final UploadImageListener listener) {
        modelFirebase.uploadImage(imageBmp, name, listener);
    }
}
