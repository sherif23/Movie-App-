package com.example.android.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.android.sunshine.app.data.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by Sherif on 18-Sep-16.
 */
public  class DetailFragment extends Fragment {

    ArrayAdapter<String> trailerAdapter;
    ArrayAdapter<String> reviewAdapter;
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    private Movie movie;



    public DetailFragment() {
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            movie = (Movie) getArguments().getSerializable(Intent.EXTRA_TEXT);
            if (movie.getReleaseDate().contains("?")) {
                int index = movie.getReleaseDate().indexOf("?") ;
                String date = movie.getReleaseDate().substring(0,index) ;
                String ave = movie.getReleaseDate().substring(index+1) ;
                movie.setReleaseDate(date);
                movie.setVoteAve(ave);
            }
            boolean check = DatabaseConstant.db.selectMovie(movie);
            ((TextView) rootView.findViewById(R.id.detail_text))
                    .setText(movie.getOriginalTitle());
            Picasso.with(getContext()).load(movie.getBackdropPath()).into((ImageView) rootView.findViewById(R.id.poster_image));
            ((TextView) rootView.findViewById(R.id.overView_textView))
                    .setText(movie.getOverview());
            ((TextView) rootView.findViewById(R.id.vote_average))
                .setText(movie.getVoteAve());
             ((TextView) rootView.findViewById(R.id.release_date))
                .setText(movie.getReleaseDate());
            ListView listView = (ListView) rootView.findViewById(R.id.review_listView);
            reviewAdapter = new ArrayAdapter<String>(
                    getActivity(), // The current context (this activity)
                    R.layout.trailer_list_item, // The name of the layout ID.
                    R.id.list_item_text, // The ID of the textview to populate.
                    movie.getReview());
            if (reviewAdapter != null)
            listView.setAdapter(reviewAdapter);

            listView = (ListView) rootView.findViewById(R.id.listView_trailers);
            trailerAdapter = new ArrayAdapter<String>(
                    getActivity(), // The current context (this activity)
                    R.layout.trailer_list_item, // The name of the layout ID.
                    R.id.list_item_text, // The ID of the textview to populate.
                    movie.getTrailerName());
            listView.setAdapter(trailerAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTrailerLinks().get(position)));
                    startActivity(i);
                }
            });

            final ToggleButton toggleButton = (ToggleButton) rootView.findViewById(R.id.toggle);
            final SharedPreferences sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(getContext());
            toggleButton.setChecked(sharedPreferences.getBoolean("toggleButton", false));
            if (check == true) toggleButton.setChecked(true);
            else toggleButton.setChecked(false);

            toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("toggleButton", toggleButton.isChecked());
                    editor.commit();
                    if (isChecked) {
                        if (!movie.getReleaseDate().contains("?")) {
                            String x = movie.getReleaseDate() + "?" + movie.getVoteAve();
                            movie.setReleaseDate(x);
                        }
                        DatabaseConstant.db.insertCard(movie);
                    } else {
                        DatabaseConstant.db.deleteMovie(movie);
                    }
                }
            });

        return rootView;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detailfragment, menu);
        // Retrieve the share menu item
      /*  MenuItem menuItem = menu.findItem(R.id.action_share);
        // Get the provider and hold onto it to set/change the share intent.
        ShareActionProvider mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        // Attach an intent to this ShareActionProvider.  You can update this at any time,
        // like when the user selects a new piece of data they might like to share.
        if (mShareActionProvider != null) {
           // mShareActionProvider.setShareIntent(createShareForecastIntent());
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }*/
    }

 /*   private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                movie.getTrailerLinks().get(0) + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }*/

}