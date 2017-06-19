package com.example.ashutoshtiwari.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Ashutosh.tiwari on 19/06/17.
 */

public class DetailListItemFragment extends Fragment {

    public DetailListItemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_detail_list_item, container, false);
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String forecastJsonString = intent.getStringExtra(Intent.EXTRA_TEXT);
            ((TextView)rootView.findViewById(R.id.textview_detail)).setText(forecastJsonString);
        }
        return rootView;
    }
}
