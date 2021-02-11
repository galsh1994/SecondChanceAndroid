package com.example.secondchance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secondchance.Model.Post;
import com.example.secondchance.Model.User;

import java.util.List;

public class postListAdapter extends RecyclerView.Adapter<postListViewHolder>{

    //this is post list but i am testing with users instead of posts
    //need to change

    List<Post> postList;
    public postListAdapter(List<Post> data){
        postList=data;
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
        holder.postUserName.setText(postList.get(position).getPostID());
        holder.postItemDescription.setText(postList.get(position).getDescription());
        holder.postDate.setText(postList.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        //return 3 is just for checking, after we use database ew need to change to data.lenget/size();
        if(postList==null)
            return 0;
        return postList.size();
    }
}
