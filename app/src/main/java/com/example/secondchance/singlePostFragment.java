package com.example.secondchance;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.secondchance.Model.Model;
import com.example.secondchance.Model.Post;
import com.example.secondchance.Model.User;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Locale;

public class singlePostFragment extends Fragment {

    Button postUserName;
    TextView postDate;
    TextView postItemLocation;
    TextView postItemCondition;
    TextView postItemDescription;
    ImageView postUserImage;
    ImageView postItemImage;
    ImageButton postItemDelete;
    ImageButton postItemEdit;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_single_post, container, false);
        String postID= singlePostFragmentArgs.fromBundle(getArguments()).getPostId();

        SharedPreferences sp = MyApplicaion.context.getSharedPreferences("Users", Context.MODE_PRIVATE);
        String currentUserID = sp.getString("currentUserID", "0");


        postUserImage=view.findViewById(R.id.single_post_user_img);
        postUserName=view.findViewById(R.id.single_post_user_name);
        postDate=view.findViewById(R.id.single_post_date);
        postItemImage=view.findViewById(R.id.single_post_item_img);
        postItemDescription=view.findViewById(R.id.single_post_item_description);
        postItemLocation=view.findViewById(R.id.single_post_item_location);
        postItemCondition=view.findViewById(R.id.single_post_item_condotion);
        postItemDelete = view.findViewById(R.id.delete_single_Post_btn);
        postItemEdit = view.findViewById(R.id.edit_single_Post_btn);
        postItemEdit.setVisibility(View.INVISIBLE);
        postItemDelete.setVisibility(View.INVISIBLE);


        Model.instance.getPost(postID, new Model.GetPostListener() {
            @Override
            public void onComplete(Post post) {

                Model.instance.getUser(post.getUserID(), new Model.GetUserListener() {
                    @Override
                    public void onComplete(User PostWriterUser) {
                        postUserName.setText(PostWriterUser.getFirstName()+" "+PostWriterUser.getLastName());
                        if (PostWriterUser.getPhotoUrl() != null) {
                            Picasso.get().load(PostWriterUser.getPhotoUrl()).into(postUserImage);
                        }
                        if (currentUserID.equals(PostWriterUser.getUserID())){
                            postItemEdit.setVisibility(View.VISIBLE);
                            postItemDelete.setVisibility(View.VISIBLE);
                        }
                        postUserName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                singlePostFragmentDirections.ActionSinglePostFragmentToProfileFragment actionToProfile =
                                        singlePostFragmentDirections.actionSinglePostFragmentToProfileFragment(PostWriterUser.getUserID());
                                Navigation.findNavController(v).navigate(actionToProfile);
                            }
                        });

                        if (post.getPhotoUrl()!=null){
                            Picasso.get().load(post.getPhotoUrl()).into(postItemImage);
                        }
                        postItemDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Model.instance.deletePost(post);
                                Navigation.findNavController(v).popBackStack();
                            }
                        });
                        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                        cal.setTimeInMillis(post.getLastUpdated() * 1000);
                        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
                        String  hours = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
                        String  minutes = String.valueOf(cal.get(Calendar.MINUTE));
                        postDate.setText(date+" "+hours+":"+minutes);
                        postItemDescription.setText("Description: "+post.getDescription());
                        postItemLocation.setText("Address: "+post.getLocation());
                        postItemCondition.setText("Condition: "+post.getCondition());
                    }
                });

            }
        });



        postItemEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singlePostFragmentDirections.ActionSinglePostFragmentToEditItemFragment actionToEdit =
                        singlePostFragmentDirections.actionSinglePostFragmentToEditItemFragment(postID);
                Navigation.findNavController(v).navigate(actionToEdit);

            }
        });

        return view;
    }
}