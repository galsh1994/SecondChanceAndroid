package com.example.secondchance.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.secondchance.MyApplicaion;

import java.util.List;

public class Model {

    public final static Model instance= new Model();
    ModelFirebase modelFirebase;

    LiveData<List<User>> userList;

    LiveData<List<Post>> postList;

    ModelSql modelSql;


    private Model(){

        modelFirebase = new ModelFirebase();
        modelSql=ModelSql.instance;

    }

    public interface Listener<T>{
        void onComplete(T result);
    }

    //////// user section /////////////////////////

    public interface getAllUsersListener extends Listener<List<User>>{}

    public LiveData<List<User>> getAllUsers() {
        if(userList==null) {
            userList=ModelSql.instance.getAllUsers();
            refreshAllPosts(null);

        }
        return userList;
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

    public void refreshAllUsers(getAllUsersListener listener){

        SharedPreferences sp= MyApplicaion.context.getSharedPreferences("Users", Context.MODE_PRIVATE);
        Long lastUpdated=sp.getLong("userLastUpdated",0);
        modelFirebase.getAllUsers(lastUpdated,new getAllUsersListener() {
            @Override
            public void onComplete(List<User> result) {

                long lastU=0;
                for (User user: result) {
                    ModelSql.instance.addUser(user,null);
                    if(user.getLastUpdated()>lastU)
                        lastU=user.getLastUpdated();

                }

                SharedPreferences.Editor editor=sp.edit();
                editor.putLong("userLastUpdated",lastU);
                editor.commit();

                if(listener!=null)
                    listener.onComplete(result);


            }
        });
    }
    public interface UploadImageListener extends Listener<String>{ }

    public void uploadImage(Bitmap imageBmp, String name, final UploadImageListener listener) {
        modelFirebase.uploadImage(imageBmp, name, listener);
    }


    /////////////////// post section ////////////////////////////

    public interface addPostListener{ void onComplete();}
    public void addPost(Post post,addPostListener listener){
        modelFirebase.addPost(post,listener);
    }

    public interface GetPostListener{ void onComplete(Post post);}
    public void getPost(String id, GetPostListener listener){
        modelFirebase.getPost( id,  listener);

    }

    public interface getAllPostsListener extends Listener<List<Post>>{}

    public LiveData<List<Post>> getAllPosts() {
        if(postList==null) {
            postList=ModelSql.instance.getAllPosts();
             refreshAllPosts(null);

        }
        return postList;
    }
    public LiveData<List<Post>> getAllUserPost(String userID){
        return ModelSql.instance.getAllUserPosts(userID);
    }

    public void refreshAllPosts(getAllPostsListener listener){

        SharedPreferences sp= MyApplicaion.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
        Long lastUpdated=sp.getLong("lastUpdated",0);
        modelFirebase.getAllPosts(lastUpdated,new getAllPostsListener() {
            @Override
            public void onComplete(List<Post> result) {

                long lastU=0;
                for (Post p: result) {
                    ModelSql.instance.addPost(p,null);
                    if(p.getLastUpdated()>lastU)
                        lastU=p.getLastUpdated();
                    
                }

                SharedPreferences.Editor editor=sp.edit();
                editor.putLong("lastUpdated",lastU);
                editor.commit();

                if(listener!=null)
                    listener.onComplete(result);


            }
        });

    }





}
