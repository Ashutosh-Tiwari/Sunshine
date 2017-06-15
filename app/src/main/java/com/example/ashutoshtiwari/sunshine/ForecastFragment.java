package com.example.ashutoshtiwari.sunshine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ashutosh.tiwari on 14/06/17.
 */

public class ForecastFragment extends Fragment {

    ListView forecastList;

    ArrayAdapter<String> mForecastAdapter;


    public ForecastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_view, container, false);

        String forecastArray[] = {
                "Today - 88/63",
                "Tomorrow -66/76",
                "Wednesday - 55/65",
                "Thursday - 87/67",
                "Friday - 78/67",
                "Saturday - 90/78",
                "Sunday - 65/76"};

        List<String> weekForecast = new ArrayList<String>(Arrays.asList(forecastArray));

         mForecastAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, weekForecast);

        forecastList = (ListView) rootView.findViewById(R.id.listview_forecast);
        forecastList.setAdapter(mForecastAdapter);
        return rootView;
    }
}
