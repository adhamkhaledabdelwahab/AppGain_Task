package kh.ad.appgaintask.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import kh.ad.appgaintask.R;
import kh.ad.appgaintask.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();
        String str = "Movies App";
        Animation animation = AnimationUtils.loadAnimation(
                this,
                R.anim.bounce_animation
        );
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Handler mHandler = new Handler();
                mHandler.postDelayed((Runnable) () -> {
                    final Intent intent = new Intent(MainActivity.this, MoviesActivity.class);
                    startActivity(intent);
                    finish();
                }, 500);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        binding.AppName.setText(str);
        binding.AppName.postDelayed(() -> binding.AppName.startAnimation(animation), 500);
    }
}