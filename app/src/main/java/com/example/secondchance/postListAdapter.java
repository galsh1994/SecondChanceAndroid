package com.example.secondchance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secondchance.Model.Post;
import com.example.secondchance.Model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class postListAdapter extends RecyclerView.Adapter<postListViewHolder>{


    onItemClickListener listener;
    LiveData<List<Post>> postList;
    public postListAdapter(LiveData<List<Post>> data){
        postList=data;
    }


    public void bindData(postListViewHolder holder,int position){

        ///TODO : add all the fieldS of the post to the view


        Post post= postList.getValue().get(postList.getValue().size()-position-1);
        holder.postUserName.setText(post.getPostID());
        holder.postItemDescription.setText(post.getDescription());
        holder.postItemLocation.setText(post.getLocation());
        holder.postDate.setText(String.valueOf(post.getLastUpdated()));
        holder.postItemCondition.setText(post.getCondition());
        holder.position=position;
        if (post.getPhotoUrl()!=null){
            Picasso.get().load(post.getPhotoUrl()).into(holder.postItemImage);
        }


    }


    @NonNull
    @Override
    public postListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_view,parent,false);
        postListViewHolder vh=new postListViewHolder(v);
        vh.listener=listener;

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull postListViewHolder holder, int position) {

        bindData(holder,position);
    }

    @Override
    public int getItemCount() {
        //return 3 is just for checking, after we use database ew need to change to data.lenget/size();
        if(postList.getValue()==null)
            return 0;
        return postList.getValue().size();
    }

    public interface onItemClickListener{
        void onClick(int position);
    }
    void setOnItemClickListener(onItemClickListener listener){
        this.listener=listener;
    }
}
