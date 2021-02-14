package com.example.secondchance;

import android.view.View;
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
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(position);
            }
        });


    }
}
