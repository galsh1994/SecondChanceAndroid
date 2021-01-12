package com.example.secondchance;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

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

import static android.app.Activity.RESULT_OK;

public class editProfileFragment extends Fragment {

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
                save();
            }
        });

        Button saveDetailsBtn = view.findViewById(R.id.saveBtnEditPage);
        saveDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //replace the details in the database
                Navigation.findNavController(v).popBackStack();
            }
        });
 

        return view;
    }

    private void save() {
    }

    private void editImage() {
        Intent takePictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profilePhoto.setImageBitmap(imageBitmap);
        }
    }
}