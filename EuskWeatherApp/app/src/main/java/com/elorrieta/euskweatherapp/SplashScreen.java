package com.elorrieta.euskweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    private ImageView logo1;
    private ImageView logo2;
    private ImageView logo3;

    private ObjectAnimator animacionAlpha;
    private ObjectAnimator animacionAlphaReves;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logo1 = findViewById(R.id.imageView);
        Animation animationScale = AnimationUtils.loadAnimation(this, R.anim.scale);
        logo1.startAnimation(animationScale);

        logo2 = findViewById(R.id.imageView1);
        animaciones(logo2);
        logo3 = findViewById(R.id.imageView3);
        animaciones(logo3);




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },3500);
    }

    private void animaciones(ImageView logo){


        animacionAlpha = ObjectAnimator.ofFloat(logo, View.ALPHA, 1.0F,0.0f);
        animacionAlpha.setDuration(500);
        AnimatorSet animatorSetAlpha = new AnimatorSet();
        animatorSetAlpha.play(animacionAlpha);
        animatorSetAlpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animacionAlphaReves = ObjectAnimator.ofFloat(logo, View.ALPHA, 0.0F,1.0f);
                animacionAlphaReves.setDuration(500);
                AnimatorSet animacionSetAlphaReves = new AnimatorSet();
                animacionSetAlphaReves.play(animacionAlphaReves);
                animacionSetAlphaReves.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {

                        animaciones(logo);
                    }

                });
                animacionSetAlphaReves.start();

            }
        });
        animatorSetAlpha.start();
    }



}