package com.example.secondchance;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secondchance.Model.Model;
import com.example.secondchance.Model.Post;
import com.example.secondchance.Model.User;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class postListAdapter extends RecyclerView.Adapter<postListViewHolder>{


    onItemClickListener listener;
    LiveData<List<Post>> postList;
    LiveData<List<User>> users;
    Boolean userNameClickable;

    public postListAdapter(LiveData<List<Post>> data, LiveData<List<User>> userList){
        postList=data;
        users=userList;
        userNameClickable=true;

    }


    public void bindData(postListViewHolder holder,int position) {

        ///TODO : add all the fieldS of the post to the view

        SharedPreferences sp = MyApplicaion.context.getSharedPreferences("Users", Context.MODE_PRIVATE);
        String currentUserID = sp.getString("currentUserID", "0");

        // get the post
        Post post = postList.getValue().get(postList.getValue().size() - position - 1);

        Model.instance.getUser(post.getUserID(), new Model.GetUserListener() {
            @Override
            public void onComplete(User PostWriterUser) {
                holder.postUserName.setText(PostWriterUser.getFirstName()+" "+PostWriterUser.getLastName());
                if (PostWriterUser.getPhotoUrl() != null) {
                    Picasso.get().load(PostWriterUser.getPhotoUrl()).into(holder.postUserImage);
                }
                if (userNameClickable) {
                    holder.postUserName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            newsFeedFragmentDirections.ActionNewsFeedFragmentToProfileFragment actionToUsersProfile =
                                    newsFeedFragmentDirections.actionNewsFeedFragmentToProfileFragment(post.getUserID());
                            Navigation.findNavController(v).navigate(actionToUsersProfile);
                        }
                    });
                }
            }
        });
        // set username in the holder


        // hide or display the edit and delete options ,depends on permissions


      // set post photo
        if (post.getPhotoUrl()!=null){
            Picasso.get().load(post.getPhotoUrl()).into(holder.postItemImage);
        }

        //set the date
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(post.getLastUpdated() * 1000);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        String  hours = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        String  minutes = String.valueOf(cal.get(Calendar.MINUTE));
        holder.postDate.setText(date+" "+hours+":"+minutes);

        Log.d("k","p"+post.getPostID());
        //set rest of the fields
        holder.postItemDescription.setText("Description: "+post.getDescription());
        holder.postItemCondition.setText("Condition: "+post.getCondition());
        holder.postItemCity.setText("Address: "+post.getCity());
        holder.position=position;


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

    public void setUserNameUnClickable(Boolean cond){
        userNameClickable=!cond;
    }}
