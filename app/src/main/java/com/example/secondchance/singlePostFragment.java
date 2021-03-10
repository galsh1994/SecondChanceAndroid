package com.example.secondchance;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secondchance.Model.Model;
import com.example.secondchance.Model.Post;
import com.example.secondchance.Model.User;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Locale;

public class singlePostFragment extends Fragment {

    Button postUserName;
    TextView postDate;
    TextView postItemCity;
    TextView postItemCondition;
    TextView postItemDescription;
    ImageView postUserImage;
    ImageView postItemImage;
    ImageButton postItemDelete;
    ImageButton postItemEdit;
    Button whatAppBtn;
    Button VisitProfile;
    String postPhotoID;
    ProgressBar PB_singlePost;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_single_post, container, false);
        String postID= singlePostFragmentArgs.fromBundle(getArguments()).getPostId();

        SharedPreferences sp = MyApplicaion.context.getSharedPreferences("Users", Context.MODE_PRIVATE);
        String currentUserID = sp.getString("currentUserID", "0");
         PB_singlePost = view.findViewById(R.id.PB_singlePost);
        PB_singlePost.setVisibility(View.VISIBLE);
        whatAppBtn = view.findViewById(R.id.whatAppBtn);
        postUserImage=view.findViewById(R.id.single_post_user_img);
        postUserName=view.findViewById(R.id.single_post_user_name);
        postDate=view.findViewById(R.id.single_post_date);
        postItemImage=view.findViewById(R.id.single_post_item_img);
        postItemDescription=view.findViewById(R.id.single_post_item_description);
        postItemCity=view.findViewById(R.id.single_post_item_city);
        postItemCondition=view.findViewById(R.id.single_post_item_condotion);
        postItemDelete = view.findViewById(R.id.delete_single_Post_btn);
        postItemEdit = view.findViewById(R.id.edit_single_Post_btn);
        VisitProfile= view.findViewById(R.id.moveToProfile);
        postItemEdit.setVisibility(View.INVISIBLE);
        postItemDelete.setVisibility(View.INVISIBLE);
        VisitProfile.setVisibility(View.INVISIBLE);
        whatAppBtn.setVisibility(View.INVISIBLE);


        Model.instance.getPost(postID, new Model.GetPostListener() {
            @Override
            public void onComplete(Post post) {
                Model.instance.getUser(post.getUserID(), new Model.GetUserListener() {
                    @Override
                    public void onComplete(User PostWriterUser) {
                        PB_singlePost.setVisibility(View.INVISIBLE);
                        postUserName.setText(PostWriterUser.getFirstName()+" "+PostWriterUser.getLastName());
                        if (PostWriterUser.getPhotoUrl() != null) {
                            Picasso.get().load(PostWriterUser.getPhotoUrl()).into(postUserImage);
                        }
                        if (currentUserID.equals(PostWriterUser.getUserID())){

                            postItemEdit.setVisibility(View.VISIBLE);
                            postItemDelete.setVisibility(View.VISIBLE);
                        }
                        else{
                            VisitProfile.setVisibility(View.VISIBLE);
                            whatAppBtn.setVisibility(View.VISIBLE);
                        }
                        VisitProfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                singlePostFragmentDirections.ActionSinglePostFragmentToProfileFragment actionToProfile =
                                        singlePostFragmentDirections.actionSinglePostFragmentToProfileFragment(PostWriterUser.getUserID());
                                Navigation.findNavController(v).navigate(actionToProfile);
                            }
                        });
                        whatAppBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String msg = "Hello, I'm interested in your item";
                                String num = PostWriterUser.getPhone();
                                String url = "https://api.whatsapp.com/send?phone="+num+"&text="+msg;
                                 Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            }
                        });
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

                        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                        cal.setTimeInMillis(post.getLastUpdated() * 1000);
                        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
                        String  hours = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
                        String  minutes = String.valueOf(cal.get(Calendar.MINUTE));
                        if(minutes.length()==1)
                        {
                            minutes = "0"+minutes;
                        }
                        postDate.setText(date+" "+hours+":"+minutes);
                        postItemDescription.setText("Description: "+post.getDescription());
                        postItemCity.setText("Address: "+post.getAddress());
                        postItemCondition.setText("Condition: "+post.getCondition());
                        postPhotoID= post.getCondition()+post.getDescription();
                        postItemDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Model.instance.deletePost(post);
                                Model.instance.refreshData(new Model.refreshListener() {
                                    @Override
                                    public void onComplete() {
                                        Navigation.findNavController(postItemDelete).popBackStack();
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onFail(String err) {
                        Toast.makeText(getActivity(), "Content is no longer available", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(view).popBackStack();
                    }
                });

            }

            @Override
            public void onFail(String err) {
                Toast.makeText(getActivity(), "Content is no longer available", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).popBackStack();
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