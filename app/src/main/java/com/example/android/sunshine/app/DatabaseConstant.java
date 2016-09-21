package com.example.android.sunshine.app;

import android.content.Context;

import com.example.android.sunshine.app.data.Movie;

/**
 * Created by Sherif on 10-Sep-16.
 */
public class DatabaseConstant {
   public static DatabaseHelper db ;
    public static boolean adapterSize  ;
    public static boolean appear = true ;
    public static int mposition = 0 ;

    DatabaseConstant (Context context) {
        db = new DatabaseHelper(context) ;
    }

}
