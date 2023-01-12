package kh.ad.appgaintask.model.request;

import kh.ad.appgaintask.core.Credentials;
import kh.ad.appgaintask.core.MovieApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Service {
    private static final Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
            .baseUrl(Credentials.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());
    private static final Retrofit retrofit = retrofitBuilder.build();
    private static final MovieApi movieApi = retrofit.create(MovieApi.class);
    public static MovieApi getMovieApi(){
        return movieApi;
    }
}
