package com.example.victor.lyricsongs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.nio.file.Files;
import java.util.ArrayList;

import java.io.File;
import java.io.FileInputStream;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> al_titles = new ArrayList<>();
    private ArrayList<String> al_artists = new ArrayList<>();
    private ArrayList<String> al_styles = new ArrayList<>();
    private Context myContext;

    String extra_fileName;

    public RecyclerViewAdapter(Context context, ArrayList<String> titles, ArrayList<String> artists, ArrayList<String> styles){
        this.al_titles = titles;
        this.al_artists = artists;
        this.al_styles = styles;
        this.myContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {

        viewHolder.song_title.setText(al_titles.get(position));
        viewHolder.song_artist.setText(al_artists.get(position));
        viewHolder.song_style.setText(al_styles.get(position));

        changeColorStyle(viewHolder.song_style, al_styles.get(position));


        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(myContext, al_titles.get(position), Toast.LENGTH_SHORT).show();

                Intent otherActivity = new Intent(myContext, see.class);
                extra_fileName = al_artists.get(position) + "_" + al_titles.get(position);
                extra_fileName.trim();
                otherActivity.putExtra("fileName", extra_fileName);
                myContext.startActivity(otherActivity);

            }
        });

        viewHolder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                extra_fileName = al_artists.get(position) + "_" + al_titles.get(position);
                extra_fileName.trim();

                AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
                builder.setCancelable(true);
                builder.setTitle("Warning !");

                LinearLayout layout_parent = (LinearLayout) v.getParent();
                builder.setMessage("Are you sure you want to delete : \n\""
                        + al_titles.get(position) + "\" of \"" + al_artists.get(position) + "\"");

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String delete_alert = al_titles.get(position) + " deleted";
                        //removeItem(position);
                        //Hide/setVisible(false) to the viewHolder

                        //FileName
                        extra_fileName = al_artists.get(position) + "_" + al_titles.get(position);
                        extra_fileName.trim();

                        Boolean deleted = myContext.deleteFile(extra_fileName);
                        System.out.println("IsDeleted : " + deleted);
                        notifyDataSetChanged();
                        //??? Il faut appeler iniArrayList mais comment faire ? public puis static
                        //MainActivity.initArrayList(); //Methode 1 pas concluante

                        //Methode 3
                        //https://stackoverflow.com/questions/31367599/how-to-update-recyclerview-adapter-data
                        //MainActivity mainAct = new MainActivity();
                        //mainAct.recy

                        Toast.makeText(myContext, delete_alert, Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
                return true;
            }
        });
    }

    public void removeItem(int position) {
        al_titles.remove(position);
        al_artists.remove(position);
        al_styles.remove(position);
        notifyItemRemoved(position);
    }

    public void changeColorStyle(TextView tv, String style){
        style.toLowerCase();
        style.trim();
        switch(style) {
            case "rap" :
                //??? On peut pas utiliser getResources...
                //viewHolder.song_style.setTextColor(getResources().getColor(R.color.black));
                break;

            case "pop" :
                //viewHolder.song_style.setTextColor(getResources().getColor(R.color.light_orange));
                break;

            case "raggae" :
                //viewHolder.song_style.setTextColor(getResources().getColor(R.color.dark_green));
                break;

            case "blues" :
                //viewHolder.song_style.setTextColor(getResources().getColor(R.color.dark_blue));
                break;

            // You can have any number of case statements.
            default : // Optional
                // Statements
        }
    }

    @Override
    public int getItemCount() {
        return al_titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView song_title;
        TextView song_artist;
        TextView song_style;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            song_title = itemView.findViewById(R.id.song_title);
            song_artist = itemView.findViewById(R.id.song_artist);
            song_style = itemView.findViewById(R.id.song_style);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }



}
