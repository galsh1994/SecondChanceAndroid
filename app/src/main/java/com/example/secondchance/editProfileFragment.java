package com.example.secondchance;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.secondchance.Model.Model;
import com.example.secondchance.Model.User;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class editProfileFragment extends Fragment {
    //needs to be changed according to cookies- save changes does nothing!!!


    static final int REQUEST_IMAGE_CAPTURE = 1;
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText description;
    ImageView profilePhoto;
    ImageButton editProfile;
    Button save;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_edit_profile, container, false);

        firstName = view.findViewById(R.id.editTextFirstName);
        lastName= view.findViewById(R.id.editTextLastName);
        email= view.findViewById(R.id.editTextEmail);
        description= view.findViewById(R.id.editTextDescreption);
        profilePhoto= view.findViewById(R.id.profilePictureEditPage);
        editProfile= view.findViewById(R.id.editProfilePhoto);
        save = view.findViewById(R.id.saveBtnEditPage);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editImage();
            }
        });

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                saveChanges();
                Navigation.findNavController(v).popBackStack();
            }
        });

        return view;
    }

    private void saveChanges() {

    }
    private void editImage() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
    private void displayFailedError() {

    }

}