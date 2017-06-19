package com.example.ashutoshtiwari.sunshine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailForecastItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_forecast_item);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.detail_item_container, new DetailListItemFragment()).commit();
        }
    }
}
