package com.example.ashutoshtiwari.sunshine;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecast_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            new FetchWeatherTask().execute("94043");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String forecastArray[] = {
                "Today - 88/63",
                "Tomorrow - 66/76",
                "Wednesday - 55/65",
                "Thursday - 87/67",
                "Friday - 78/67",
                "Saturday - 90/78",
                "Sunday - 65/76"};

        View rootView = inflater.inflate(R.layout.fragment_forecast, container, false);

        List<String> weekForecast = new ArrayList<String>(Arrays.asList(forecastArray));
        mForecastAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, weekForecast);

        forecastList = (ListView) rootView.findViewById(R.id.listview_forecast);
        forecastList.setAdapter(mForecastAdapter);

        forecastList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String forecast = mForecastAdapter.getItem(position);

                Intent intent = new Intent(getActivity(), DetailForecastItem.class)
                        .putExtra(Intent.EXTRA_TEXT, forecast);
                startActivity(intent);
            }
        });
        return rootView;
    }

    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        private final String TAG = FetchWeatherTask.class.getSimpleName();

        private String getReadableDateString(long time) {
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
            return shortenedDateFormat.format(time);
        }

        private String formatHighLows(double high, double low) {
            long roundedHigh = Math.round(high);
            long roundedLow = Math.round(low);
            String highLowStr = "";

            highLowStr = roundedHigh + "/" + roundedLow;
            return highLowStr;
        }

        private String[] getWeatherDataFromJson(String forecastJsonString, int numDays) {
            final String OWM_LIST = "list";
            final String OWM_WEATHER = "weather";
            final String OWM_TEMPRETURE = "temp";
            final String OWM_MAX = "max";
            final String OWM_MIN = "min";
            final String OWM_DESCRIPTION = "main";

            try {
                JSONObject forecastJson = new JSONObject(forecastJsonString);
                JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);


                Time dayTime = new Time();
                dayTime.setToNow();

                int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

                dayTime = new Time();
                String[] resultStrings = new String[numDays];

                for (int i = 0; i < weatherArray.length(); i++) {
                    String day;
                    String description;
                    String highAndLow;

                    JSONObject dayForecast = weatherArray.getJSONObject(i);

                    long dateTime;
                    dateTime = dayTime.setJulianDay(julianStartDay + i);
                    day = getReadableDateString(dateTime);

                    JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                    description = weatherObject.getString(OWM_DESCRIPTION);

                    JSONObject tempretureObject = dayForecast.getJSONObject(OWM_TEMPRETURE);

                    double high = tempretureObject.getDouble(OWM_MAX);
                    double low = tempretureObject.getDouble(OWM_MIN);

                    highAndLow = formatHighLows(high, low);
                    resultStrings[i] = day + " - " + description + " - " + highAndLow;
                }
                return resultStrings;

            } catch (JSONException e) {
                Log.e(TAG, "JSON ERROR: ", e);
            }


            return null;
        }

        @Override
        protected String[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String forecastJsonString = null;
            String format = "json";
            String units = "metric";
            int numDays = 7;

            try {
                final String baseUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String QUERY_PARAM = "q";
                final String FORMAT_PARAM = "mode";
                final String UNIT_PARAM = "units";
                final String DAYS_PARAM = "cnt";
                final String APPID_PARAM = "APPID";

                Uri builtUrl = Uri.parse(baseUrl).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNIT_PARAM, units)
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays)).appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                        .build();


                URL url = new URL(builtUrl.toString());
                Log.v(TAG, url.toString());

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
                Log.e(TAG, "Error", e);
                return null;
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }
            return getWeatherDataFromJson(forecastJsonString, numDays);
        }

        @Override
        protected void onPostExecute(String[] strings) {
            mForecastAdapter.clear();
            mForecastAdapter.addAll(strings);
        }
    }
}
