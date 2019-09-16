package com.example.victor.lyricsongs;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;

public class addBis extends AppCompatActivity {

    EditText musicInformations;
    Button importData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bis);

        getSupportActionBar().setTitle("Import song");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //For the gradient action Bar
        /*
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_two));
        }
        */

        musicInformations = findViewById(R.id.addBis_musicInformations);

        importData = findViewById(R.id.addBis_import);
        importData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicInformations.getText().toString().length()>1){
                    valider();
                } else {
                    Toast.makeText(addBis.this, "You forgot your text", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void valider(){

        String data = musicInformations.getText().toString();

        String[] splittedData = data.split("&");

        //The fileName is artist_title.trim() and the list is composed first with title then artist
        String filename = splittedData[1] + "_" + splittedData[0];
        filename.trim();

        String fileContents = data;
        FileOutputStream outputStream;

        if (splittedData.length == 6){
            try {
                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(fileContents.getBytes());
                outputStream.close();
                System.out.println("dans le try");
                Toast.makeText(this, "Music saved !", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                System.out.println("dans le catch");
                Toast.makeText(this, "Error, music not saved" + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            //If the add was a succes, redirect to mainActivity
            //finish();
            //We must use intent otherActivity because, if I use finish, in MainActivity the
            //onCreate is not call and so initArrayList, then the list is not up to date
            Intent otherActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(otherActivity);
        } else {
            Toast.makeText(addBis.this, "Wrong text format", Toast.LENGTH_SHORT).show();
        }


    }
}
