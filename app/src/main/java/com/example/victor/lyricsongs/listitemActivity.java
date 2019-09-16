package com.example.victor.lyricsongs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class listitemActivity extends AppCompatActivity {

    TextView title;
    TextView artist;
    TextView style;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_listitem);

        title = findViewById(R.id.song_title);
        artist = findViewById(R.id.song_artist);
        style = findViewById(R.id.song_style);

    }
}
