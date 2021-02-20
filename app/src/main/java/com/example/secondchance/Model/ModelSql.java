package com.example.secondchance.Model;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ModelSql {
    public final static ModelSql instance= new ModelSql();


    public LiveData<List<User>> getAllUsers() {
      return AppLocalDb.db.userDao().getAllUsers();
    }


    public interface addUserListener{
        void OnComplete();
    }
    public void addUser(User user, addUserListener listener) {
        class MyAsyncTask extends AsyncTask {
            List<User> users;
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDb.db.userDao().insertAll(user);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener!=null)
                    listener.OnComplete();
            }
        }
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }



    //////////////// posts ////////////////////////////


    public interface addPostListener{
        void OnComplete();
    }
    public void addPost(Post post, addPostListener listener) {
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDb.db.postDao().insertAll(post);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener!=null)
                    listener.OnComplete();
            }
        }
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }

    public LiveData<List<Post>> getAllPosts(){
        return AppLocalDb.db.postDao().getAllPosts();
    }

    public LiveData<List<Post>> getAllUserPosts(String userID){
        return AppLocalDb.db.postDao().getAllUserPosts(userID);
    }

    public void deletePost(Post post, Model.DeleteListener listener){
        AppLocalDb.db.postDao().delete(post);
        listener.onComplete();

    }


}
