package kh.ad.appgaintask.view.activity;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import kh.ad.appgaintask.core.api.MovieDetailsApiClient;
import kh.ad.appgaintask.databinding.ActivityNoInternetBinding;
import kh.ad.appgaintask.model.models.MovieModel;
import kh.ad.appgaintask.view_model.NetworkViewModel;

public class NoInternetActivity extends AppCompatActivity implements Observer<Boolean> {

    private boolean navigationStart = false;

    private MovieModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityNoInternetBinding binding = ActivityNoInternetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        GetDataFromIntent();

        NetworkViewModel networkViewModel = new ViewModelProvider(this)
                .get(NetworkViewModel.class);
        networkViewModel.registerNetworkStateObserver(getApplication());
        networkViewModel.getConnected().observe(this, this);
    }

    private void GetDataFromIntent() {
        if (getIntent().hasExtra("movie")) {
            model = getIntent().getParcelableExtra("movie");
        }
    }

    @Override
    public void onChanged(Boolean aBoolean) {
        if (aBoolean) {
            if (!navigationStart) {
                if (model != null) {
                    Intent intent1 = new Intent(this, MoviesActivity.class);
                    Intent intent2 = new Intent(this, MovieDetailsActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent2.putExtra("movie", model);
                    TaskStackBuilder.create(this).addNextIntent(intent1).addNextIntentWithParentStack(intent2).startActivities();
                } else {
                    int movieId = getIntent().getIntExtra("movieId", 0);
                    if (movieId == 0) {
                        Intent intent = new Intent(this, MoviesActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        navigationStart = true;
                    } else {
                        MovieDetailsApiClient instance = MovieDetailsApiClient.getInstance();
                        instance.getMovieDetails().observe(NoInternetActivity.this, movieModel -> {
                            if (!navigationStart) {
                                if (movieModel != null) {
                                    navigateToMovieDetailsActivity(movieModel);

                                } else {
                                    Toast.makeText(this, "Invalid Movie ID", Toast.LENGTH_SHORT).show();
                                    finishAffinity();
                                }
                                navigationStart = true;
                            }
                        });
                        instance.getMovieDetailsById(movieId);
                    }
                }
            }
        }
    }

    private void navigateToMovieDetailsActivity(MovieModel movieModel) {
        final Intent intent = new Intent(NoInternetActivity.this, MovieDetailsActivity.class);
        intent.putExtra("movie", movieModel);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}