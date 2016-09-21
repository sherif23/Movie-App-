package com.example.android.sunshine.app;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.sunshine.app.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sherif on 06-Sep-16.
 */

public class FetchTrailerTask extends AsyncTask <Movie , Void , Void> {


    @Override
    protected Void doInBackground(Movie... movies) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        try {

            URL url = new URL("http://api.themoviedb.org/3/movie/" + movies[0].getId() + "/videos?api_key=b645d8907aee53afe121c18189f87a7f");
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
            movies[0].setTrailersJsonData(forecastJsonStr);
            parseData( movies[0],forecastJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }



    public void parseData (Movie movie , String data) throws JSONException {
        JSONObject trailerJson = new JSONObject(data) ;
        JSONArray results = trailerJson.getJSONArray("results");
        List<String> links = new ArrayList<String>() ;
        List <String> trailerNames = new ArrayList<String>() ;
        for ( int i = 0 ; i < results.length() ; i++ ) {
            JSONObject result = results.getJSONObject(i) ;
            links.add("https://www.youtube.com/watch?v="+result.getString("key")) ;
            trailerNames.add(result.getString("name"));
        }

        movie.setTrailerLinks(links);
        movie.setTrailerName(trailerNames);
    }

}