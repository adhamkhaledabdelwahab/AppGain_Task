package kh.ad.appgaintask.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import kh.ad.appgaintask.model.models.MovieModel;
import kh.ad.appgaintask.view_model.repository.MovieRepository;

public class MovieListViewModel extends ViewModel {

    private final MovieRepository movieRepository;

    public MovieListViewModel(){
        movieRepository = MovieRepository.getInstance();
    }

    public LiveData<List<MovieModel>> getPopMovies() {
        return movieRepository.getPopularMovies();
    }

    public void searchPopularMoviesApi(int pageNumber){
        movieRepository.searchPopularMoviesApi(pageNumber);
    }

    public void searchNextPagePopular(){
        movieRepository.searchNextPagePopular();
    }
}
