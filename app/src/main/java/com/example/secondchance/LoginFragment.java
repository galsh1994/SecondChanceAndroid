package com.example.secondchance;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.lang.UScript;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.secondchance.Model.Model;
import com.example.secondchance.Model.Post;
import com.example.secondchance.Model.User;

import java.util.LinkedList;
import java.util.List;


public class LoginFragment extends Fragment {


    List<User> userList;
    boolean redirectToNewsFeed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);




        EditText email=view.findViewById(R.id.login_email_edit_text);
        EditText password=view.findViewById(R.id.login_password_edit_text);
        TextView loginMessage=view.findViewById(R.id.login_message);
        loginMessage.setVisibility(view.INVISIBLE);

        UserListViewModel userListViewModel=new ViewModelProvider(this).get(UserListViewModel.class);
        LiveData<List<User>> users =userListViewModel.getUserList();
        userList=new LinkedList<>();
        users.observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                for (User user:users) {
                    userList.add(user);
                }
            }
        });


        Button LogInCheck = view.findViewById(R.id.save_and_login_btn);
        LogInCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                redirectToNewsFeed=false;
                SharedPreferences sp= MyApplicaion.context.getSharedPreferences("Users", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();


                for (User user:userList) {

                    if(email.getText().toString().equals(user.getEmail())) {
                        if (password.getText().toString().equals(user.getPassword())) {
                            redirectToNewsFeed=true;
                            editor.putString("currentUser",user.getUserID());
                            editor.commit();
                            break;

                        }
                        else {
                            loginMessage.setText("wrong password");
                            break;

                        }

                    }
                    else {
                        loginMessage.setText("Invalid email");


                    }


                }

                loginMessage.setVisibility(view.VISIBLE);


                if(redirectToNewsFeed)
                    Navigation.findNavController(v).navigate(R.id.action_login_to_newsFeed);


            }
        });

        TextView GoToRegister = view.findViewById(R.id.go_to_register_from_login);
        GoToRegister.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_login_to_register));





        return view;
    }
}