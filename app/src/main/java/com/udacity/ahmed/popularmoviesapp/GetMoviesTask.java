package com.udacity.ahmed.popularmoviesapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

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


public class GetMoviesTask extends AsyncTask<Movie, Void, ArrayList<Movie>> {
    String type;
    boolean isError=false;
    public GetMoviesTask(String type){
        this.type=type;
    }

    @Override
    protected ArrayList doInBackground(Movie... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJsonStr = null;


        try {


            Uri uriBuilt = Uri.parse("http://api.themoviedb.org/3/movie/"+type+"?").buildUpon()
                    .appendQueryParameter("api_key",BuildConfig.MOVIE_DB_API_KEY).build();

            URL url = new URL(uriBuilt.toString());


            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return null;
            }
            moviesJsonStr = buffer.toString();

        } catch (IOException e) {
            isError=true;
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    isError=true;
                }
            }
        }

        try {
            return getMovieDataFromJson(moviesJsonStr);
        } catch (JSONException e) {
            isError=true;
        }

        return null;
    }

    private ArrayList getMovieDataFromJson(String forecastJsonStr)
            throws JSONException {

        final String OWM_LIST = "results";
        final String OWM_POSTER_PATH = "poster_path";

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray movieArray = forecastJson.getJSONArray(OWM_LIST);

        ArrayList<Movie> resultStrs = new ArrayList<>();
        for(int i = 0; i < movieArray.length(); i++) {
            JSONObject movie = movieArray.getJSONObject(i);
            String poster = movie.getString(OWM_POSTER_PATH);
            String title = movie.getString("title");
            String overview = movie.getString("overview");
            String voteAvarage = movie.getString("vote_average");
            String id = movie.getString("id");
            String releaseDate = movie.getString("release_date");
            String backdropPath = movie.getString("backdrop_path");
            Movie m =new Movie();
            m.setPosterPath("https://image.tmdb.org/t/p/w500"+poster);
            m.setBackdropPath("https://image.tmdb.org/t/p/w500"+backdropPath);
            m.setId(id);
            m.setTitle(title);
            m.setOverview(overview);
            m.setReleaseDate(releaseDate);
            m.setVoteAvarage(voteAvarage);
            resultStrs.add(m);
        }
        return resultStrs;
    }

}
