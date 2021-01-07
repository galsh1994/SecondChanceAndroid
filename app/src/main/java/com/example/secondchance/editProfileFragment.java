package com.example.secondchance;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class editProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_edit_profile, container, false);

        Button saveDetailsBtn = view.findViewById(R.id.edit_details_on_profile);
//        saveDetailsBtn.setOnClickListener(new View.OnClickListener() {
    //        @Override
  //          public void onClick(View v) {
                //replace the details in the database
               // Navigation.findNavController(view).popBackStack();
      //      }
        //});

        return view;
    }
}