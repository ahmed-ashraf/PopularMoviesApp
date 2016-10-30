package com.udacity.ahmed.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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

/**
 * Created by ahmed on 10/27/2016.
 */

public abstract class GetReviewsTask extends AsyncTask<Void,Void, ArrayList<Review>> {
    String movieId;
    boolean isError=false;
    public GetReviewsTask(String movieId){
        this.movieId =movieId;
    }

    @Override
    protected ArrayList<Review> doInBackground(Void... voids) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String trailersJSON=null;
        String surl = "https://api.themoviedb.org/3/movie/"+movieId+"/reviews?api_key="+BuildConfig.MOVIE_DB_API_KEY;

        try{
            URL url = new URL(surl);

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
            trailersJSON = buffer.toString();
        }catch (Exception e){
            isError=true;
            return null;
        }finally {
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
            return getReviewsFromJson(trailersJSON);
        } catch (JSONException e) {
            isError=true;
        }

        return null;
    }
    private ArrayList<Review> getReviewsFromJson(String jsonStr) throws JSONException{

        ArrayList<Review> results = new ArrayList<>();
        JSONObject obJson = new JSONObject(jsonStr);
        JSONArray resArray = obJson.getJSONArray("results");
        for (int i=0;i<resArray.length();i++){
            JSONObject review = resArray.getJSONObject(i);
            Review r = new Review();
            r.setId(review.getString("id"));
            r.setAuthor(review.getString("author"));
            r.setContent(review.getString("content"));
            r.setUrl(review.getString("url"));
            results.add(r);
        }
        return results;
    }

    @Override
    protected void onPostExecute(ArrayList<Review> reviews) {
        if(isError){
            handleError();
        }else
        for (int i=0;i<reviews.size();i++){
            Review review  = reviews.get(i);
            addLayout(review);
        }
    }
    abstract void addLayout(final Review review);
    abstract void handleError();
}
