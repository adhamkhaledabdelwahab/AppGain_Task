package kh.ad.appgaintask.view.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import kh.ad.appgaintask.databinding.ActivityMoviesBinding;
import kh.ad.appgaintask.view.adapter.MovieRecyclerView;
import kh.ad.appgaintask.view.adapter.onMovieListener;
import kh.ad.appgaintask.view_model.MovieListViewModel;
import kh.ad.appgaintask.view_model.NetworkViewModel;

public class MoviesActivity extends AppCompatActivity implements onMovieListener, Observer<Boolean> {

    private MovieRecyclerView adapter;

    private MovieListViewModel movieListViewModel;

    private ActivityMoviesBinding binding;

    private boolean navigationStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoviesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NetworkViewModel networkViewModel = new ViewModelProvider(this)
                .get(NetworkViewModel.class);
        networkViewModel.registerNetworkStateObserver(getApplication());
        networkViewModel.getConnected().observe(this, this);

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

    @Override
    public void onChanged(Boolean aBoolean) {
        if (!aBoolean) {
            if (!navigationStart) {
                Intent intent = new Intent(this, NoInternetActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                navigationStart = true;
            }
        }
    }
}