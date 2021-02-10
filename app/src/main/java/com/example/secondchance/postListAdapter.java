package com.example.secondchance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secondchance.Model.User;

import java.util.List;

public class postListAdapter extends RecyclerView.Adapter<postListViewHolder>{

    //this is post list but i am testing with users instead of posts
    //need to change

    List<User> userList;
    public postListAdapter(List<User> data){
        userList=data;
    }


    @NonNull
    @Override
    public postListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_view,parent,false);
        postListViewHolder vh=new postListViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull postListViewHolder holder, int position) {
        //here we need to update the data of each member in the holder,this is just a test
        holder.postUserName.setText(userList.get(position).getFirstName());
        holder.postItemDescription.setText(userList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        //return 3 is just for checking, after we use database ew need to change to data.lenget/size();
        if(userList==null)
            return 0;
        return userList.size();
    }
}
