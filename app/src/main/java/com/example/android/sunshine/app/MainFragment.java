package com.example.android.sunshine.app;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.sunshine.app.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment /*implements SwipeRefreshLayout.OnRefreshListener, MovieAdapter.Listener */{

    private static final String SELECTED = "select";
    List <Movie> movies = new ArrayList<Movie>() ;
    RecyclerView recyclerViewMovie;
    MovieAdapter adaptermovies;
    SwipeRefreshLayout swipeRefreshLayout;
    Callback callback ;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    GridView gridView;
    static int index ;



    public MainFragment() {
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateMovie();
            return  true ;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateMovie() {
        FetchMovieTask weatherTask = new FetchMovieTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String Ctegory = prefs.getString(getString(R.string.pref_units_key),
                getString(R.string.pref_location_default));
        weatherTask.execute(Ctegory);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovie();
    }

    @Override
    public void onPause() {
        DatabaseConstant.mposition = gridView.getFirstVisiblePosition();
        super.onPause();
    }

    @Override
    public void onResume()
    {
        gridView.setSelection(DatabaseConstant.mposition);
        super.onResume();

    }



    private List <Movie> getMovieDataFromJson(String forecastJsonStr)
            throws JSONException {


        JSONObject forecastJson = new JSONObject(forecastJsonStr) ;
        JSONArray results = forecastJson.getJSONArray("results");
        movies.clear();
        for ( int i = 0 ; i < results.length() ; i++ ) {
            JSONObject result = results.getJSONObject(i) ;
            Movie movie = new Movie(result.getString("id"), result.getString("original_title"),
                    result.getString("title"), result.getString("backdrop_path"), result.getString("poster_path"),
            result.getDouble("vote_average"), result.optBoolean("adult"),result.getString("overview"),result.getString("release_date"));
            FetchTrailerTask trailerTask = new FetchTrailerTask();
            trailerTask.execute(movie);
            FetchReviewTask reviewTask = new FetchReviewTask() ;
            reviewTask.execute(movie) ;
            movies.add(movie);
        }

        return movies;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
       /* recyclerViewMovie = (RecyclerView) rootView.findViewById(R.id.recycler_view_cards);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        Log.e("position" , String.valueOf(DatabaseConstant.mposition) ) ;
        adaptermovies= new MovieAdapter (getActivity());
        adaptermovies.setListener(this);
        recyclerViewMovie
                .setLayoutManager
                        (new GridLayoutManager(getContext(), 2));
        recyclerViewMovie.setAdapter(adaptermovies);
        swipeRefreshLayout.setOnRefreshListener(this);*/
        gridView = (GridView) rootView.findViewById(R.id.movieGrid);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final Movie movie = adaptermovies.getItem(position);
                ((Callback) getActivity()).onItemSelected(movie);
                index = position ;


            }


        });



        return rootView;
    }



    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState !=null){
            movies = (ArrayList<Movie>) savedInstanceState.getSerializable("allmovies");
        }else {
            movies = new ArrayList<>();
        }
        adaptermovies = new MovieAdapter(getContext() , movies);

        setHasOptionsMenu(true);

    }
    @Override
    public void onSaveInstanceState(Bundle bundle){

        bundle.putSerializable("allmovies" , (Serializable) movies);
        if(index != GridView.INVALID_POSITION){
            bundle.putInt(SELECTED ,index);
        }
        super.onSaveInstanceState(bundle);

    }

    public class FetchMovieTask extends AsyncTask <String , Void , List <Movie>> {


        @Override
        protected List <Movie> doInBackground(String... params) {
            movies.clear();
            if (params[0].equals("favorit")) {
                movies = DatabaseConstant.db.getMovies();
                for (int i = 0; i < movies.size(); i++) {
                    try {
                        FetchReviewTask fetchReviewTask = new FetchReviewTask();
                        fetchReviewTask.parseData(movies.get(i), movies.get(i).getReviwesJsonData());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    FetchTrailerTask trailerTask = new FetchTrailerTask();
                    try {
                        trailerTask.parseData(movies.get(i), movies.get(i).getTrailersJsonData());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return movies;
            }
            else if (params[0].equals("popular") || params[0].equals("top_rated")) {
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;

                // Will contain the raw JSON response as a string.
                String forecastJsonStr = null;

                try {

                    URL url = new URL("http://api.themoviedb.org/3/movie/" + params[0] + "?api_key=b645d8907aee53afe121c18189f87a7f");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    // Read the input stream into a String
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        // Nothing to do.
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        // Stream was empty.  No point in parsing.
                        return null;
                    }
                    forecastJsonStr = buffer.toString();
                    Log.v("Data", "forecast json string" + forecastJsonStr);
                } catch (IOException e) {
                    Log.e("PlaceholderFragment", "Error ", e);
                    // If the code didn't successfully get the weather data, there's no point in attemping
                    // to parse it.
                    return null;
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e("PlaceholderFragment", "Error closing stream", e);
                        }
                    }
                }

                try {


                    return getMovieDataFromJson(forecastJsonStr);

                } catch (JSONException e) {
                    e.printStackTrace();

                }
                return null;
            }

            return null ;
        }

        @Override
        protected void onPostExecute(List <Movie> result) {
            //adaptermovies.setData(result);
            //adaptermovies.notifyDataSetChanged();
            adaptermovies = new MovieAdapter(getActivity(), result);
            gridView.setAdapter(adaptermovies);

        }
    }
}



