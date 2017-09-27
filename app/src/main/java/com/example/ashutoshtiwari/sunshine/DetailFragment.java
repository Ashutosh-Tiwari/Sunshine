package com.example.ashutoshtiwari.sunshine;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ashutoshtiwari.sunshine.data.WeatherContract;

/**
 * Created by Ashutosh.tiwari on 19/06/17.
 * Detail List Item Fragment to show details related to individual weather item
 */

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = DetailFragment.class.getSimpleName();
    private static final String FORECAST_SHARE_HASHTAG = "#SunshineApp";
    public static final String DETAIL_URI = "URI";

    private static final String[] DETAIL_COLUMNS = {
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID
    };

    private static final int COLUMN_WEATHER_ID = 0;
    private static final int COLUMN_WEATHER_DATE = 1;
    private static final int COLUMN_WETHER_DESC = 2;
    private static final int COLUMN_WEATHER_MAX_TEMP = 3;
    private static final int COLUMN_WEATHER_MIN_TEMP = 4;
    private static final int COLUMN_WEATHER_HUMIDITY = 5;
    private static final int COLUMN_WEATHER_PRESSURE = 6;
    private static final int COLUMN_WEATHER_WIND_SPEED = 7;
    private static final int COLUMN_WEATHER_DEGREES = 8;
    private static final int COLUMN_WEATHER_CONDITION_ID = 9;

    private static final int DETAIL_LOADER = 1;


    private String mForecastJsonString;
    private Uri mUri;
    ShareActionProvider shareActionProvider;

    private ImageView mIconView;
    private TextView mFriendlyDateView;
    private TextView mDateView;
    private TextView mDescriptionView;
    private TextView mTempHighView;
    private TextView mTempLowView;
    private TextView mHumidityView;
    private TextView mWindView;
    private TextView mPressureView;

    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle args = getArguments();
        if (args != null) {
            mUri = args.getParcelable(DetailFragment.DETAIL_URI);
        }

        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        mIconView = (ImageView) view.findViewById(R.id.detail_icon);
        mDateView = (TextView) view.findViewById(R.id.detail_date_textview);
        mFriendlyDateView = (TextView) view.findViewById(R.id.detail_day_textview);
        mDescriptionView = (TextView) view.findViewById(R.id.detail_forecast_textview);
        mTempHighView = (TextView) view.findViewById(R.id.detail_high_textview);
        mTempLowView = (TextView) view.findViewById(R.id.detail_low_textview);
        mHumidityView = (TextView) view.findViewById(R.id.detail_humidity_textview);
        mWindView = (TextView) view.findViewById(R.id.detail_wind_textview);
        mPressureView = (TextView) view.findViewById(R.id.detail_pressure_textview);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_fragment, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);


        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if (shareActionProvider != null) {
            shareActionProvider.setShareIntent(createShareIntent());
        } else Log.e(TAG, "Share action provider is null!");
    }

    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastJsonString + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (mUri != null) {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null
            );
        } else {
            return null;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) {
            Log.d(TAG, "Cursor is empty!");
        }

        //Read weather condition id from the cursor
        int weatherId = data.getInt(COLUMN_WEATHER_CONDITION_ID);

        //Use a placeholder image
        mIconView.setImageResource(Utility.getArtResourceForWeatherCondition(weatherId));

        //Read weather date from cursor and update views for day of week and date
        long date = data.getLong(COLUMN_WEATHER_DATE);
        String friendlyDateText = Utility.getDayName(getActivity(), date);
        String dateText = Utility.getFormattedMonthDay(getActivity(), date);
        mFriendlyDateView.setText(friendlyDateText);
        mDateView.setText(dateText);

        //Read description from the cursor and update view
        String description = data.getString(COLUMN_WETHER_DESC);
        mDescriptionView.setText(description);
        mIconView.setContentDescription(description);

        //Read user preference for metric or imperial temperature unit
        boolean isMetric = Utility.isMetric(getActivity());

        //Read high & low temperatures from cursor and update the views
        String high = Utility.formatTemperature(getContext(), data.getDouble(COLUMN_WEATHER_MAX_TEMP));
        mTempHighView.setText(high);
        String low = Utility.formatTemperature(getContext(), data.getDouble(COLUMN_WEATHER_MIN_TEMP));
        mTempLowView.setText(low);

        //Read humidity from cursor and update the view
        float humidity = data.getFloat(COLUMN_WEATHER_HUMIDITY);
        mHumidityView.setText(getActivity().getString(R.string.format_humidity, humidity));

        //Read wind speed and direction from cursor and update the view
        float windSpeed = data.getFloat(COLUMN_WEATHER_WIND_SPEED);
        float windDir = data.getFloat(COLUMN_WEATHER_DEGREES);
        mWindView.setText(Utility.getFormattedWind(getActivity(), windSpeed, windDir));

        //Read pressure from the cursor and update the view
        float pressure = data.getFloat(COLUMN_WEATHER_PRESSURE);
        mPressureView.setText(getActivity().getString(R.string.format_pressure, pressure));

        //ForecastJsonString
        mForecastJsonString = String.format("%s - %s - %s/%s", dateText, description, high, low);


        if (shareActionProvider != null) {
            shareActionProvider.setShareIntent(createShareIntent());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void onLocationChanged(String location) {
        // replace the uri, since the location has changed
        Uri uri = mUri;

        if (uri != null) {
            long date = WeatherContract.WeatherEntry.getDateFromUri(uri);
            //Update the local copy of location URI
            mUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(location, date);
            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        }
    }
}