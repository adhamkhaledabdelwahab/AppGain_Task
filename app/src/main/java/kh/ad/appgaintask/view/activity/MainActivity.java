package kh.ad.appgaintask.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import kh.ad.appgaintask.R;
import kh.ad.appgaintask.core.api.MovieDetailsApiClient;
import kh.ad.appgaintask.databinding.ActivityMainBinding;
import kh.ad.appgaintask.model.models.MovieModel;
import kh.ad.appgaintask.view_model.NetworkViewModel;

public class MainActivity extends AppCompatActivity implements Animation.AnimationListener, Observer<Boolean> {

    private ActivityMainBinding binding;
    private boolean navigationStart = false;
    private Animation animation;
    private boolean isNetworkConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NetworkViewModel networkViewModel = new ViewModelProvider(this)
                .get(NetworkViewModel.class);
        networkViewModel.registerNetworkStateObserver(getApplication());
        networkViewModel.getConnected().observe(this, this);


        animation = AnimationUtils.loadAnimation(
                this,
                R.anim.bounce_animation
        );
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();
        String str = "Movies App";
        animation.setAnimationListener(this);
        binding.AppName.setText(str);
        binding.AppName.postDelayed(() -> binding.AppName.startAnimation(animation), 500);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (isNetworkConnected) {
            afterAnimation();
        } else {
            final Intent intent = new Intent(MainActivity.this, NoInternetActivity.class);
            Uri uri = getIntent().getData();
            if (uri != null) {
                List<String> parameters = uri.getPathSegments();
                if (parameters.size() == 2) {
                    intent.putExtra("movieId", Integer.parseInt(parameters.get(1)));
                }
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    private void afterAnimation() {
        Uri uri = getIntent().getData();
        if (uri != null) {
            Log.d("MainActivity", "Uri: " + uri);
            List<String> parameters = uri.getPathSegments();
            if (parameters.size() == 2) {
                MovieDetailsApiClient instance = MovieDetailsApiClient.getInstance();
                instance.getMovieDetails().observe(MainActivity.this, movieModel -> {
                    if (!navigationStart) {
                        if (movieModel != null) {
                            navigateToMovieDetailsActivity(movieModel);

                        } else {
                            navigateToMoviesActivity();
                        }
                        navigationStart = true;
                    }
                });
                instance.getMovieDetailsById(Integer.parseInt(parameters.get(1)));
            } else {
                navigateToMoviesActivity();
            }
        } else {
            navigateToMoviesActivity();
        }
    }

    private void navigateToMoviesActivity() {
        Handler mHandler = new Handler();
        mHandler.postDelayed((Runnable) () -> {
            final Intent intent = new Intent(MainActivity.this, MoviesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }, 500);
    }

    private void navigateToMovieDetailsActivity(MovieModel movieModel) {
        final Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
        intent.putExtra("movie", movieModel);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onChanged(Boolean aBoolean) {
        isNetworkConnected = aBoolean;
    }
}