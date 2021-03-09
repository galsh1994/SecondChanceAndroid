package com.example.secondchance;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secondchance.Model.Model;
import com.example.secondchance.Model.User;
import com.google.firebase.auth.FirebaseAuth;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class RegisterFragment extends Fragment {

    private FirebaseAuth mAuth;
    List<User> userList;
    Boolean userWasSaved;
    Boolean checkAllFields = false;
    EditText registerFirstName;
    EditText registerLastName;
    EditText registerEmail;
    EditText registerPassword;
    ImageButton registerEditProfilePhoto;
    ImageView registerProfilePhoto;
    Button saveRegister;
    EditText registerPhone;
    TextView message;
    TextView fieldsMSG;
    ProgressBar PB_register;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.non_buttons_menu,menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=  inflater.inflate(R.layout.fragment_register, container, false);

        //catch all buttons
        PB_register = view.findViewById(R.id.PB_register);
        PB_register.setVisibility(View.INVISIBLE);
        saveRegister = view.findViewById(R.id.register_btn);
        registerFirstName = view.findViewById(R.id.registerFirstName);
        registerFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) { Validation.hasText(registerFirstName); }
        });
        registerLastName= view.findViewById(R.id.registerLastName);
        registerLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) { Validation.hasText(registerLastName); }
        });
        registerEmail= view.findViewById(R.id.registerEmail);
        registerEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) { Validation.isEmailAddress(registerEmail,true); }
        });
        registerProfilePhoto= view.findViewById(R.id.registerProfilePhoto);
        registerEditProfilePhoto= view.findViewById(R.id.registerEditProfilePhoto);
        registerPassword= view.findViewById(R.id.registerPassword);
        registerPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) { Validation.isPassword(registerPassword,true); }
        });
        registerPhone = view.findViewById(R.id.registerPhone);
        registerPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) { Validation.isPhoneNumber(registerPhone,true); }
        });
        message=view.findViewById(R.id.register_message_text);
        message.setVisibility(view.INVISIBLE);
        fieldsMSG=view.findViewById(R.id.requiredDetails_editProfile);
        fieldsMSG.setVisibility(view.INVISIBLE);


       // get user list

        UserListViewModel userListViewModel=new ViewModelProvider(this).get(UserListViewModel.class);
        LiveData<List<User>> users =userListViewModel.getUserList();

        users.observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                userList=new LinkedList<>();
                for (User user:users) {
                    userList.add(user);
                }
                Model.instance.refreshData(null);
            }
        });

        registerEditProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editImage();
            }
        });

        saveRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                PB_register.setVisibility(View.VISIBLE);
                userWasSaved =true;
                checkAllFields=Validation.checkAllFieldsForUser(registerFirstName.getText().toString(),registerLastName.getText().toString(),registerEmail.getText().toString(),registerPassword.getText().toString(),registerPhone.getText().toString());

                if(!checkAllFields)
                { fieldsMSG.setVisibility(view.VISIBLE); }

                for (User user:userList) {
                    if(user.getEmail().equals(registerEmail.getText().toString())){
                        message.setVisibility(view.VISIBLE);
                        userWasSaved =false;
                        break;
                    }
                }
                 if(userWasSaved &&checkAllFields)
                saveChanges();
            }
        });

        return view;
    }

    private void saveChanges() {
        User user = new User();
        user.setUserID(registerEmail.getText().toString());
        user.setFirstName(registerFirstName.getText().toString());
        user.setLastName(registerLastName.getText().toString());
        user.setEmail(registerEmail.getText().toString());
        user.setPassword(registerPassword.getText().toString());
        user.setPhone("972"+(registerPhone.getText().toString()).substring(1));

        BitmapDrawable drawable = (BitmapDrawable)registerProfilePhoto.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        Model.instance.uploadUserImage(bitmap, user.getEmail(), new Model.UploadUserImageListener() {
            @Override
            public void onComplete(String url) {
                if (url == null){
                     displayFailedError();
                }else{
                    user.setPhotoUrl(url);

                    Model.instance.registerAuthFB(user, user.getPassword(), new Model.idSaverListener() {
                        @Override
                        public void onComplete(boolean flag,String id) {
                            if (flag) {
                                Navigation.findNavController(saveRegister).popBackStack();
                            }
                            else
                            {
                                PB_register.setVisibility(View.INVISIBLE);

                                Toast.makeText(getActivity(), "Your register failed", Toast.LENGTH_SHORT).show();
                             }
                            }
                    });

//                    Model.instance.addUser(user, new Model.addUserListener() {
//                        @Override
//                        public void onComplete() {
//                           Model.instance.refreshAllUsers(null);
//                            Navigation.findNavController(saveRegister).popBackStack();
//                        }
//                    });
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
                        registerProfilePhoto.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        try {
                            final Uri imageUri = data.getData();
                            final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            registerProfilePhoto.setImageBitmap(selectedImage);
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