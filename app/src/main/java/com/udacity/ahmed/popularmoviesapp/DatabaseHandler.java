package com.udacity.ahmed.popularmoviesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by ahmed on 10/25/2016.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "favoriteMovies";


    public static final String TABLE_NAME = "movie";

    public static final String COLUMN_MOVIE_ID = "movie_id";
    public static final String COLUMN_MOVIE_TITLE = "original_title";
    public static final String COLUMN_MOVIE_POSTER_PATH = "poster_path";
    public static final String COLUMN_MOVIE_OVERVIEW = "overview";
    public static final String COLUMN_MOVIE_VOTE_AVERAGE = "vote_average";
    public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";
    public static final String COLUMN_MOVIE_BACKDROP_PATH = "backdrop_path";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_MOVIE_ID + " TEXT PRIMARY KEY," + COLUMN_MOVIE_TITLE + " TEXT,"
                + COLUMN_MOVIE_POSTER_PATH + " TEXT," +COLUMN_MOVIE_OVERVIEW+" TEXT,"+COLUMN_MOVIE_VOTE_AVERAGE+" TEXT,"
                +COLUMN_MOVIE_RELEASE_DATE+" TEXT,"+COLUMN_MOVIE_BACKDROP_PATH+" TEXT"+ ")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public void addMovie(Movie movie){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MOVIE_ID, movie.getId());
        values.put(COLUMN_MOVIE_TITLE, movie.getTitle());
        values.put(COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
        values.put(COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        values.put(COLUMN_MOVIE_VOTE_AVERAGE, movie.getVoteAvarage());
        values.put(COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
        values.put(COLUMN_MOVIE_BACKDROP_PATH, movie.getBackdropPath());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Movie getMovie(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] { COLUMN_MOVIE_ID,
                        COLUMN_MOVIE_TITLE, COLUMN_MOVIE_POSTER_PATH,
                COLUMN_MOVIE_OVERVIEW,COLUMN_MOVIE_VOTE_AVERAGE,COLUMN_MOVIE_RELEASE_DATE
        ,COLUMN_MOVIE_BACKDROP_PATH}, COLUMN_MOVIE_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        else return null;
        Movie movie = new Movie();
        try{
            movie.setId(cursor.getString(0));
        }catch (Exception e){
            return null;
        }
        movie.setTitle(cursor.getString(1));
        movie.setPosterPath(cursor.getString(2));
        movie.setOverview(cursor.getString(3));
        movie.setVoteAvarage(cursor.getString(4));
        movie.setReleaseDate(cursor.getString(5));
        movie.setBackdropPath(cursor.getString(6));
        return movie;
    }

    public ArrayList<Movie> getAllMovies(){
        ArrayList<Movie> movieList = new ArrayList<Movie>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setId(cursor.getString(0));
                movie.setTitle(cursor.getString(1));
                movie.setPosterPath(cursor.getString(2));
                movie.setOverview(cursor.getString(3));
                movie.setVoteAvarage(cursor.getString(4));
                movie.setReleaseDate(cursor.getString(5));
                movie.setBackdropPath(cursor.getString(6));
                // Adding contact to list
                movieList.add(movie);
            } while (cursor.moveToNext());
        }
        return movieList;
    }
    public void deleteMovie(Movie movie){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_MOVIE_ID + " = ?",
                new String[] { String.valueOf(movie.getId()) });
        db.close();
    }
}
