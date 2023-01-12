package kh.ad.appgaintask.view_model.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import kh.ad.appgaintask.model.models.MovieModel;
import kh.ad.appgaintask.model.request.MovieApiClient;

public class MovieRepository {
    private static MovieRepository instance;

    private final MovieApiClient movieApiClient;

    private int mPageNumber;

    public static MovieRepository getInstance(){
        if (instance == null)
            instance = new MovieRepository();
        return instance;
    }

    private MovieRepository(){
        movieApiClient = MovieApiClient.getInstance();
    }

    public LiveData<List<MovieModel>> getPopularMovies(){
        return movieApiClient.getMoviesPop();
    }

    public void searchPopularMoviesApi(int pageNumber){
        mPageNumber = pageNumber;
        movieApiClient.searchPopularMoviesApi(pageNumber);
    }

    public void searchNextPagePopular(){
        searchPopularMoviesApi(mPageNumber+1);
    }
}
