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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.secondchance.Model.Model;
import com.example.secondchance.Model.User;

import java.util.LinkedList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class RegisterFragment extends Fragment {

    List<User> userList;
    Boolean saveUser;

    EditText registerFirstName;
    EditText registerLastName;
    EditText registerEmail;
    EditText registerDescription;
    EditText registerPassword;
    ImageButton registerEditProfilePhoto;
    ImageView registerProfilePhoto;
    Button saveRegister;
    EditText registerPhone;
    TextView message;
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=  inflater.inflate(R.layout.fragment_register, container, false);

        //catch all buttons

        saveRegister = view.findViewById(R.id.register_btn);
       registerFirstName = view.findViewById(R.id.registerFirstName);
       registerLastName= view.findViewById(R.id.registerLastName);
       registerEmail= view.findViewById(R.id.registerEmail);
       registerDescription= view.findViewById(R.id.registerDescreption);
       registerProfilePhoto= view.findViewById(R.id.registerProfilePhoto);
       registerEditProfilePhoto= view.findViewById(R.id.registerEditProfilePhoto);
       registerPassword= view.findViewById(R.id.registerPassword);
       registerPhone = view.findViewById(R.id.registerPhone);
       message=view.findViewById(R.id.register_message_text);
       message.setVisibility(view.INVISIBLE);



       // get user list

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
        Log.d("before" ,registerPhone.getText().toString());
        // replace the phone number to the right format
        Editable checkPhone = registerPhone.getText();
        String checkPhoneStr= checkPhone.toString();
        if (checkPhoneStr.startsWith("+"))
        {
            checkPhoneStr = checkPhoneStr.substring(1);
        }
        if (checkPhoneStr.startsWith("0"))
        {
            checkPhoneStr = checkPhoneStr.replace("0","972");
        }
        registerPhone.setText(checkPhoneStr);
        Log.d("after" ,registerPhone.getText().toString());

        registerEditProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editImage();
            }
        });

        saveRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                saveUser=true;

                for (User user:userList) {
                    if(user.getEmail().equals(registerEmail.getText().toString())){
                        message.setVisibility(view.VISIBLE);
                        saveUser=false;
                        break;
                    }
                }
                if(saveUser)
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
        user.setPhone(registerPhone.getText().toString());


        BitmapDrawable drawable = (BitmapDrawable)registerProfilePhoto.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        Model.instance.uploadUserImage(bitmap, user.getUserID(), new Model.UploadUserImageListener() {
            @Override
            public void onComplete(String url) {
                if (url == null){
                     displayFailedError();
                }else{
                    user.setPhotoUrl(url);
                    Model.instance.addUser(user, new Model.addUserListener() {
                        @Override
                        public void onComplete() {
                           Model.instance.refreshAllUsers(null);
                            Navigation.findNavController(saveRegister).popBackStack();
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
                        registerProfilePhoto.setImageBitmap(selectedImage);
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
                                registerProfilePhoto.setImageBitmap(BitmapFactory.decodeFile(picturePath));
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