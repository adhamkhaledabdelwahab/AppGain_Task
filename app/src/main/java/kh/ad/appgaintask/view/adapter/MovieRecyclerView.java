package kh.ad.appgaintask.view.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import kh.ad.appgaintask.databinding.PopularMoviesLayoutBinding;
import kh.ad.appgaintask.model.models.MovieModel;

public class MovieRecyclerView extends RecyclerView.Adapter<MovieRecyclerView.Holder> {

    private List<MovieModel> mMovies;
    private final onMovieListener listener;
    private PopularMoviesLayoutBinding binding;

    public MovieRecyclerView(onMovieListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        binding = PopularMoviesLayoutBinding.inflate(inflater, parent, false);
        return new Holder(binding.getRoot(), listener);

    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.title.setText(mMovies.get(position).getTitle());
        holder.ratingBar.setNumStars(5);
        holder.ratingBar.setRating(mMovies.get(position).getVote_average() / 2);
        Glide.with(holder.itemView.getContext())
                .load("https://image.tmdb.org/t/p/original"
                        + mMovies.get(position).getPoster_path())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (mMovies != null)
            return mMovies.size();
        return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMovies(List<MovieModel> movieModels) {
        this.mMovies = movieModels;
        this.notifyDataSetChanged();
    }

    public MovieModel getSelectedMovie(int position) {
        if (mMovies != null) {
            if (mMovies.size() > 0) {
                return mMovies.get(position);
            }
        }
        return null;
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        onMovieListener listener;

        TextView title;

        ImageView imageView;

        RatingBar ratingBar;

        public Holder(@NonNull View itemView, onMovieListener listener) {
            super(itemView);
            title = binding.movieTitle;
            imageView = binding.movieImg;
            ratingBar = binding.ratingBar;
            binding.getRoot().setOnClickListener(this);
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            listener.onMovieClick(getAdapterPosition());
        }
    }
}
