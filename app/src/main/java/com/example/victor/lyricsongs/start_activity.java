package com.example.victor.lyricsongs;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class start_activity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1000;

    private RelativeLayout start_background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_activity);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent otherActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(otherActivity);
                finish();
            }
        }, SPLASH_TIME_OUT);

        //To set the background gradient :
        start_background = findViewById(R.id.start_background);
        //start_background.setBackgroundResource(R.drawable.gradient_one);


    }
}
