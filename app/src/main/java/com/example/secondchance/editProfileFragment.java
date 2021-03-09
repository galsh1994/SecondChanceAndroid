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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.secondchance.Model.AppLocalDb;
import com.example.secondchance.Model.Model;
import com.example.secondchance.Model.ModelSql;
import com.example.secondchance.Model.User;
import com.example.secondchance.Model.UserDao;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class editProfileFragment extends Fragment {
    //needs to be changed according to cookies- save changes does nothing!!!


    static final int REQUEST_IMAGE_CAPTURE = 1;
    List<User> userList;
    Boolean userWasSaved;
    Boolean checkAllFields = false;
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText phoneNumber;
    ImageView profilePhoto;
    ImageButton editProfile;
    Button save;
    Button cancel;
    String currentUserID="0";
    User currentUser;
    TextView message;
    TextView fieldsMSG;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=  inflater.inflate(R.layout.fragment_edit_profile, container, false);

        firstName = view.findViewById(R.id.editTextFirstName);
        lastName= view.findViewById(R.id.editTextLastName);
        email= view.findViewById(R.id.editTextEmail);
        phoneNumber= view.findViewById(R.id.editTextPhone);
        profilePhoto= view.findViewById(R.id.profilePictureEditPage);
        editProfile= view.findViewById(R.id.editProfilePhoto);
        save = view.findViewById(R.id.saveBtnEditPage);
        cancel = view.findViewById(R.id.cancel_post_profile);
        message=view.findViewById(R.id.edit_profile_message_text);
        message.setVisibility(view.INVISIBLE);
        fieldsMSG=view.findViewById(R.id.requiredDetails_editProfile);
        fieldsMSG.setVisibility(View.INVISIBLE);
        currentUserID= editProfileFragmentArgs.fromBundle(getArguments()).getUserId();

        //show the details of the post before update
        Model.instance.getUser(currentUserID, new Model.GetUserListener() {
            @Override
            public void onComplete(User user) {
                if (user.getPhotoUrl()!=null){
                    Picasso.get().load(user.getPhotoUrl()).into(profilePhoto);
                }
                firstName.setText(user.getFirstName());
                lastName.setText(user.getLastName());
                email.setText(user.getEmail());
                phoneNumber.setText("0"+user.getPhone().substring(3));
                currentUser=user;
            }
        });
        //validation for the updated fields
        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                Validation.hasText(firstName);
            }
        });
        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                Validation.hasText(lastName);
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) { Validation.isEmailAddress(email,true); }
        });
        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) { Validation.isPhoneNumber(phoneNumber,true); }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editImage();
            }
        });
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


        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                userWasSaved = true;
                checkAllFields = Validation.checkAllFieldsForUser(
                        firstName.getText().toString(),
                        lastName.getText().toString(),
                        email.getText().toString(),
                        currentUser.getPassword(),
                        phoneNumber.getText().toString());
                if (!checkAllFields) {
                    fieldsMSG.setVisibility(view.VISIBLE);
                }

                for (User user : userList) {
                    if (user.getEmail().equals(email.getText().toString())&& !user.getUserID().equals(currentUserID)) {
                        message.setVisibility(view.VISIBLE);
                        userWasSaved = false;
                        break;
                    }
                }
                if (userWasSaved && checkAllFields) {
                    Model.instance.deleteUserPhoto(email.getText().toString(), null);
                    saveChanges();
                }
            }
            });
        return view;
    }

    private void saveChanges() {
        User user = new User();
        user.setUserID(currentUserID);
        user.setFirstName(firstName.getText().toString());
        user.setLastName(lastName.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPhone("972"+(phoneNumber.getText().toString()).substring(1));
        user.setPassword(currentUser.getPassword());

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
                        }
                    });
                    Navigation.findNavController(save).popBackStack();

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
                        try {
                            final Uri imageUri = data.getData();
                            final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            profilePhoto.setImageBitmap(selectedImage);
                        } catch (Exception e) {
                            e.printStackTrace();
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