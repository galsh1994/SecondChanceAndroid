package com.example.secondchance;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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

import com.example.secondchance.Model.Model;
import com.example.secondchance.Model.Post;
import com.squareup.picasso.Picasso;

import java.io.InputStream;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class EditItemFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Boolean postWasUpdated;
    Boolean checkAllFields = false;
    TextView fieldsMSG;
    EditText Description;
    EditText Condition;
    ImageView postPhoto;
    ImageButton editPhoto;
    Button save;
    Button cancel;
    String address;
    String tempPostID;
    String oldPostImageID;
    String currentPostID="0";
    Double postLat=0.0,postLong=0.0;
    Post currentPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_edit_item, container, false);
        save = view.findViewById(R.id.saveBtnEditPostPage);
        Description=view.findViewById(R.id.editDescription);
        Condition= view.findViewById(R.id.editTextCondition);
        postPhoto=view.findViewById(R.id.postPictureEditPage);
        editPhoto= view.findViewById(R.id.editProfilePhoto);
        fieldsMSG=  view.findViewById(R.id.requiredDetails_editProfile);
        fieldsMSG.setVisibility(View.INVISIBLE);
        cancel = view.findViewById(R.id.cancel_edit_post);
        currentPostID= EditItemFragmentArgs.fromBundle(getArguments()).getPostId();
        //show details before update
        Model.instance.getPost(currentPostID, new Model.GetPostListener() {
            @Override
            public void onComplete(Post post) {
                if (post.getPhotoUrl()!=null){
                    Picasso.get().load(post.getPhotoUrl()).into(postPhoto);
                }
                Description.setText(post.getDescription());
                Condition.setText(post.getCondition());
                address=post.getAddress();
                postLat= post.getCoordinatesLat();
                postLong=post.getCoordinatesLong();
                currentPost=post;
                oldPostImageID= post.getCondition()+post.getDescription();
            }
        });
        //validation for the updated fields
        Description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) { Validation.hasText(Description); }
        });

        Condition.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) { Validation.hasText(Condition); }
        });


        editPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editImage();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                postWasUpdated =true;
                checkAllFields=Validation.checkAllFieldsForPost(
                        Description.getText().toString(),
                        Condition.getText().toString());

                if(!checkAllFields) {
                    fieldsMSG.setVisibility(view.VISIBLE); }
                if(postWasUpdated &&checkAllFields) {
                    Model.instance.deletePostPhoto(oldPostImageID, null);
                    saveChanges();
                }
            }
        });
        return view;
    }


    private void saveChanges() {
        Post post = new Post();
        post.setPostID(currentPost.getPostID());
        Log.d("id:",currentPost.getPostID());
        post.setCondition(Condition.getText().toString());
        post.setDescription(Description.getText().toString());
        post.setUserID(currentPost.getUserID());
        post.setAddress(address);
        post.setCoordinatesLong(postLong);
        post.setCoordinatesLat(postLat);
        tempPostID = Condition.getText().toString()+Description.getText().toString();
        Log.d("tempush:",tempPostID);

        BitmapDrawable drawable = (BitmapDrawable)postPhoto.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        Model.instance.uploadPostImage(bitmap, tempPostID, new Model.UploadPostImageListener() {
            @Override
            public void onComplete(String url) {
                if (url == null){
                    displayFailedError();
                }else{
                    post.setPhotoUrl(url);
                    Model.instance.updatePost(post, new Model.UpdatePostListener() {
                        @Override
                        public void onComplete() {
                            Model.instance.refreshData(null);
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
                        postPhoto.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        try {
                            final Uri imageUri = data.getData();
                            final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            postPhoto.setImageBitmap(selectedImage);
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