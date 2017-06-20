package com.example.ashutoshtiwari.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Ashutosh.tiwari on 19/06/17.
 */

public class DetailListItemFragment extends Fragment {

    private ActionProvider shareActionProvider;
    String forecastJsonString;

    public DetailListItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_detail_list_item, container, false);
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            forecastJsonString = intent.getStringExtra(Intent.EXTRA_TEXT);
            ((TextView) rootView.findViewById(R.id.textview_detail)).setText(forecastJsonString);
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = MenuItemCompat.getActionProvider(menuItem);
        //shareActionProvider.(new Intent().putExtra(Intent.EXTRA_TEXT, forecastJsonString.concat("#SunshineApp")));


    }
}
