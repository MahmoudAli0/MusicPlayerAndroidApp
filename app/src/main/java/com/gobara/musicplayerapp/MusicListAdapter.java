package com.gobara.musicplayerapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.viewholder>  {
    ArrayList<MediaModel> songesList;
    ArrayList<String> songesListAll;
   static  Context context;
   private int position ;

    public MusicListAdapter(ArrayList<MediaModel> songesList, Context context) {
        this.songesList = songesList;
        this.context = context;
        this.songesListAll =new ArrayList<>();
    }



    @Override
    public viewholder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recyecler_item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder( viewholder holder, int position) {

        MediaModel songesData=songesList.get(position);
        String songesTittle=songesData.getName();
        holder.musicTitle.setText(songesData.getName());

        if(myMadiaPlayer.currentIndex==position){
            holder.musicTitle.setTextColor(Color.parseColor("#FF0000"));
        }else{
            holder.musicTitle.setTextColor(Color.parseColor("#000000"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myMadiaPlayer.getInstance().reset();
                myMadiaPlayer.currentIndex= position;
                Intent inn=new Intent(context,musicPlayerActivity.class);

                inn.putExtra("LIST",songesList);
                inn.setFlags(inn.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(inn);
            }
        });

    }
    @Override
    public int getItemCount() {

        return songesList.size();
    }
    public void filterdList(ArrayList<MediaModel> filteList){
        songesList=  filteList;
        notifyDataSetChanged();

    }

    public class viewholder extends RecyclerView.ViewHolder {
//View.OnCreateContextMenuListener
        TextView musicTitle;
        ImageView musicImage,showPopMenu11;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            musicTitle=itemView.findViewById(R.id.music_title);
            musicImage=itemView.findViewById(R.id.icon_view);
            //showPopMenu(showPopMenu11);
        }

    }
}
