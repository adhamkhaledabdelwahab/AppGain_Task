package kh.ad.appgaintask.model.request;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import kh.ad.appgaintask.core.AppExecutors;
import kh.ad.appgaintask.core.Credentials;
import kh.ad.appgaintask.model.models.MovieModel;
import kh.ad.appgaintask.model.response.MovieSearchResponse;
import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {

    private static MovieApiClient instance;

    private final MutableLiveData<List<MovieModel>> mMoviesPop;

    private RetrievePopularMoviesRunnable retrievePopularMoviesRunnable;

    public static MovieApiClient getInstance() {
        if (instance == null)
            instance = new MovieApiClient();
        return instance;
    }

    private MovieApiClient() {
        mMoviesPop = new MutableLiveData<>();
    }

    public LiveData<List<MovieModel>> getMoviesPop() {
        return mMoviesPop;
    }

    public void searchPopularMoviesApi(int pageNumber) {

        if (retrievePopularMoviesRunnable != null)
            retrievePopularMoviesRunnable = null;

        retrievePopularMoviesRunnable = new RetrievePopularMoviesRunnable(pageNumber);

        final Future<?> myHandler = AppExecutors
                .getInstance()
                .networkIO()
                .submit(retrievePopularMoviesRunnable);

        AppExecutors.getInstance().networkIO().schedule(() -> {
            myHandler.cancel(true);
        }, 5000, TimeUnit.MILLISECONDS);
    }

    private class RetrievePopularMoviesRunnable implements Runnable {

        private final int pageNumber;
        boolean cancelRequest;

        public RetrievePopularMoviesRunnable(int pageNumber) {
            this.pageNumber = pageNumber;
            this.cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response<MovieSearchResponse> response = getPopularMovies(pageNumber).execute();
                if (cancelRequest) {
                    return;
                }
                if (response.code() == 200) {
                    assert response.body() != null;
                    List<MovieModel> list = new ArrayList<>(
                            response.body().getMovies());
                    if (pageNumber == 1) {
                        mMoviesPop.postValue(list);
                    } else {
                        List<MovieModel> currentMovies = mMoviesPop.getValue();
                        assert currentMovies != null;
                        currentMovies.addAll(list);
                        mMoviesPop.postValue(currentMovies);
                    }
                } else {
                    assert response.errorBody() != null;
                    String error = response.errorBody().string();
                    Log.v("Tag", "Error " + error);
                    mMoviesPop.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mMoviesPop.postValue(null);
            }

            if (cancelRequest) {
                return;
            }
        }

        private Call<MovieSearchResponse> getPopularMovies(int pageNumber) {
            return Service.getMovieApi().getPopularMovies(Credentials.API_KEY, pageNumber);
        }

        private void cancelRequest() {
            Log.v("Tag", "Cancelling Search Request");
            cancelRequest = true;
        }
    }
}
