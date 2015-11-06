package com.concavenp.nanodegree.popularmovies;

import java.util.Date;
import java.util.List;

/**
 * Created by dave on 11/2/2015.
 */
public class MovieItems {

    int page;
    List<item> results;
    int total_pages;
    int total_results;

    public static class item {
        boolean adult;
        String backdrop_path;
        List<Integer> genre_ids;
        int id;
        String original_language;
        String original_title;
        String overview;
        String release_date;
        String poster_path;
        double popularity;
        String title;
        boolean video;
        double vote_average;
        int vote_Count;
    }

}
