package com.example.secondchance;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.secondchance.Model.Model;
import com.example.secondchance.Model.User;

import java.util.LinkedList;
import java.util.List;


public class LoginFragment extends Fragment {

    TextView forgotPassword;
    Button logInBtn;
    EditText email;
    EditText password;
    TextView loginMessage;
    TextView GoToRegister;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.non_buttons_menu,menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
        email=view.findViewById(R.id.login_email_edit_text);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) { Validation.isEmailAddress(email,true); }
        });
        password=view.findViewById(R.id.login_password_edit_text);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) { Validation.isPassword(password,true); }
        });
        loginMessage=view.findViewById(R.id.login_message);
        loginMessage.setVisibility(view.INVISIBLE);
        forgotPassword=view.findViewById(R.id.forget_password);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_forgetPasswordFragment);
            }
        });
        GoToRegister = view.findViewById(R.id.go_to_register_from_login);
        GoToRegister.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_login_to_register));

        logInBtn = view.findViewById(R.id.save_and_login_btn);
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailS = email.getText().toString();
                String passwordS = password.getText().toString();
                SharedPreferences sp = MyApplicaion.context.getSharedPreferences("Users", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                Model.instance.logInAuth(emailS,passwordS, new Model.idSaverListener() {
                    @Override
                    public void onComplete(boolean flag, String id) {
                        if (flag) {
                            editor.putString("currentUserID",id);
                            editor.commit();
                            loginMessage.setVisibility(view.INVISIBLE);
                            Navigation.findNavController(view).navigate(R.id.action_login_to_newsFeed);
                        } else {
                            loginMessage.setVisibility(view.VISIBLE);
                            loginMessage.setText("failed to log in");
                        }
                    }


                });
            }
        });
        return view;
    }
}