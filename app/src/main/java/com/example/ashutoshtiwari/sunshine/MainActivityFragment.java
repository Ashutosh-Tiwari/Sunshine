package com.example.ashutoshtiwari.sunshine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivityFragment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceHolderFragment()).commit();
        }


    }
}
