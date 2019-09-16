package com.example.victor.lyricsongs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;

public class see extends AppCompatActivity {

    //layout's views
    //TextView title;
    TextView lyrics;
    TextView artist;
    TextView feat;
    TextView album;
    TextView style;

    //variables
    String music_title = "Song's Title";
    String extra_fileName = "";
    String readed = "";
    String temp_lyrics = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see);

        getSupportActionBar().setTitle("Song's title");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //For the gradient action Bar
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_two));
        }

        readed = "";
        temp_lyrics = "";

        //title   = findViewById(R.id.see_title);
        lyrics  = findViewById(R.id.see_lyrics);
        artist  = findViewById(R.id.see_artist);
        feat    = findViewById(R.id.see_feat);
        album   = findViewById(R.id.see_album);
        style   = findViewById(R.id.see_style);

        if(getIntent().hasExtra("fileName")){
            extra_fileName = getIntent().getStringExtra("fileName");
            placeText();
            getSupportActionBar().setTitle(music_title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.see_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.modify:
                Intent otherActivity = new Intent(getApplicationContext(), add.class);
                //String extra_fileName = artist.getText().toString() + "_" + title.getText().toString();
                String extra_fileName = artist.getText().toString() + "_" + music_title;
                extra_fileName.trim();
                otherActivity.putExtra("fileName", extra_fileName);
                startActivity(otherActivity);

                return true;

            case R.id.share:
                String musicToShare = createList();
                sendMail(musicToShare);

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void placeText(){

        try {

            FileInputStream fis = openFileInput(extra_fileName);
            InputStreamReader isr = null;
            isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);

            String data = br.readLine();
            System.out.println("La phrase : " + data);
            String[] splittedData = data.split("&");
            System.out.println("Longueur tab : " + splittedData.length);

            System.out.println("Titre : " + splittedData[0]);
            System.out.println("Artiste : " + splittedData[1]);
            System.out.println("Feat : " + splittedData[2]);
            System.out.println("Album : " + splittedData[3]);
            System.out.println("Style : " + splittedData[4]);
            System.out.println("Lyrics : " + splittedData[5]);

            //title.setText(splittedData[0]);
            music_title = splittedData[0];
            artist.setText(splittedData[1]);
            feat.setText(splittedData[2]);
            album.setText(splittedData[3]);
            style.setText(splittedData[4]);

            temp_lyrics = splittedData[5];
            while ((readed = br.readLine()) != null){
                temp_lyrics += "\n" + readed;
                System.out.println("Readed : " + readed);
                System.out.println("temp_lyrics : " + temp_lyrics);
            }
            lyrics.setText(temp_lyrics);

        } catch (Exception e) {
            System.out.println("Exception see : " + e);
        }
    }



    public String createList(){
        String stringEmpty = "----";
        String informations = "";
        //informations += title.getText().toString() + "&";
        informations += music_title + "&";
        informations += artist.getText().toString() + "&";
        if (feat.getText().toString().equals("")){
            System.out.println("dans empty");
            informations += stringEmpty + "&";
        } else {
            informations += feat.getText().toString() + "&";
        }
        if (album.getText().toString().equals("")){
            System.out.println("dans empty");
            informations += stringEmpty + "&";
        } else {
            informations += album.getText().toString() + "&";
        }
        if (style.getText().toString().equals("")){
            System.out.println("dans empty");
            informations += stringEmpty + "&";
        } else {
            informations += style.getText().toString() + "&";
        }
        if ((lyrics.getText().toString().trim()).equals("")){
            informations += "No lyrics saved";
        } else {
            informations += lyrics.getText().toString();
        }

        System.out.println("Informations : " + informations);
        return informations;
    }

    private void sendMail(String msg){
        Intent emailIntent = new Intent((Intent.ACTION_SEND));
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.removeExtra(Intent.EXTRA_TEXT);
        emailIntent.putExtra(Intent.EXTRA_TEXT, msg);

        try{
            startActivity(Intent.createChooser(emailIntent, "choose an Email Client"));
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
