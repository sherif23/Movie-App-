package com.example.android.sunshine.app;

import android.provider.BaseColumns;

/**
 * Created by Sherif on 09-Sep-16.
 */
public class MovieContract {

//    private String id ;
//    private String originalTitle ;
//    private String title ;
//    private String posterPath ;
//    private String backdropPath ;
//    private Double voteAve ;
//    private Boolean adult ;
//    private String overview ;
//    private String releaseDate ;
//    private String trailersJsonData ;
//    private String reviwesJsonData ;

    public static final class MovieEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "Movie3";
        public static final String COLOUMN_posterPath = "poster_path" ;
        public static final String COLOUMN_backdropPath = "backdrop_path" ;
        public static final String COLOUMN_voteAve = "vote_Ave" ;
        public static final String COLOUMN_releaseDate = "release_date" ;
        public static final String COLOUMN_trailersJsonData = "trailers_data" ;
        public static final String COLOUMN_reviewsjsonData = "reviews_data" ;
        public static final String COLOUMN_overview = "overview";
    }
}
