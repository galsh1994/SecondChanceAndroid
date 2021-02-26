package com.example.secondchance;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.secondchance.Model.Model;
import com.example.secondchance.Model.Post;
import com.example.secondchance.Model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class profileFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;



    RecyclerView postList;
    UserListViewModel userListViewModel;
    PostListViewModel postListViewModel;
    TextView fullName;
    TextView email;
    TextView phone;
    ImageView profilePhoto;
    User currentUser;
    //Button deleteAccountBtn;
    Button editProfile;
    ImageButton mapMode;
    ImageButton whatAppBtn;
    String LoggedUserID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_profile, container, false);
        profilePhoto= view.findViewById(R.id.profile_user_img);
        fullName= view.findViewById(R.id.profile_FullName);
        email= view.findViewById(R.id.profile_email);
        phone= view.findViewById(R.id.profile_phone);
        //deleteAccountBtn=view.findViewById(R.id.delet_account_btn);
        whatAppBtn = view.findViewById(R.id.whatAppBtn);
        mapMode = view.findViewById(R.id.map_mode);
        whatAppBtn.setVisibility(View.INVISIBLE);
        editProfile= view.findViewById(R.id.edit_details_on_profile);
        editProfile.setVisibility(View.INVISIBLE);

        String ProfileUserID= profileFragmentArgs.fromBundle(getArguments()).getUserID();
        Log.d("TAG","user id is:"+ProfileUserID);
        SharedPreferences sp= MyApplicaion.context.getSharedPreferences("Users", Context.MODE_PRIVATE);
        LoggedUserID=sp.getString("currentUserID","0");

        userListViewModel = new ViewModelProvider(this).get(UserListViewModel.class);
        postListViewModel=new ViewModelProvider(this).get(PostListViewModel.class);

        postList=view.findViewById(R.id.profile_post_list);
        postList.hasFixedSize();

        LinearLayoutManager layoutmaneger = new LinearLayoutManager(this.getContext());
        postList.setLayoutManager(layoutmaneger);

        Model.instance.getUser(ProfileUserID, new Model.GetUserListener() {
            @Override
            public void onComplete(User user) {
                if (user.getPhotoUrl()!=null){
                    Picasso.get().load(user.getPhotoUrl()).into(profilePhoto);
                }
                if (ProfileUserID==LoggedUserID)
                {
                    editProfile.setVisibility(View.VISIBLE);
                    editProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            profileFragmentDirections.ActionProfileToEditProfile actionEdit =
                                    profileFragmentDirections.actionProfileToEditProfile(ProfileUserID);
                            Navigation.findNavController(v).navigate(actionEdit);
                        }
                    });
                }
                else{
                    whatAppBtn.setVisibility(View.VISIBLE);
                    whatAppBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url = "https://api.whatsapp.com/send?phone="+user.getPhone();
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);

                        }
                    });
                }
                fullName.setText(user.getFirstName()+" "+user.getLastName());
                email.setText(user.getEmail());
                phone.setText(user.getPhone());
                currentUser=user;

                /*
                deleteAccountBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Model.instance.deleteUser(currentUser);
                    }
                });*/

                postListAdapter adapter = new postListAdapter(postListViewModel.getUserPosts(ProfileUserID),userListViewModel.getUserList());
                postList.setAdapter(adapter);

                adapter.setOnItemClickListener(new postListAdapter.onItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        //TODO navigate to single post fragment

                        int size= postListViewModel.getUserPosts(ProfileUserID).getValue().size();
                        String postId = postListViewModel.getUserPosts(ProfileUserID).getValue().get(size-position-1).getPostID() ;
                        profileFragmentDirections.ActionProfileFragmentToSinglePostFragment actionProfileFragmentToSinglePostFragment=
                        profileFragmentDirections.actionProfileFragmentToSinglePostFragment(postId);
                        Navigation.findNavController(view).navigate(actionProfileFragmentToSinglePostFragment);

                    }
                });

                adapter.setUserNameUnClickable(true);

                userListViewModel.getUserList().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
                    @Override
                    public void onChanged(List<User> users) {
                        adapter.notifyDataSetChanged();
                    }
                });


                postListViewModel.getUserPosts(ProfileUserID).observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
                    @Override
                    public void onChanged(List<Post> posts) {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        //post list

//        postList=view.findViewById(R.id.profile_post_list);
//        postList.hasFixedSize();
//
//        LinearLayoutManager layoutmaneger = new LinearLayoutManager(this.getContext());
//        postList.setLayoutManager(layoutmaneger);
//
//        postListAdapter adapter = new postListAdapter();
//        postList.setAdapter(adapter);



        Button newsFeedFromProfile= view.findViewById(R.id.homeFrom_profile);
        newsFeedFromProfile.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_profileFragment_to_newsFeedFragment));

        Button profileToself= view.findViewById(R.id.visitProfileFrom_profile);
        profileToself.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileFragmentDirections.ActionProfileFragmentSelf actionProfileToSelf =
                        profileFragmentDirections.actionProfileFragmentSelf(LoggedUserID);
                Navigation.findNavController(v).navigate(actionProfileToSelf);
            }
        });

        mapMode.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_profileFragment_to_mapsFragment));

        Button addAPostProfileBtn= view.findViewById(R.id.addApost_profile);
        addAPostProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileFragmentDirections.ActionProfileFragmentToAddPostFragment actionAdd =
                        profileFragmentDirections.actionProfileFragmentToAddPostFragment(LoggedUserID);
                Navigation.findNavController(v).navigate(actionAdd);
            }
        });


        return view;
    }
}