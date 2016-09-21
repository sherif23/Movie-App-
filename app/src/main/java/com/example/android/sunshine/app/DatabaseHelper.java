package com.example.android.sunshine.app;

/**
 * Created by Sherif on 09-Sep-16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.sunshine.app.data.Movie;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper
{
    /* constants */
    public static final String TABLE_NAME = "Movie3";
    public static final String COLOUMN_posterPath = "poster_path" ;
    public static final String COLOUMN_backdropPath = "backdrop_path" ;
    public static final String COLOUMN_voteAve = "vote_Ave" ;
    public static final String COLOUMN_releaseDate = "release_date" ;
    public static final String COLOUMN_trailersJsonData = "trailers_data" ;
    public static final String COLOUMN_reviewsjsonData = "reviews_data" ;
    public static final String COLOUMN_overview = "overview";
    private static final String LOG_TAG = DatabaseHelper.class.getSimpleName();
    public static final String DATABASE_NAME = "movie.db";
    public static final int DATABASE_VERSION = 10;

    public DatabaseHelper(Context context)
    {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        String createSql =
                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + "(\n" +
                        MovieContract.MovieEntry._ID + " TEXT PRIMARY KEY\n" +
                        ", " + COLOUMN_posterPath + " TEXT\n" +
                        ", " + COLOUMN_backdropPath + " TEXT \n" +
                       // ", " + COLOUMN_voteAve + "TEXT\n"+
                        ", " + COLOUMN_releaseDate + " TEXT\n"+
                        ", " + COLOUMN_trailersJsonData + " TEXT\n"+
                        ", " + COLOUMN_reviewsjsonData + " TEXT\n"+
                        ", " + COLOUMN_overview + " TEXT\n"+
                        ", UNIQUE (" + MovieContract.MovieEntry._ID + ") ON CONFLICT REPLACE)";

        sqLiteDatabase.execSQL(createSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        String deleteSql = "DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME;
        sqLiteDatabase.execSQL(deleteSql);
        onCreate(sqLiteDatabase);
    }


    /**
     * adds a card to the database
     * the key is the card's title
     * replaces in case of conflict
     */
    public boolean insertCard(Movie movie)
    {
        ContentValues contentValues = new ContentValues();
        Log.v("title",movie.getOriginalTitle()) ;
        contentValues.put(MovieContract.MovieEntry._ID, movie.getOriginalTitle());
        contentValues.put(COLOUMN_posterPath,movie.getPosterPath());
        contentValues.put(COLOUMN_backdropPath, movie.getBackdropPath());
        //contentValues.put(COLOUMN_voteAve, movie.getVoteAve());
        contentValues.put(COLOUMN_releaseDate, movie.getReleaseDate());
        contentValues.put(COLOUMN_reviewsjsonData, movie.getReviwesJsonData());
        contentValues.put(COLOUMN_trailersJsonData, movie.getTrailersJsonData());
        contentValues.put(COLOUMN_overview, movie.getOverview());



        SQLiteDatabase database = getWritableDatabase();
        long x = database.insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValues);
        if ( x == -1 )
            return false ;
        else
             return true ;
    }


    /**
     * returns all the cards in the database
     */
    public List<Movie> getMovies()
    {
       SQLiteDatabase database = getReadableDatabase();

        Cursor cursor = database.query(MovieContract.MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        List<Movie> movieList = new ArrayList<Movie>();

        while (cursor.moveToNext())
            {
                Movie movie = new Movie();
                movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry._ID)));
                movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLOUMN_backdropPath)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLOUMN_posterPath)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLOUMN_overview)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLOUMN_releaseDate)));
                movie.setReviwesJsonData(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLOUMN_reviewsjsonData)));
                movie.setTrailersJsonData(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLOUMN_trailersJsonData)));
               // movie.setVoteAve(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLOUMN_voteAve)));
                movieList.add(movie);
            }

        return movieList;
    }

    public boolean selectMovie (Movie movie1) {
        SQLiteDatabase database = getReadableDatabase();

        Cursor cursor = database.query(MovieContract.MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        boolean check = false ;
        while (cursor.moveToNext())
        {
            Movie movie = new Movie();
            movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry._ID)));
            movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLOUMN_backdropPath)));
            movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLOUMN_posterPath)));
            movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLOUMN_overview)));
            movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLOUMN_releaseDate)));
            movie.setReviwesJsonData(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLOUMN_reviewsjsonData)));
            movie.setTrailersJsonData(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLOUMN_trailersJsonData)));
            // movie.setVoteAve(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLOUMN_voteAve)));
            if (movie.getOriginalTitle().equals(movie1.getOriginalTitle())) {check = true ; break;}
        }

        return check ;
    }

    public boolean deleteMovie ( Movie movie) {
        SQLiteDatabase database = getWritableDatabase();
        String id = movie.getOriginalTitle();
        int x = database.delete(MovieContract.MovieEntry.TABLE_NAME, "_id = ?", new String[]{id});
        if (x == 0) return false;
        else return true;
    }

}