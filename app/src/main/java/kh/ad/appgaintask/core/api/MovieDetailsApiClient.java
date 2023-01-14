package kh.ad.appgaintask.core.api;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import kh.ad.appgaintask.core.AppExecutors;
import kh.ad.appgaintask.model.models.MovieModel;
import retrofit2.Call;
import retrofit2.Response;

public class MovieDetailsApiClient {
    private static MovieDetailsApiClient instance;

    private final MutableLiveData<MovieModel> mMovieDetails;

    public static MovieDetailsApiClient getInstance() {
        if (instance == null)
            instance = new MovieDetailsApiClient();
        return instance;
    }

    private MovieDetailsApiClient() {
        mMovieDetails = new MutableLiveData<>();
    }

    public LiveData<MovieModel> getMovieDetails() {
        return mMovieDetails;
    }

    public void getMovieDetailsById(int id) {

        if (retrieveMovieDetailsRunnable != null)
            retrieveMovieDetailsRunnable = null;

        retrieveMovieDetailsRunnable = new RetrieveMovieDetailsRunnable(id);

        final Future<?> myHandler = AppExecutors
                .getInstance()
                .networkIO()
                .submit(retrieveMovieDetailsRunnable);

        AppExecutors.getInstance().networkIO().schedule(() -> {
            myHandler.cancel(true);
        }, 5000, TimeUnit.MILLISECONDS);
    }

    private RetrieveMovieDetailsRunnable retrieveMovieDetailsRunnable;

    private class RetrieveMovieDetailsRunnable implements Runnable {

        private final int movieId;
        boolean cancelRequest;

        public RetrieveMovieDetailsRunnable(int id) {
            this.movieId = id;
            this.cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response<MovieModel> response = getMovieDetails(this.movieId).execute();
                if (cancelRequest) {
                    return;
                }
                Log.d("MovieDetails", "Headers: " + response.headers());
                if (response.code() == 200) {
                    Log.d("MovieDetails", "Response Body: " + response.body());
                    assert response.body() != null;
                    MovieModel movie = response.body();
                    mMovieDetails.postValue(movie);
                } else {
                    Log.d("MovieDetails", "Response Error: " + response.errorBody());
                    assert response.errorBody() != null;
                    String error = response.errorBody().string();
                    Log.v("Tag", "Error " + error);
                    mMovieDetails.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mMovieDetails.postValue(null);
            }

            if (cancelRequest) {
                return;
            }
        }

        private Call<MovieModel> getMovieDetails(int id) {
            return Service.getMovieApi().getMovieById(id, Credentials.API_KEY);
        }

        private void cancelRequest() {
            Log.v("Tag", "Cancelling Search Request");
            cancelRequest = true;
        }
    }
}
