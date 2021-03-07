package com.example.secondchance;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.secondchance.Model.Model;


public class forgetPasswordFragment extends Fragment {

    Button resetBtn;
    EditText email;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forget_password, container, false);
        resetBtn = view.findViewById(R.id.reset_btn);
        email = view.findViewById(R.id.email_forgot);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString();
                if (userEmail.equals("")) {
                    Toast.makeText(getActivity(), "You must enter Email", Toast.LENGTH_SHORT).show();
                } else {
                    Model.instance.resetPass(userEmail, new Model.SuccessListener() {
                        @Override
                        public void onComplete(boolean result) {
                            if (result) {
                                Navigation.findNavController(view).navigate(R.id.action_forgetPasswordFragment_to_indexFragment);
                            } else {
                                Toast.makeText(getActivity(), "Failed to send email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        return view;


    }


    }