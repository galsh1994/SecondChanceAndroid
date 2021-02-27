package com.example.secondchance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.secondchance.Model.Model;
import com.example.secondchance.Model.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    int LOCATION_REQUEST_CODE = 10001;
    NavController navController;
    FusedLocationProviderClient fusedLocationProviderClient;
    double longLoc;
    double latLoc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navController = Navigation.findNavController(this, R.id.mainactivity_navhost);
        NavigationUI.setupActionBarWithNavController(this, navController);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
        } else {
            askLocationPermission();
        }
    }

    private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                /// ask the user for permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);

            }

        }
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        }

        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location!=null)
                {

                    Log.d("TAG","onSuccess in main st"+location.toString());
                    Log.d("TAG","onSuccess in main long"+location.getLongitude());
                    Log.d("TAG","onSuccess in main lat"+location.getLatitude());
                    longLoc = location.getLongitude();
                    latLoc = location.getLatitude();


                }
                else
                {
                    Log.d("TAG","onSuccess: Location was null...");

                }

            }
        });
        locationTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Tag","OnFail"+e.getLocalizedMessage());
            }
        });


    }

    public Double getLongLoc ()
    {
        return longLoc;
    }

    public Double getLatLoc ()
    {
        return latLoc;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getLastLocation();
            }
            else {

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            navController.navigateUp();
            return true;
        }

        if (item.getItemId() == R.id.registerFragment){
            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);

            // set the message to display
            alertbox.setMessage("Are you sure you want to delete your account?");

            // add a neutral button to the alert box and assign a click listener
            alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                // click listener on the alert box
                public void onClick(DialogInterface arg0, int arg1) {
                    // the button was clicked
                    SharedPreferences sp= MyApplicaion.context.getSharedPreferences("Users", Context.MODE_PRIVATE);
                    String currentUserID = sp.getString("currentUserID", "0");
                    Log.d("TAG","The current user id is:"+currentUserID);
                    Model.instance.getUser(currentUserID, new Model.GetUserListener() {
                        @Override
                        public void onComplete(User user) {
                            Log.d("TAG","The current user in user is:"+user);
                            Log.d("TAG","The current user id in user is:"+user.getUserID());
                            Model.instance.deleteUser(user);

                        }
                    });
                     SharedPreferences.Editor editor=sp.edit();
                    editor.putString("currentUserID","0");
                    editor.putString("currentUserFirstName","0");
                    editor.putString("currentUserLastName","0");
                    editor.putString("currentUserEmail","0");
                    editor.putString("currentUserPhotoUrl","0");

                    editor.commit();

                    NavigationUI.onNavDestinationSelected(item,navController);

                }
            });

            alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {

                // click listener on the alert box
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();

                }
            });


            // show it
            alertbox.show();
            return true;
        }

        if (item.getItemId() == R.id.indexFragment){
            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);

            // set the message to display
            alertbox.setMessage("Are you sure you want to logout?");

            // add a neutral button to the alert box and assign a click listener
            alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                // click listener on the alert box
                public void onClick(DialogInterface arg0, int arg1) {
                    // the button was clicked
                    SharedPreferences sp= MyApplicaion.context.getSharedPreferences("Users", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putString("currentUserID","0");
                    editor.putString("currentUserFirstName","0");
                    editor.putString("currentUserLastName","0");
                    editor.putString("currentUserEmail","0");
                    editor.putString("currentUserPhotoUrl","0");

                    editor.commit();

                       NavigationUI.onNavDestinationSelected(item,navController);
                }
            });

            alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {

                // click listener on the alert box
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();

                }
            });


            // show it
            alertbox.show();
            return true;
        }

        return NavigationUI.onNavDestinationSelected(item,navController);
        //return super.onOptionsItemSelected(item);
    }
}