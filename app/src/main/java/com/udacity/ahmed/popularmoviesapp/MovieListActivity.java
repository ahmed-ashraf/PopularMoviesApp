package com.udacity.ahmed.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * An activity representing a list of movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private static boolean mTwoPane;
    String sortingType;
    View recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
        sortingType = prefs.getString("sort_order", "popular");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        recyclerView = findViewById(R.id.movie_list);
        assert recyclerView != null;
        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
        }
        setupRecyclerView((RecyclerView) recyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        ((RecyclerView) recyclerView).setLayoutManager(layoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
        String newType = prefs.getString("sort_order", "popular");
        if (!newType.equals(sortingType)) {
            this.sortingType=newType;
            setupRecyclerView((RecyclerView) recyclerView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Context context = getApplication();
            Intent i = new Intent(context, SettingsActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            return true;
        }else if(id ==R.id.favorites){
            Context context = getApplication();
            Intent i = new Intent(context, FavoritesActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {
        final MovieRecyclerViewAdapter sir=new MovieRecyclerViewAdapter(this,mTwoPane);
        GetMoviesTask getMoviesTask = new GetMoviesTask(sortingType){
            @Override
            protected void onPostExecute(ArrayList<Movie> movieModels) {
                if(isError){
                    Toast.makeText(getBaseContext(), "Can not connect to server\nCheck your Internet connection.",
                            Toast.LENGTH_LONG).show();
                }else {
                    sir.setList(movieModels);
                    recyclerView.setAdapter(sir);
                }
            }
        };
        getMoviesTask.execute();
    }


}
