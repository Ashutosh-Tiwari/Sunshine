package com.example.ashutoshtiwari.sunshine;

import android.content.Intent;
import android.database.Cursor;
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
import android.widget.TextView;

import com.example.ashutoshtiwari.sunshine.data.WeatherContract;

/**
 * Created by Ashutosh.tiwari on 19/06/17.
 * Detail List Item Fragment to show details related to individual weather item
 */

public class DetailListItemFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = DetailListItemFragment.class.getSimpleName();
    private static final String FORECAST_SHARE_HASHTAG = "#SunshineApp";

    private static final int DETAIL_LOADER = 1;

    private static final String[] FORECAST_COLUMNS = {
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP
    };

    private static final int COLUMN_WEATHER_ID = 0;
    private static final int COLUMN_WEATHER_DATE = 1;
    private static final int COLUMN_WETHER_DESC = 2;
    private static final int COLUMN_WEATHER_MAX_TEMP = 3;
    private static final int COLUMN_WEATHER_MIN_TEMP = 4;

    String mForecastJsonString;

    ShareActionProvider shareActionProvider;

    public DetailListItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_detail_list_item, container, false);
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

        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }

        return new CursorLoader(
                getActivity(),
                intent.getData(),
                FORECAST_COLUMNS,
                null,
                null,
                null);
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

        boolean isMetric = Utility.isMetric(getActivity());

        String dateString = Utility.formatDate(data.getLong(COLUMN_WEATHER_DATE));
        String weatherDecription = data.getString(COLUMN_WETHER_DESC);
        String high = Utility.formatTemperature(data.getDouble(COLUMN_WEATHER_MAX_TEMP), isMetric);
        String low = Utility.formatTemperature(data.getDouble(COLUMN_WEATHER_MIN_TEMP), isMetric);

        mForecastJsonString = String.format("%s - %s - %s/%s", dateString, weatherDecription, high, low);
        TextView textViewDetail = (TextView) getView().findViewById(R.id.textview_detail);
        textViewDetail.setText(mForecastJsonString);

        if (shareActionProvider != null) {
            shareActionProvider.setShareIntent(createShareIntent());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
