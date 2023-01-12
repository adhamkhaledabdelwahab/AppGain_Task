package kh.ad.appgaintask.view.activity;

import kh.ad.appgaintask.databinding.ActivityMoviesBinding;
import kh.ad.appgaintask.view.adapter.MovieRecyclerView;
import kh.ad.appgaintask.view.adapter.onMovieListener;
import kh.ad.appgaintask.view_model.MovieListViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import java.util.Locale;

public class MoviesActivity extends AppCompatActivity implements onMovieListener {

    private MovieRecyclerView adapter;

    private MovieListViewModel movieListViewModel;

    private ActivityMoviesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoviesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Configuration config;
        config = new Configuration(getResources().getConfiguration());
        config.locale = Locale.ENGLISH;
        config.setLayoutDirection(new Locale("en"));
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        movieListViewModel = new ViewModelProvider(this)
                .get(MovieListViewModel.class);

        configureRecyclerView();

        observeAnyChange();

        observePopularMovies();

        movieListViewModel.searchPopularMoviesApi(1);
    }

    private void observePopularMovies() {
        movieListViewModel.getPopMovies().observe(this,
                movieModels -> {
                    if (movieModels != null) {
                        Log.v("Tag", "list length " + movieModels.size());
                        adapter.setMovies(movieModels);
                    }
                });
    }

    private void observeAnyChange() {
        movieListViewModel.getPopMovies().observe(this,
                movieModels -> {
                    if (movieModels != null) {
                        adapter.setMovies(movieModels);
                    }
                });
    }

    private void configureRecyclerView() {
        adapter = new MovieRecyclerView(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollHorizontally(1)) {
                    movieListViewModel.searchNextPagePopular();
                }
            }
        });
    }

    @Override
    public void onMovieClick(int position) {
        Intent intent = new Intent(MoviesActivity.this,
                MovieDetailsActivity.class);
        intent.putExtra("movie", adapter.getSelectedMovie(position));
        startActivity(intent);
    }
}