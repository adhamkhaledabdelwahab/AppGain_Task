package kh.ad.appgaintask.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;

import kh.ad.appgaintask.databinding.ActivityMovieDetailsBinding;
import kh.ad.appgaintask.model.models.MovieModel;

public class MovieDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMovieDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(this);

        GetDataFromIntent();
    }

    private void GetDataFromIntent() {
        if (getIntent().hasExtra("movie")) {
            MovieModel model = getIntent().getParcelableExtra("movie");
            Log.v("Tag", "incoming intent " + model.getId());
            binding.movieDetailsDesc.setText(model.getOverview());
            binding.movieDetailsTitle.setText(model.getTitle());
            binding.movieDetailsRating.setRating(model.getVote_average() / 2);
            Glide.with(this)
                    .load("https://image.tmdb.org/t/p/original"
                            + (model.getBackdrop_path() != null ? model.getBackdrop_path() :
                            model.getPoster_path()))
                    .into(binding.movieDetailsImage);
        }
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }
}