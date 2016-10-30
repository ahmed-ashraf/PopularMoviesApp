package com.udacity.ahmed.popularmoviesapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * A fragment representing a single movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    /**
     * The dummy content this fragment is presenting.
     */
    private Movie movie;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(false);
        if (getArguments().containsKey("movieDetails")) {
            movie = getArguments().getParcelable("movieDetails");

        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.movie_detail, container, false);
        Activity activity = this.getActivity();
        final boolean[] isInFavorite = {false};
        final DatabaseHandler databaseHandler = new DatabaseHandler(activity);
        final Movie m = databaseHandler.getMovie(movie.getId());
        final FloatingActionButton fab = (FloatingActionButton) activity.findViewById(R.id.fab);
        if(fab!=null){
            if (m != null) {
                isInFavorite[0] = true;
                fab.setImageResource(R.drawable.heart_1);
            }
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isInFavorite[0]) {
                        isInFavorite[0] = true;
                        fab.setImageResource(R.drawable.heart_1);
                        databaseHandler.addMovie(movie);
                    } else {
                        isInFavorite[0] = false;
                        fab.setImageResource(R.drawable.heart_2);
                        databaseHandler.deleteMovie(movie);
                    }
                }
            });
        }

        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(movie.getTitle());
            ImageView backdrop = (ImageView)appBarLayout.findViewById(R.id.movie_backdrop);
            Picasso.with(appBarLayout.getContext()).load(movie.getBackdropPath()).into(backdrop);
        }

        if (movie != null) {
            ((TextView) rootView.findViewById(R.id.overview)).setText(movie.getOverview());
            Picasso.with(rootView.getContext()).load(movie.getPosterPath()).into((ImageView) rootView.findViewById(R.id.poster));

            final LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.videoslayout);
            GetTrailersTask videosTask = new GetTrailersTask(movie.getId()){

                @Override
                void addLayout(final String key) {
                    View layout =  LayoutInflater.from(rootView.getContext()).inflate(R.layout.trailer_conent, linearLayout, false);
                    ImageView image = (ImageView) layout.findViewById(R.id.trailerImage);
                    ImageView play = (ImageView) layout.findViewById(R.id.imageView2);
                    play.setImageDrawable(rootView.getContext().getDrawable(R.drawable.play_button));

                    Picasso.with(rootView.getContext()).load("http://img.youtube.com/vi/"+key+"/0.jpg").into(image);
                    play.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+key));
                            rootView.getContext().startActivity(browserIntent);
                        }
                    });
                    linearLayout.addView(layout);
                }

                @Override
                void handleError() {
                    Toast.makeText(rootView.getContext(), "Can not connect to server\nCheck your Internet connection.",
                            Toast.LENGTH_LONG).show();
                }
            };
            videosTask.execute();

            final LinearLayout reviewsLayout = (LinearLayout) rootView.findViewById(R.id.reviews);
            GetReviewsTask reviewsTask = new GetReviewsTask(movie.getId()){

                @Override
                void addLayout(final Review review) {
                    View layout = LayoutInflater.from(rootView.getContext()).inflate(R.layout.review_content, reviewsLayout, false);
                    TextView author = (TextView) layout.findViewById(R.id.author);
                    TextView content = (TextView) layout.findViewById(R.id.reveiwContent);
                    content.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(review.getUrl()));
                            rootView.getContext().startActivity(browserIntent);
                        }
                    });
                    author.setText(review.getAuthor());
                    content.setText(review.getContent().substring(0, Math.min(review.getContent().length(), 100))+"...READ MORE");
                    reviewsLayout.addView(layout);
                }

                @Override
                void handleError() {
                    Toast.makeText(rootView.getContext(), "Can not connect to server\nCheck your Internet connection.",
                            Toast.LENGTH_LONG).show();
                }
            };
            reviewsTask.execute();

            ((TextView)rootView.findViewById(R.id.year)).setText(movie.getReleaseDate()
                    .substring(0, Math.min(movie.getReleaseDate().length(), 4)));
            ((TextView)rootView.findViewById(R.id.rating)).setText(movie.getVoteAvarage()+"/10");
        }
        return rootView;
    }
}
