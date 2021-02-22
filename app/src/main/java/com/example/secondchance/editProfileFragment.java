package com.example.secondchance;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.secondchance.Model.AppLocalDb;
import com.example.secondchance.Model.Model;
import com.example.secondchance.Model.ModelSql;
import com.example.secondchance.Model.User;
import com.example.secondchance.Model.UserDao;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class editProfileFragment extends Fragment {
    //needs to be changed according to cookies- save changes does nothing!!!


    static final int REQUEST_IMAGE_CAPTURE = 1;
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText phoneNumber;
    EditText password;
    ImageView profilePhoto;
    ImageButton editProfile;
    Button save;
    String currentUserID="0";
    User currentUser;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=  inflater.inflate(R.layout.fragment_edit_profile, container, false);

        firstName = view.findViewById(R.id.editTextFirstName);
        lastName= view.findViewById(R.id.editTextLastName);
        email= view.findViewById(R.id.editTextEmail);
        phoneNumber= view.findViewById(R.id.editTextPhone);
        password= view.findViewById(R.id.editTextPassword);
        profilePhoto= view.findViewById(R.id.profilePictureEditPage);
        editProfile= view.findViewById(R.id.editProfilePhoto);
        save = view.findViewById(R.id.saveBtnEditPage);

        currentUserID= editProfileFragmentArgs.fromBundle(getArguments()).getUserId();



        Model.instance.getUser(currentUserID, new Model.GetUserListener() {
            @Override
            public void onComplete(User user) {
                if (user.getPhotoUrl()!=null){
                    Picasso.get().load(user.getPhotoUrl()).into(profilePhoto);
                }
                firstName.setText(user.getFirstName());
                lastName.setText(user.getLastName());
                email.setText(user.getEmail());
                phoneNumber.setText(user.getPhone());
                password.setText(user.getPassword());
                currentUser=user;
            }
        });



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
                Model.instance.refreshAllUsers(null);
                editProfileFragmentDirections.ActionEditProfileFragmentToProfileFragment actionToUpdatedProfile =
                        editProfileFragmentDirections.actionEditProfileFragmentToProfileFragment(currentUserID);
                Navigation.findNavController(v).navigate(actionToUpdatedProfile);            }

            });

        return view;
    }

    private void saveChanges() {
        User user = new User();
        user.setUserID(currentUserID);
        user.setFirstName(firstName.getText().toString());
        user.setLastName(lastName.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPhone(phoneNumber.getText().toString());
        user.setPassword(password.getText().toString());

        BitmapDrawable drawable = (BitmapDrawable)profilePhoto.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        Model.instance.uploadUserImage(bitmap, user.getUserID(), new Model.UploadUserImageListener() {
            @Override
            public void onComplete(String url) {
                if (url == null){
                    displayFailedError();
                }else{
                    user.setPhotoUrl(url);
                    Model.instance.updateUser(user, new Model.UpdateUserListener() {
                        @Override
                        public void onComplete() {
                            Model.instance.refreshAllUsers(null);
                            Navigation.findNavController(save).popBackStack();
                        }
                    });
                }
            }
        });
    }
    private void editImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose your profile picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        profilePhoto.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage =  data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                profilePhoto.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }
    private void displayFailedError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Operation Failed");
        builder.setMessage("Saving image failed, please try again later...");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
}