package com.example.secondchance.Model;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ModelSql {
    public final static ModelSql instance= new ModelSql();

    public interface getAllUsersListener{
        void OnComplete(List<User> users);
    }
    public void getAllUsers(getAllUsersListener listener) {
        class MyAsyncTask extends AsyncTask {
            List<User> users;
            @Override
            protected Object doInBackground(Object[] objects) {
                users = AppLocalDb.db.userDao().getAllUsers();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                listener.OnComplete(users);
            }
        }
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
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


}
