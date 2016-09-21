package com.example.android.sunshine.app;

import com.example.android.sunshine.app.data.Movie;

/**
 * Created by Sherif on 18-Sep-16.
 */

public interface Callback {
    /**
     * DetailFragmentCallback for when an item has been selected.
     */
    public void onItemSelected(Movie movie);
}
