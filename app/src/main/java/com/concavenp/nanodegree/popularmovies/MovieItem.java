package com.concavenp.nanodegree.popularmovies;

import java.util.Date;
import java.util.List;

/**
 * Created by dave on 11/2/2015.
 */
public class MovieItem {
    /*
        {
            "adult":false,
            "backdrop_path":"/dkMD5qlogeRMiEixC4YNPUvax2T.jpg",
            "genre_ids":[28,12,878,53],
            "id":135397,
            "original_language":"en",
            "original_title":"Jurassic World",
            "overview":"Twenty-two years after the events of Jurassic Park, Isla Nublar now features a fully functioning dinosaur theme park, Jurassic World, as originally envisioned by John Hammond.",
            "release_date":"2015-06-12",
            "poster_path":"/jjBgi2r5cRt36xF6iNUEhzscEcb.jpg",
            "popularity":51.985336,
            "title":"Jurassic World",
            "video":false,
            "vote_average":6.9,
            "vote_count":2835
        }
    */
    boolean mAdult;
    String mBackdropPath;
    List<Integer> mGenreIds;
    int mId;
    String mOriginalLanguage;
    String mOriginalTitle;
    String mOverview;
    Date mReleaseDate;
    String mPosterPath;
    double mPopularity;
    String mTitle;
    boolean mVideo;
    double mVoteAverage;
    int mVoteCount;

    public MovieItem() {
        mBackdropPath = "/dkMD5qlogeRMiEixC4YNPUvax2T.jpg";

    }

    public MovieItem(
            boolean adult,
            String backdropPath,
            List<Integer> genreIds,
            int id,
            String originalLanguage,
            String originalTitle,
            String overview,
            Date releaseDate,
            String posterPath,
            double popularity,
            String title,
            boolean video,
            double voteAverage,
            int voteCount
    ) {
        mAdult = adult;
        mBackdropPath = backdropPath;
        mGenreIds = genreIds;
        mId = id;
        mOriginalLanguage = originalLanguage;
        mOriginalTitle = originalTitle;
        mOverview = overview;
        mReleaseDate = releaseDate;
        mPosterPath = posterPath;
        mPopularity = popularity;
        mTitle = title;
        mVideo = video;
        mVoteAverage = voteAverage;
        mVoteCount = voteCount;
    }

}
