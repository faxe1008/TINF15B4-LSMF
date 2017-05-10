package com.example.fabian.tinf15b4_lsmf.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fabian.tinf15b4_lsmf.HelperFunctions;
import com.example.fabian.tinf15b4_lsmf.R;
import com.example.fabian.tinf15b4_lsmf.apis.Ssapi;
import com.example.fabian.tinf15b4_lsmf.modells.ImageCache;
import com.example.fabian.tinf15b4_lsmf.modells.User;
import com.omertron.themoviedbapi.MovieDbException;
import com.omertron.themoviedbapi.model.movie.MovieInfo;

import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity {


    MovieInfo movieInfo;
    boolean likedMovie;
    boolean likeChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ImageView moviePoster = (ImageView) findViewById(R.id.moviePoster);
        TextView title = (TextView) findViewById(R.id.title);
        TextView rating = (TextView) findViewById(R.id.rating);
        TextView genre = (TextView) findViewById(R.id.genre);
        TextView movieDesc = (TextView) findViewById(R.id.movie_desc);

        movieInfo = (MovieInfo) getIntent().getSerializableExtra("movieInfo");

        if (movieInfo != null) {
            title.setText(movieInfo.getTitle());


            String ratingScore;


            if (movieInfo.getPopularity() > 10) {
                ratingScore = "10";
            } else {
                ratingScore = Float.toString(movieInfo.getPopularity()).substring(0, 3);
            }

            rating.setText(ratingScore + "/10");
            moviePoster.setImageBitmap(ImageCache.getInstance().getCache().get(movieInfo.getId()));

            List<Integer> lg = movieInfo.getGenreIds();
            if (lg != null && lg.size() > 0) {
                try {
                    genre.setText(HelperFunctions.getInstance().getGenreMap("de").get(lg.get(0)) + ", " + movieInfo.getReleaseDate().substring(0, 4));
                } catch (MovieDbException e) {
                    e.printStackTrace();
                }
            }

            movieDesc.setText(movieInfo.getOverview());

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMovieDetail);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        final ImageView btnLike = (ImageView) findViewById(R.id.btn_like);

        //Only liked movies here
        likedMovie = MainActivity.session.getUser().getLikedMovies().contains(movieInfo.getId());
        //Search for movie in liked movies list as cond
        if (likedMovie) {
            btnLike.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            btnLike.setImageResource(android.R.drawable.btn_star_big_off);
        }

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_like) {
                    likedMovie = !likedMovie;
                    if (likedMovie) {

                        btnLike.setImageResource(android.R.drawable.btn_star_big_on);
                    } else {

                        btnLike.setImageResource(android.R.drawable.btn_star_big_off);

                    }
                    likeChanged = !likeChanged;
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                if (likeChanged) {
                    Ssapi ssapi = MainActivity.session.getSsapi();
                    User user = MainActivity.session.getUser();
                    if (likedMovie) {
                        ssapi.insertLikedMovie(user, movieInfo);
                        user.addLikedMovie(movieInfo.getId());
                    } else {
                        ssapi.removeLikedMovie(user, movieInfo);
                        user.removeLikedMovie(movieInfo.getId());
                    }
                }
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
