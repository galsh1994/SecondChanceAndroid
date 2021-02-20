package com.example.secondchance;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
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
import com.example.secondchance.Model.User;
import com.squareup.picasso.Picasso;

public class profileFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    RecyclerView postList;
    TextView fullName;
    TextView email;
    ImageView profilePhoto;
    User currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_profile, container, false);
        profilePhoto= view.findViewById(R.id.profile_user_img);
        fullName= view.findViewById(R.id.profile_FullName);
        email= view.findViewById(R.id.profile_email);

        String userID= profileFragmentArgs.fromBundle(getArguments()).getUserID();
        Log.d("TAG","user id is:"+userID);
        Model.instance.getUser(userID, new Model.GetUserListener() {
            @Override
            public void onComplete(User user) {
                if (user.getPhotoUrl()!=null){
                    Picasso.get().load(user.getPhotoUrl()).into(profilePhoto);
                }
                fullName.setText(user.getFirstName()+" "+user.getLastName());
                email.setText(user.getEmail());
                currentUser=user;
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
        newsFeedFromProfile.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_profileFragment_pop));

        Button profileToself= view.findViewById(R.id.visitProfileFrom_profile);
        profileToself.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_profileFragment_self));



        Button editItemBtn= view.findViewById(R.id.edit_item_btn);
        editItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileFragmentDirections.ActionProfileToEditItem actionEditItem = profileFragmentDirections.actionProfileToEditItem(currentUser.getUserID());
                Navigation.findNavController(v).navigate(actionEditItem);
            }
        });


        Button editProfileBtn= view.findViewById(R.id.edit_details_on_profile);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileFragmentDirections.ActionProfileToEditProfile actionEdit = profileFragmentDirections.actionProfileToEditProfile(currentUser.getUserID());
            Navigation.findNavController(v).navigate(actionEdit);
            }
        });


        Button addAPostProfileBtn= view.findViewById(R.id.addApost_profile);
        addAPostProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileFragmentDirections.ActionProfileFragmentToAddPostFragment actionAdd = profileFragmentDirections.actionProfileFragmentToAddPostFragment(currentUser.getUserID());
                Navigation.findNavController(view).navigate(actionAdd);
            }
        });
        Button messagesFromProfile= view.findViewById(R.id.messagesFrom_profile);
         messagesFromProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileFragmentDirections.ActionProfileFragmentToMessagesFragment actionMessages = profileFragmentDirections.actionProfileFragmentToMessagesFragment(currentUser.getUserID());
                Navigation.findNavController(view).navigate(actionMessages);
            }
        });
           ImageButton itemDetailsFromProfile= view.findViewById(R.id.item_details_From_profile);
        itemDetailsFromProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item_name = "Basket";
                profileFragmentDirections.ActionProfileToItemDetails action = profileFragmentDirections.actionProfileToItemDetails(currentUser.getUserID(),item_name);
                Navigation.findNavController(v).navigate(action);
            }
        });


        return view;
    }
}