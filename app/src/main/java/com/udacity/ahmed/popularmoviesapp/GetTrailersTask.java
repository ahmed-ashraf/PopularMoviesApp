package com.udacity.ahmed.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

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
 * Created by ahmed on 10/24/2016.
 */

public abstract class GetTrailersTask extends AsyncTask<Void,Void, ArrayList<String>> {

    String movieID;
    boolean isError = false;
    public GetTrailersTask(String movieID){
        this.movieID = movieID;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String trailersJSON=null;
        String surl = "https://api.themoviedb.org/3/movie/"+movieID+"/videos?api_key="+BuildConfig.MOVIE_DB_API_KEY;

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
        }catch (IOException e){
            isError = true;
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
            return getTrailerKeysFromJson(trailersJSON);
        } catch (JSONException e) {
            isError = true;
        }

        return null;
    }
    private ArrayList getTrailerKeysFromJson (String jsonStr) throws JSONException{
        final String OWM_LIST = "results";
        final String OWM_KEY = "key";
        ArrayList<String> resultStrs = new ArrayList<>();
        JSONObject forecastJson = new JSONObject(jsonStr);
        JSONArray keyArray = forecastJson.getJSONArray(OWM_LIST);

        for(int i = 0; i < keyArray.length(); i++) {
            JSONObject k = keyArray.getJSONObject(i);
            String key = k.getString(OWM_KEY);
            resultStrs.add(key);
        }
        return resultStrs;
    }
    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        if (isError){
            handleError();
        }else
        for (int i=0;i<strings.size();i++){
            addLayout(strings.get(i));
        }
    }
    abstract void addLayout(final String key);
    abstract void handleError();
}
