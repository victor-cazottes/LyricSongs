package com.example.victor.lyricsongs;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String temp_title = "";
    String temp_artist = "";
    String temp_feat = "";
    String temp_style = "";

    List<Music> music = new ArrayList<Music>();

    //Vars
    private ArrayList<String> al_titles = new ArrayList<>();
    private ArrayList<String> al_artists = new ArrayList<>();
    private ArrayList<String> al_styles = new ArrayList<>();

    //for searchView
    private ArrayList<String> search_titles = new ArrayList<>();
    private ArrayList<String> search_artists = new ArrayList<>();
    private ArrayList<String> search_styles = new ArrayList<>();
    ArrayList<Music> newList = new ArrayList<>();
    ArrayList<Music> filtered_newList = new ArrayList<Music>();


    RecyclerViewAdapter adapter;

    FloatingActionButton fab_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //For the gradient action Bar
        /*
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_two));
        }
        */

        //Button to add music
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otherActivity = new Intent(getApplicationContext(), add.class);
                startActivity(otherActivity);
            }
        });

        //Drawer that appear from the left
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        initArrayList();


        //deleteAll();

    }


    public static class Music {
        private String music_title;
        private String music_artist;
        private String music_style;

        public Music(String music_title, String music_artist, String music_style) {
            this.music_title = music_title;
            this.music_artist = music_artist;
            this.music_style = music_style;
        }

        public String getMusic_artist() {
            return music_artist;
        }

        public String getMusic_style() {
            return music_style;
        }

        public String getMusic_title() {
            return music_title;
        }

        // Function to remove duplicates from an ArrayList
        public static ArrayList<Music> removeDuplicates(ArrayList<Music> list)
        {

            // Create a new ArrayList
            ArrayList<Music> newList = new ArrayList<Music>();

            // Traverse through the first list
            for (Music element : list) {

                // If this element is not present in newList
                // then add it
                if (!newList.contains(element)) {
                    newList.add(element);
                }
            }

            // return the new list
            return newList;
        }

    }

    public void initArrayList(){

        System.out.println("Debut");
        File internalFiles = getFilesDir();
        File[] fs = internalFiles.listFiles();
        System.out.println("Nb de fichiers : " + fs.length);

        for (int i=0; i<fs.length; i++){
            try{

                if (fs[i].getName().equals("instant-run")){
                    System.out.println("Do nothing");
                } else {
                    FileInputStream fis = openFileInput(fs[i].getName());
                    InputStreamReader isr = new InputStreamReader(fis, "UTF-8");

                    BufferedReader br = new BufferedReader(isr);

                    String data = br.readLine();
                    String[] splittedData = data.split("&");

                    temp_title  = splittedData[0];
                    temp_artist = splittedData[1];
                    temp_feat   = splittedData[2];
                    temp_style  = splittedData[4];

                    al_titles.add(temp_title);
                    al_artists.add(temp_artist);
                    al_styles.add(temp_style);
                }

            } catch (Exception e){
                System.out.println("Exception MainActivity : " + e);
            }

        }

        sortByStyle(al_titles, al_artists, al_styles);

    }

    private void deleteAll(){

        File internalFiles = getFilesDir();
        File[] fs = internalFiles.listFiles();

        for (int i=0; i<fs.length; i++){
            boolean exist = fs[i].exists();
            boolean deletd = fs[i].delete();
            System.out.println("exists : " + exist);
            System.out.println("Delete : " + deletd);
        }
    }

    private void initRecyclerView(ArrayList tit, ArrayList art, ArrayList sty){
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new RecyclerViewAdapter(this, tit, art, sty);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Separation
        DividerItemDecoration horizontalSeparation = new DividerItemDecoration(this, layoutManager.getOrientation());
        recyclerView.addItemDecoration(horizontalSeparation);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.menu_searchView);
        android.widget.SearchView searchView = (android.widget.SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //??? Il me faut override la method getFilter dans mon recyclerView
                //adapter.getFilter().filter(s);

                String userInput = s.toLowerCase();
                System.out.println("userInput : " + userInput);
                newList.clear();
                System.out.println("NewList before ::: " + newList.toString());

                //Search by artists
                for (int i=0; i<al_artists.size(); i++){
                    String artist = al_artists.get(i);
                    String title = al_titles.get(i);
                    String style = al_styles.get(i);
                    if (artist.toLowerCase().contains(userInput)
                            || title.toLowerCase().contains(userInput)
                            || style.toLowerCase().contains(userInput)){
                        Music filtered_music = new Music(al_titles.get(i), al_artists.get(i), al_styles.get(i));
                        newList.add(filtered_music);
                    }
                }
                System.out.println("NewList after ::: ");
                for (Music music : newList){
                    System.out.println(music.getMusic_title());
                }

                //We delete duplicated muscis
                filtered_newList.clear();
                System.out.println("filtered_newList before ::: " + filtered_newList.toString());
                filtered_newList = Music.removeDuplicates(newList);
                System.out.println("filtered_newList after ::: ");
                for (Music music : filtered_newList){
                    System.out.println(music.getMusic_title());
                }

                search_titles.clear();
                search_artists.clear();
                search_styles.clear();

                for (int i=0; i<filtered_newList.size(); i++){
                    search_artists.add( filtered_newList.get(i).getMusic_artist());
                    search_titles.add( filtered_newList.get(i).getMusic_title());
                    search_styles.add( filtered_newList.get(i).getMusic_style());
                }

                initRecyclerView(search_titles, search_artists, search_styles);

                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_settings) {
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sort_artist) {
            sortByArtist(al_titles, al_artists, al_styles);
        } else if (id == R.id.nav_sort_music) {
            sortByTitle(al_titles, al_artists, al_styles);
        } else if (id == R.id.nav_sort_style) {
            sortByStyle(al_titles, al_artists, al_styles);
        }// else if (id == R.id.people) {

        //}

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public void sortByArtist(ArrayList titles, ArrayList artists, ArrayList styles){
        music.clear();

        for (int i=0; i<artists.size(); i++){
            Music newMusic = new Music(titles.get(i).toString(), artists.get(i).toString(), styles.get(i).toString());
            music.add(newMusic);
        }

        Collections.sort(music, new Comparator<Music>() {
            @Override
            public int compare(Music m1, Music m2) {
                return m1.getMusic_artist().toLowerCase().trim().compareTo(m2.getMusic_artist().toLowerCase().trim());
            }
        });

        artists.clear();
        titles.clear();
        styles.clear();

        for (int i=0; i<music.size(); i++){
            artists.add( music.get(i).music_artist);
            titles.add( music.get(i).music_title);
            styles.add( music.get(i).music_style);
        }

        initRecyclerView(titles, artists, styles);
    }

    public void sortByTitle(ArrayList titles, ArrayList artists, ArrayList styles){
        music.clear();

        for (int i=0; i<artists.size(); i++){
            Music newMusic = new Music(titles.get(i).toString(), artists.get(i).toString(), styles.get(i).toString());
            music.add(newMusic);
        }

        Collections.sort(music, new Comparator<Music>() {
            @Override
            public int compare(Music m1, Music m2) {
                return m1.getMusic_title().toLowerCase().trim().compareTo(m2.getMusic_title().toLowerCase().trim());
            }
        });

        artists.clear();
        titles.clear();
        styles.clear();

        for (int i=0; i<music.size(); i++){
            artists.add( music.get(i).music_artist);
            titles.add( music.get(i).music_title);
            styles.add( music.get(i).music_style);
        }

        initRecyclerView(titles, artists, styles);
    }

    public void sortByStyle(ArrayList titles, ArrayList artists, ArrayList styles){
        music.clear();

        for (int i=0; i<artists.size(); i++){
            Music newMusic = new Music(titles.get(i).toString(), artists.get(i).toString(), styles.get(i).toString());
            music.add(newMusic);
        }

        Collections.sort(music, new Comparator<Music>() {
            @Override
            public int compare(Music m1, Music m2) {
                return m1.getMusic_style().toLowerCase().trim().compareTo(m2.getMusic_style().toLowerCase().trim());
            }
        });

        artists.clear();
        titles.clear();
        styles.clear();

        for (int i=0; i<music.size(); i++){
            artists.add( music.get(i).music_artist);
            titles.add( music.get(i).music_title);
            styles.add( music.get(i).music_style);
        }

        initRecyclerView(titles, artists, styles);
    }


}
