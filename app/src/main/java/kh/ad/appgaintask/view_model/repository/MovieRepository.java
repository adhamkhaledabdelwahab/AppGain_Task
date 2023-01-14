package kh.ad.appgaintask.view_model.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import kh.ad.appgaintask.model.models.MovieModel;
import kh.ad.appgaintask.core.api.PopularMoviesApiClient;

public class MovieRepository {
    private static MovieRepository instance;

    private final PopularMoviesApiClient popularMoviesApiClient;

    private int mPageNumber;

    public static MovieRepository getInstance(){
        if (instance == null)
            instance = new MovieRepository();
        return instance;
    }

    private MovieRepository(){
        popularMoviesApiClient = PopularMoviesApiClient.getInstance();
    }

    public LiveData<List<MovieModel>> getPopularMovies(){
        return popularMoviesApiClient.getMoviesPop();
    }

    public void searchPopularMoviesApi(int pageNumber){
        mPageNumber = pageNumber;
        popularMoviesApiClient.searchPopularMoviesApi(pageNumber);
    }

    public void searchNextPagePopular(){
        searchPopularMoviesApi(mPageNumber+1);
    }
}
