package kh.ad.appgaintask.core;

import kh.ad.appgaintask.model.models.MovieModel;
import kh.ad.appgaintask.model.response.MovieSearchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {
    // Search for movies
    // https://api.themoviedb.org/3/search/movie?api_key={api_key}&query=Jack+Reacher
    @GET("3/search/movie")
    Call<MovieSearchResponse> searchMovie(
            @Query("api_key") String key,
            @Query("query") String query,
            @Query("page") int page
    );

    // Making Search with ID
    // https://api.themoviedb.org/3/movie/550?api_key=f4ead36004a979fb7933852b88a8499a
    @GET("3/movie/{movie_id}")
    Call<MovieModel> getMovieById(
            @Path("movie_id") int id,
            @Query("api_key") String key
    );

    // Getting Popular Movies
    // https://api.themoviedb.org/3/movie/popular?api_key=f4ead36004a979fb7933852b88a8499a&page=1
    @GET("3/movie/popular")
    Call<MovieSearchResponse> getPopularMovies(
            @Query("api_key") String key,
            @Query("page") int page
    );
}
