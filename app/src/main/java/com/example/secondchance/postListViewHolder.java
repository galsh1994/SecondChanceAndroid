package com.example.secondchance;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class postListViewHolder extends RecyclerView.ViewHolder {

    TextView postUserName;
    TextView postDate;
    TextView postItemLocation;
    TextView postItemCondition;
    TextView postItemDescription;
    ImageView postUserImage;
    ImageView postItemImage;
    ImageButton postItemDelete;
    ImageButton postItemEdit;
    int position;
    postListAdapter.onItemClickListener listener;


    public postListViewHolder(@NonNull View itemView) {
        super(itemView);

        postUserImage=itemView.findViewById(R.id.post_user_img);
        postUserName=itemView.findViewById(R.id.post_user_name);
        postDate=itemView.findViewById(R.id.post_date);
        postItemImage=itemView.findViewById(R.id.post_item_img);
        postItemDescription=itemView.findViewById(R.id.post_item_description);
        postItemLocation=itemView.findViewById(R.id.post_item_location);
        postItemCondition=itemView.findViewById(R.id.post_item_condotion);
        postItemDelete = itemView.findViewById(R.id.deletePost_btn);
        postItemEdit = itemView.findViewById(R.id.editPost_btn);
        postItemEdit.setVisibility(View.INVISIBLE);
        postItemDelete.setVisibility(View.INVISIBLE);
        postItemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete func
            }
        });

        postItemEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edit func
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(position);
            }
        });


    }
}
