package kh.ad.appgaintask.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import kh.ad.appgaintask.databinding.ActivityMovieDetailsBinding;
import kh.ad.appgaintask.model.models.MovieModel;
import kh.ad.appgaintask.view_model.NetworkViewModel;

public class MovieDetailsActivity extends AppCompatActivity implements View.OnClickListener, Observer<Boolean> {

    private ActivityMovieDetailsBinding binding;

    private MovieModel movieModel;

    private boolean navigationStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NetworkViewModel networkViewModel = new ViewModelProvider(this)
                .get(NetworkViewModel.class);
        networkViewModel.registerNetworkStateObserver(getApplication());
        networkViewModel.getConnected().observe(this, this);

        binding.backButton.setOnClickListener(this);

        GetDataFromIntent();
    }

    private void GetDataFromIntent() {
        if (getIntent().hasExtra("movie")) {
            movieModel = getIntent().getParcelableExtra("movie");
            binding.movieDetailsDesc.setText(movieModel.getOverview());
            binding.movieDetailsTitle.setText(movieModel.getTitle());
            binding.movieDetailsRating.setRating(movieModel.getVote_average() / 2);
            Glide.with(this)
                    .load("https://image.tmdb.org/t/p/original"
                            + (movieModel.getBackdrop_path() != null ? movieModel.getBackdrop_path() :
                            movieModel.getPoster_path()))
                    .into(binding.movieDetailsImage);
        }
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }

    @Override
    public void onChanged(Boolean aBoolean) {
        if (!aBoolean) {
            if (!navigationStart) {
                Intent intent = new Intent(this, NoInternetActivity.class);
                intent.putExtra("movie", movieModel);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                navigationStart = true;
            }
        }
    }
}