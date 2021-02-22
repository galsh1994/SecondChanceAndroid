package com.example.secondchance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import com.example.secondchance.Model.ModelFirebase;

public class MainActivity extends AppCompatActivity {

    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navController = Navigation.findNavController(this, R.id.mainactivity_navhost);
        NavigationUI.setupActionBarWithNavController(this,navController);

    }

     @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.main,menu);
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            navController.navigateUp();
            return true;
        }

        if (item.getItemId() == R.id.menu_logout){
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

                    //TODO navigate or pop back to index fragment


                }
            });

            alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {

                // click listener on the alert box
                public void onClick(DialogInterface arg0, int arg1) {

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