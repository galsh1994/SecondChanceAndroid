package com.example.secondchance;

import android.app.Activity;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.secondchance.Model.Model;
import com.example.secondchance.Model.Post;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class addPostFragment extends Fragment  {

    EditText description;
    EditText city;
    EditText condition;
    Button savePost;
    Button cancelPost;
    ImageButton EditPostPhoto;
    ImageView PostPhoto;
    String userID;
    View view;
    String postID;
    double coordinatesLatitude;
    double coordinatesLongitude;
    double liveLat=0.0;
    double liveLong=0.0;
    private static final String TAG = "addPostFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_post, container, false);
        userID = addPostFragmentArgs.fromBundle(getArguments()).getUserID();
        postID= String.valueOf(Math.random() * 10);
        description = view.findViewById(R.id.addPostDescription);
        city = view.findViewById(R.id.addPostLocation);
        condition = view.findViewById(R.id.addPostCondition);
        PostPhoto= view.findViewById(R.id.postPhoto);
        cancelPost =view.findViewById(R.id.cancel_post);
        MainActivity activity = (MainActivity) getActivity();
        liveLat = activity.getLatLoc();
        liveLong = activity.getLongLoc();

        Spinner dropdown = view.findViewById(R.id.spinner1);

        String[] items = new String[]{"Northern District","Haifa District", "Jerusalem District", "Central District","Southern District"};

        if (liveLat!=0.0 && liveLong!=0.0) {
            coordinatesLatitude = liveLat;
            coordinatesLongitude = liveLong;
            dropdown.setVisibility(View.INVISIBLE);
        }
        else {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
            dropdown.setAdapter(adapter);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            coordinatesLatitude = 33.082074259888685;
                            coordinatesLongitude = 35.10665117350627;
                            break;
                        case 1:
                            coordinatesLatitude = 32.79463018576074;
                            coordinatesLongitude = 34.98707025581802;
                            break;
                        case 2:
                            coordinatesLatitude = 31.765889791750478;
                            coordinatesLongitude = 35.20830599424469;
                            break;
                        case 3:
                            coordinatesLatitude = 32.13862263989835;
                            coordinatesLongitude = 34.84179248500992;
                            break;
                        case 4:
                            coordinatesLatitude = 30.62877503481801;
                            coordinatesLongitude = 34.76909538027209;
                            break;
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    coordinatesLatitude = 32.13862263989835;
                    coordinatesLongitude = 34.84179248500992;
                }
            });

        }
        cancelPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });
        EditPostPhoto= view.findViewById(R.id.EditPostPhoto);
        EditPostPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editImage();
            }
        });
        savePost = view.findViewById(R.id.save);
        savePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });



        return view;
    }

    private void saveChanges() {

        Post post = new Post();
        post.setPostID(postID);
        post.setDescription(description.getText().toString());
        post.setLocation(city.getText().toString());
        post.setCondition(condition.getText().toString());
        post.setUserID(userID);
        post.setCoordinatesLat(coordinatesLatitude);
        post.setCoordinatesLong(coordinatesLongitude);


        BitmapDrawable drawable = (BitmapDrawable) PostPhoto.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        Model.instance.uploadPostImage(bitmap, postID, new Model.UploadPostImageListener() {
            @Override
            public void onComplete(String url) {
                if (url == null) {
                    displayFailedError();
                } else {
                    post.setPhotoUrl(url);
                    Model.instance.addPost(post, new Model.addPostListener() {
                        @Override
                        public void onComplete() {
                            Model.instance.refreshAllPosts(null);
                            Navigation.findNavController(view).popBackStack();
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
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        PostPhoto.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                PostPhoto.setImageBitmap(BitmapFactory.decodeFile(picturePath));
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