package com.example.secondchance;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.secondchance.Model.ModelFirebase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ModelFirebase mf = new ModelFirebase();
        mf.exampleToAcess();

    }




}