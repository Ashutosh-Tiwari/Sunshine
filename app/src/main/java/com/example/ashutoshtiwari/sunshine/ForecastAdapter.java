package com.example.ashutoshtiwari.sunshine;

/*
  Created by Ashutosh.tiwari on 18/07/17.
  Cursor Adapter for the forecast data
 */

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */


class ForecastAdapter extends CursorAdapter {

    private static final int VIEW_TYPE_COUNT = 2;
    private final int VIEW_TYPE_TODAY = 0;
    private final int VIEW_TYPE_FUTURE_DAY = 1;
    private boolean mUseTodayLayout;


    ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    @Override
    public int getItemViewType(int position) {
        return (position == 0 && mUseTodayLayout) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    /*
            Remember that these views are reused as needed.
         */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;

        switch (viewType) {
            case VIEW_TYPE_TODAY: {
                layoutId = R.layout.list_item_forecast_today;
                break;
            }
            case VIEW_TYPE_FUTURE_DAY: {
                layoutId = R.layout.list_item_forecast;
                break;
            }
        }

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        view.setTag(myViewHolder);

        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        MyViewHolder myViewHolder = (MyViewHolder) view.getTag();

        int viewType = getItemViewType(cursor.getPosition());

        //Get weather icon based on the view type
        switch (viewType) {
            case VIEW_TYPE_TODAY: {
                myViewHolder.iconView.setImageResource(Utility.getArtResourceForWeatherCondition(
                        cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)
                ));
                break;
            }

            case VIEW_TYPE_FUTURE_DAY: {
                myViewHolder.iconView.setImageResource(Utility.getIconResourceForWeatherCondition(
                        cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)
                ));
                break;
            }

        }

        //Read weather date from cursor
        long dateInMilis = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
        myViewHolder.dateView.setText(Utility.getFriendlyDayString(context, dateInMilis));

        //Read weather forecast from cursor
        String description = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        myViewHolder.descriptionView.setText(description);
        myViewHolder.iconView.setContentDescription(description);

        //Read high temperature from cursor
        double high = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
        myViewHolder.highTempView.setText(Utility.formatTemperature(context, high));

        //Read low temperature from cursor
        double low = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
        myViewHolder.lowTempView.setText(Utility.formatTemperature(context, low));
    }

    private class MyViewHolder {
        ImageView iconView;
        TextView dateView;
        TextView descriptionView;
        TextView highTempView;
        TextView lowTempView;

        MyViewHolder(View view) {
            this.iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            this.dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
            this.descriptionView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
            this.highTempView = (TextView) view.findViewById(R.id.list_item_high_textview);
            this.lowTempView = (TextView) view.findViewById(R.id.list_item_low_textview);
        }
    }

    public void setUseTodayLayout(boolean useTodayLayout) {
        mUseTodayLayout = useTodayLayout;
    }
}
