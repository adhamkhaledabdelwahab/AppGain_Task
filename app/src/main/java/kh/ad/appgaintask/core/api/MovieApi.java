package kh.ad.appgaintask.core.api;

import kh.ad.appgaintask.model.models.MovieModel;
import kh.ad.appgaintask.model.response.MovieSearchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {
    @GET("3/movie/{movie_id}")
    Call<MovieModel> getMovieById(
            @Path("movie_id") int id,
            @Query("api_key") String key
    );

    @GET("3/movie/popular")
    Call<MovieSearchResponse> getPopularMovies(
            @Query("api_key") String key,
            @Query("page") int page
    );
}
