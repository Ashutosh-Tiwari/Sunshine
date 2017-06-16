package com.example.ashutoshtiwari.sunshine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ashutosh.tiwari on 14/06/17.
 */

public class ForecastFragment extends Fragment {

    ListView forecastList;
    ArrayAdapter<String> mForecastAdapter;

    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;

    String forecastJsonString = null;

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

        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?zip=94043,us&mode=json&units=metric&cnt=7");

            //Create connection to openWeatherMap query
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Read inputstream into a string
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            forecastJsonString = buffer.toString();

        } catch (IOException e) {
            Log.e("ForecastFragment", "Error", e);
            return null;
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("ForecastFragment", "Error closing reader", e);
                }
        }
        return rootView;
    }
}
