package com.example.fabian.tinf15b4_lsmf.modells;

import android.util.Log;

import com.example.fabian.tinf15b4_lsmf.enums.SortOrder;
import com.omertron.themoviedbapi.model.movie.MovieInfo;

import java.util.Comparator;

import static com.example.fabian.tinf15b4_lsmf.enums.SortOrder.RATING_DESC;



public class MovieComparator implements Comparator<MovieInfo> {


    SortOrder sortOrder = RATING_DESC;

    public MovieComparator(SortOrder so) {
        this.sortOrder = so;
    }

    @Override
    public int compare(MovieInfo movieInfo, MovieInfo t1) {
        float first =  t1.getVoteAverage();
        float second =  movieInfo.getVoteAverage();

        switch (sortOrder) {
            case RATING_DESC:
                return (int) (first - second)*100;

            case RATING_ASC:
                return (int) (second - first)*100;

            case NAME_DESC:
                return movieInfo.getTitle().compareTo(t1.getTitle());

            case NAME_ASC:
                return t1.getTitle().compareTo(movieInfo.getTitle());
            default:
                return (int) (t1.getPopularity() - movieInfo.getPopularity())*100;

        }

    }


}
