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
    public interface refreshListener{
        public void onComplete();
    }
    public void refreshData(refreshListener listener){
        refreshAllPosts(new getAllPostsListener() {
            @Override
            public void onComplete(List<Post> result) {
                updateDeletedPosts(new UpdateDeletedUsersListener() {
                    @Override
                    public void onComplete(String result) {
                        refreshAllUsers(new getAllUsersListener() {
                            @Override
                            public void onComplete(List<User> result) {
                                updateDeletedUsers(new UpdateDeletedUsersListener() {
                                    @Override
                                    public void onComplete(String result) {
                                        if(listener!=null)
                                        listener.onComplete();

                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }


    ///////// Authentication section  /////////////////////////

    public interface SuccessListener{
        void onComplete(boolean flag);
    }
    public interface idSaverListener{
        void onComplete(boolean flag,String id);
    }
    public void registerAuthFB(User user,String password,idSaverListener listener) {
        modelFirebase.registerAuthFB(user,password,listener);
    }

    public void logInAuth(String email,String password, idSaverListener listener) {
        modelFirebase.logInAuth(email,password,listener);
    }
    public void resetPass(String email, SuccessListener listener) {
        modelFirebase.resetPass(email,listener);
    }

    //////// user section /////////////////////////

    public interface getAllUsersListener extends Listener<List<User>>{}

    public LiveData<List<User>> getAllUsers() {
        if(userList==null) {
            userList=ModelSql.instance.getAllUsers();
            refreshAllUsers(null);

        }
        return userList;
    }

    public interface GetUserListener{
        void onComplete(User user);
    }
    public void getUser(String id, GetUserListener listener){
         modelFirebase.getUser(id,listener);
     }

    public interface addUserListener{
        void onComplete();
    }

    public void addUser(User user, addUserListener listener) {
        modelFirebase.addUser(user,listener);
    }

    public interface UpdateUserListener extends addUserListener{}
    public void updateUser(final User user, final UpdateUserListener listener){
        modelFirebase.updateUser(user,listener);
    }

    interface  DeleteListener extends addUserListener{}
    public void deleteUser(User user){
        for (Post post:postList.getValue())
            if(post.getUserID().equals(user.getUserID()))
                deletePost(post);


        modelFirebase.deleteUser(user, new DeleteListener() {
            @Override
            public void onComplete() {
                ModelSql.instance.deleteUser(user,null);
            }
        });

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

    public void updateDeletedUsers(UpdateDeletedUsersListener listener){

        SharedPreferences sp = MyApplicaion.context.getSharedPreferences("Users", Context.MODE_PRIVATE);
        Long lastDeleted = sp.getLong("lastDeleted", 0);
        modelFirebase.getAllDeletedUsers(lastDeleted, new ModelFirebase.GetAllDeletedUsersListener(){
            @Override
            public void onComplete(List<User> result) {

                long lastD = 0;
                for (User user : result) {
                    ModelSql.instance.deleteUser(user, null);
                    if (user.getLastUpdated() > lastD)
                        lastD = user.getLastUpdated();

                }

                SharedPreferences.Editor editor = sp.edit();
                editor.putLong("lastDeleted", lastD);
                editor.commit();

                if (listener != null)
                    listener.onComplete(null);
            }
        });
    }

    public interface UploadUserImageListener extends Listener<String>{ }

    public void uploadUserImage(Bitmap imageBmp, String name, final UploadUserImageListener listener) {
        modelFirebase.uploadUserImage(imageBmp, name, listener);
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

    public interface UpdatePostListener extends addPostListener{}
    public void updatePost(final Post post, final UpdatePostListener listener){
        modelFirebase.updatePost(post,listener);
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

    public void refreshAllPosts(getAllPostsListener listener) {

        SharedPreferences sp = MyApplicaion.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
        Long lastUpdated = sp.getLong("lastUpdated", 0);
        modelFirebase.getAllPosts(lastUpdated, new getAllPostsListener() {
            @Override
            public void onComplete(List<Post> result) {

                long lastU = 0;
                for (Post p : result) {
                    ModelSql.instance.addPost(p, null);
                    if (p.getLastUpdated() > lastU)
                        lastU = p.getLastUpdated();
                }
                SharedPreferences.Editor editor = sp.edit();
                editor.putLong("lastUpdated", lastU);
                editor.commit();

                if (listener != null)
                    listener.onComplete(result);
            }
        });
    }
        public interface UploadPostImageListener extends Listener<String>{ }

        public void uploadPostImage(Bitmap imageBmp, String name, final Model.UploadPostImageListener listener) {
            modelFirebase.uploadPostImage(imageBmp, name, listener);
        }
    public interface DeletePostListener{
        public void onComplete();
    }
        public void deletePost(Post post){
        modelFirebase.deletePost(post, new DeleteListener() {
            @Override
            public void onComplete() {
                ModelSql.instance.deletePost(post,null);
            }
        });

        }

        public interface UpdateDeletedUsersListener extends Listener<String>{}
        public void updateDeletedPosts(UpdateDeletedUsersListener listener){

            SharedPreferences sp = MyApplicaion.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
            Long lastDeleted = sp.getLong("lastDeleted", 0);
            modelFirebase.getAllDeletedPosts(lastDeleted, new ModelFirebase.getAllPostsListener() {
                @Override
                public void onComplete(List<Post> result) {

                    long lastD = 0;
                    for (Post p : result) {
                        ModelSql.instance.deletePost(p, null);
                        if (p.getLastUpdated() > lastD)
                            lastD = p.getLastUpdated();

                    }
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putLong("lastDeleted", lastD);
                    editor.commit();
                    if (listener != null)
                        listener.onComplete(null);
                }
            });
        }


    public interface LatLongListener {
        void onComplete(List<Double> latitudePoint,List<Double> longitudePoints,List<String> gameIDS);
    }

    public void getLatLongPoint(LatLongListener listener)
    {
        ModelFirebase.getLatLong(listener);
    }
}
