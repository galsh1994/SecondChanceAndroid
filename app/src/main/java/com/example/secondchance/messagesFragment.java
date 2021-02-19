package com.example.secondchance;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class messagesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_messages, container, false);

        String userID= profileFragmentArgs.fromBundle(getArguments()).getUserID();
        Log.d("TAG","user id is:"+userID);

//        Button privateMsgBtn = view.findViewById(R.id.private_m_button);
//        privateMsgBtn.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//        Navigation.findNavController(v).navigate(R.id.action_messages_to_private_messages);
//         }
//        });



        return view;
    }
}