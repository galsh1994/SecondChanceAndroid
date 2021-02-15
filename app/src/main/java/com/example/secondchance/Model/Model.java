package com.example.secondchance.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.secondchance.MyApplicaion;

import java.util.List;

public class








Model {

    public final static Model instance= new Model();
    ModelFirebase modelFirebase;

    MutableLiveData<List<User>> userList;

    LiveData<List<Post>> postList;

    // not sure if modelSql is needed
    ModelSql modelSql = new ModelSql();


    private Model(){

        modelFirebase = new ModelFirebase();

        userList =new MutableLiveData<List<User>>();

       // postList =new MutableLiveData<List<Post>>();

    }

    public interface Listener<T>{
        void onComplete(T result);
    }

    //////// user section /////////////////////////

    public interface getAllUsersListener extends Listener<List<User>>{}

    public MutableLiveData<List<User>> getAllUsers() {
        modelFirebase.getAllUsers(new Model.getAllUsersListener() {
            @Override
            public void onComplete(List<User> result) {

                userList.setValue(result);
            }
        });
        Log.d("TAG","users from db");
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
