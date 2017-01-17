package com.vinay.guessthemovie;

import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

/**
 * Created by salimatti on 1/16/2017.
 */

public class HelpDb {


    private TmdbMovies movies = new TmdbApi("87827f3c119a9d103cb4f2e78112046f").getMovies();
    MovieDb movie = movies.getMovie(5353, "en");
    MovieResultsPage n=movies.getNowPlayingMovies("tamil",500);
    List<MovieDb> a=n.getResults();

    String s=movie.getTitle();


}
