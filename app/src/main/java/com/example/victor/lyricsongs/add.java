package com.example.victor.lyricsongs;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class add extends AppCompatActivity {

    //Layout's views
    EditText title;
    AutoCompleteTextView artist;
    AutoCompleteTextView feat;
    AutoCompleteTextView album;
    AutoCompleteTextView style;
    EditText lyrics;

    Button cancel;
    Button validate;

    //Variables
    String extra_fileName = "";
    String extra_context;
    String readed = "";
    String temp_lyrics = "";
    Boolean editMode = false;

    //for the autoComplete
    public ArrayList<String> list_artists = new ArrayList<>();
    public ArrayList<String> filtered_list_artists = new ArrayList<>();
    public ArrayList<String> filtered_list_feat = new ArrayList<>();

    public ArrayList<String> list_albums = new ArrayList<>();
    public ArrayList<String> filtered_list_albums = new ArrayList<>();

    public ArrayList<String> list_styles = new ArrayList<>();
    public ArrayList<String> filtered_list_styles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        getSupportActionBar().setTitle("Edit song");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //For the gradient action Bar
        /*
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_two));
        }
        */

        title   = findViewById(R.id.add_title);
        artist  = findViewById(R.id.add_artist);
        feat    = findViewById(R.id.add_feat);
        album   = findViewById(R.id.add_album);
        style   = findViewById(R.id.add_style);
        lyrics  = findViewById(R.id.et_lyrics);

        this.cancel     = findViewById(R.id.button_cancel);
        this.validate    = findViewById(R.id.button_validate);

        this.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(otherActivity);
            }
        });

        this.validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (artist.getText().toString().equals("") || title.getText().toString().equals("")){
                    Snackbar.make(v, "Title and Artist can't be empty", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    validate();
                }
            }
        });

        setFilters();

        if(getIntent().hasExtra("fileName")){
            extra_fileName = getIntent().getStringExtra("fileName");
            editMode = true;
            placeText();
        }

        completeAutoCompleteList();
        //for Artists
        final ArrayAdapter adapterArtists = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filtered_list_artists);
        artist.setAdapter(adapterArtists);
        System.out.println("Artists size :: " + filtered_list_artists.size());
        artist.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int indice = 10000;
                filtered_list_feat.clear();
                for (int j=0; j<filtered_list_artists.size(); j++){
                    filtered_list_feat.add(filtered_list_artists.get(j));
                }
                System.out.println("The list    : " + filtered_list_feat.toString());
                System.out.println("The listBis : " + filtered_list_artists.toString());
                for (int i=0; i<filtered_list_feat.size(); i++){
                    System.out.println("::::" + filtered_list_feat.get(i) + " : " + artist.getText().toString());
                    if (filtered_list_feat.get(i).equals(artist.getText().toString())){
                        String tempArtist = "temp";
                        tempArtist = filtered_list_feat.get(i);
                        System.out.println("Removed ... :D");
                        System.out.println("Artist size ::: " + filtered_list_artists.size());
                        System.out.println("Feat size b::: " + filtered_list_feat.size());
                        System.out.println("la liste : " + filtered_list_feat.toString());
                        //filtered_list_feat.remove(i);//???
                        indice = i;

                        ArrayAdapter adapter2 = new ArrayAdapter<String>(add.this, android.R.layout.simple_list_item_1, filtered_list_feat);
                        feat.setAdapter(adapter2);
                        System.out.println("Feat size d::: " + filtered_list_feat.size());
                        //filtered_list_feat.add(i, tempArtist);//???
                        System.out.println("Feat size a::: " + filtered_list_feat.size());
                        System.out.println("la liste2 : " + filtered_list_feat.toString());
                        //??? J'eneleve de la liste le chanteur mis dans la case artiste pour pas qu'un chanteur face un feat
                        //avec lui-même, je "setAdapter" puis APRES je remet le chanteur dans la liste et il apparait
                        //quand même dans les propositions alors qu'au moment de la création de l'adapter il y était pas

                    }
                }
                if(!(indice == 10000)){
                    System.out.println("indice : " + indice);
                    filtered_list_feat.remove(indice);//???
                } else {
                    System.out.println("rip");
                }

            }
        });


        //For albums
        final ArrayAdapter adapterAlbums = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filtered_list_albums);
        album.setAdapter(adapterAlbums);

        //For styles
        final ArrayAdapter adapterStyles = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filtered_list_styles);
        style.setAdapter(adapterStyles);

        //System.out.println("Chocolat");
        //System.out.println("Artists size ::: " + filtered_list_artists.size());
        //feat.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (editMode){
            System.out.println("No toolbar");
        } else {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.add_menu, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addBis:
                Intent otherActivity = new Intent(getApplicationContext(), addBis.class);
                startActivity(otherActivity);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void setFilters(){
        //Text Filter for title / artist / Fit (so we don't have &#;!/...)
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {

                if (source instanceof SpannableStringBuilder) {
                    SpannableStringBuilder sourceAsSpannableBuilder = (SpannableStringBuilder)source;
                    for (int i = end - 1; i >= start; i--) {
                        char currentChar = source.charAt(i);
                        if (!Character.isLetterOrDigit(currentChar) && !Character.isSpaceChar(currentChar)) {
                            sourceAsSpannableBuilder.delete(i, i+1);
                        }
                    }
                    return source;
                } else {
                    StringBuilder filteredStringBuilder = new StringBuilder();
                    for (int i = start; i < end; i++) {
                        char currentChar = source.charAt(i);
                        if (Character.isLetterOrDigit(currentChar) || Character.isSpaceChar(currentChar)) {
                            filteredStringBuilder.append(currentChar);
                        }
                    }
                    return filteredStringBuilder.toString();
                }
            }
        };
        title.setFilters(new InputFilter[] { filter });
        artist.setFilters(new InputFilter[] { filter });
        feat.setFilters(new InputFilter[] { filter });
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

            title.setText(splittedData[0]);
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

    //Complete the list for the AutoCompleteTextView
    public void completeAutoCompleteList(){
        //complete the artist list porpositions
        File internalFiles = getFilesDir();
        File[] fs = internalFiles.listFiles();
        System.out.println("Nb de fichiers : " + fs.length);

        for (int i=0; i<fs.length; i++) {
            FileInputStream fis = null;
            try {
                fis = openFileInput(fs[i].getName());
                InputStreamReader isr = new InputStreamReader(fis, "UTF-8");

                BufferedReader br = new BufferedReader(isr);

                String data = br.readLine();
                String[] splittedData = data.split("&");

                //we ad the artist name to the list
                list_artists.add(splittedData[1]);
                list_artists.add(splittedData[2]);

                list_albums.add(splittedData[3]);

                list_styles.add(splittedData[4]);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        filtered_list_artists.clear();
        filtered_list_artists = removeDuplicates(list_artists);

        filtered_list_albums.clear();
        filtered_list_albums = removeDuplicates(list_albums);

        filtered_list_styles.clear();
        filtered_list_styles = removeDuplicates(list_styles);
    }

    // Function to remove duplicates from an ArrayList
    public static ArrayList<String> removeDuplicates(ArrayList<String> list)
    {

        // Create a new ArrayList
        ArrayList<String> newList = new ArrayList<>();

        // Traverse through the first list
        for (String element : list) {

            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {
                newList.add(element);
            }
        }

        // return the new list
        return newList;
    }

    public void validate() {
        String infos = createList();

        String filename = artist.getText().toString() + "_" + title.getText().toString();
        filename.trim();

        //delete old one to avoid duplicated songs
        if (!extra_fileName.equals(filename)){
            //??? Retourne tourjours "false"
            boolean deleted = deleteFile(filename);
            System.out.println("Fichier supprémié : " + deleted);
        }

        String fileContents = infos;
        FileOutputStream outputStream;

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
    }

    public String createList(){
        String stringEmpty = "----";
        String informations = "";
        informations += title.getText().toString() + "&";
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


}
