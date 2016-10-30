package com.udacity.ahmed.popularmoviesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by ahmed on 10/25/2016.
 */

public class FavoritesActivity extends AppCompatActivity {
    View recyclerView;
    private static boolean mTwoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.movie_list);
        assert recyclerView != null;
        DatabaseHandler databaseHandler = new DatabaseHandler(getApplication());
        ArrayList<Movie> movies=databaseHandler.getAllMovies();
        MovieRecyclerViewAdapter sir = new MovieRecyclerViewAdapter(this,mTwoPane);
        sir.setList(movies);
        ((RecyclerView)recyclerView).setAdapter(sir);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        ((RecyclerView) recyclerView).setLayoutManager(layoutManager);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
