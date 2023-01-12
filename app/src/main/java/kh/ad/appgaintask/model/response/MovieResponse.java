package kh.ad.appgaintask.model.response;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import kh.ad.appgaintask.model.models.MovieModel;

// This class is for requesting single movie
public class MovieResponse {
    // 1- Finding the Movie Object
    @SerializedName("results")
    @Expose()
    private MovieModel movie;

    public MovieModel getMovie(){
        return movie;
    }

    @NonNull
    @Override
    public String toString() {
        return "MovieResponse{" +
                "movie=" + movie +
                '}';
    }
}
