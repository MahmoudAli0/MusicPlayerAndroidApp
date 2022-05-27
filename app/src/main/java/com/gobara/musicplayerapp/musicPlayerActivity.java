package com.gobara.musicplayerapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class musicPlayerActivity extends AppCompatActivity {

    TextView titleSong,current_time,totalTime;
    SeekBar seekBar;
    ImageView title_img,nextImage,previous_img,pause_Img;
    ArrayList<MediaModel> songList;
    MediaModel currentSong;
    MediaPlayer mediaPlayer= myMadiaPlayer.getInstance();
    private RelativeLayout musicl;
    int x=0;
    public static final String CHANEL_ID="m_chanel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        getRefernces();
        setResWithMusic();
        musicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!= null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    current_time.setText(ConverToMMS(mediaPlayer.getCurrentPosition()+""));
                    if(mediaPlayer.isPlaying()){
                        pause_Img.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                        title_img.setRotation(x++);

                    }else{
                        pause_Img.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                            NotificationChannel channel= new NotificationChannel(CHANEL_ID,"music_chanel",
                                    NotificationManager.IMPORTANCE_DEFAULT);
                            NotificationManager nm= getSystemService(NotificationManager.class);
                            nm.createNotificationChannels(Collections.singletonList(channel));

                        }
                        Intent intent= new Intent(getApplicationContext(),MainActivity.class);

                        PendingIntent pi= PendingIntent.getActivity(getApplicationContext(),0,intent,0);
                        NotificationCompat.Builder builder= new NotificationCompat.Builder(getApplicationContext(),CHANEL_ID);
                        builder.setSmallIcon(R.drawable.music_logo_icon).setContentTitle("the song is paused").
                                setContentText("click on play to play the music again").setPriority(NotificationCompat.PRIORITY_HIGH)
                                .addAction(R.drawable.music_logo_icon,"play",pi);
                        NotificationManagerCompat nmc= NotificationManagerCompat.from(getApplicationContext());
                        nmc.notify(1,builder.build());
                        title_img.setRotation(0);

                    }
                }
                new Handler().postDelayed(this,100);

            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mediaPlayer!=null && b){
                    mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
    private void load_setting(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean check_night = sp.getBoolean("NIGHT", false);
        if (check_night) {
            musicl.setBackgroundColor(Color.parseColor("#222222"));

        } else {
            musicl.setBackgroundColor(Color.parseColor("#ffffff"));
            pause_Img.setColorFilter(R.color.black, PorterDuff.Mode.SRC_ATOP);
            previous_img.setColorFilter(R.color.black, PorterDuff.Mode.SRC_ATOP);
            nextImage.setColorFilter(R.color.black, PorterDuff.Mode.SRC_ATOP);
            titleSong.setTextColor(Color.parseColor("#000000"));
            current_time.setTextColor(Color.parseColor("#000000"));
            totalTime.setTextColor(Color.parseColor("#000000"));
        }
        String orientation = sp.getString("ORIENTATION", "false");
        if ("1".equals(orientation)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
        } else if ("2".equals(orientation)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if ("3".equals(orientation)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }
    void setResWithMusic(){
        currentSong=songList.get(myMadiaPlayer.currentIndex);
        titleSong.setText(currentSong.getName());
        totalTime.setText(ConverToMMS(currentSong.getDuration()));

        pause_Img.setOnClickListener(v-> pausePlay());
        previous_img.setOnClickListener(v-> playPreviousMusic());
        nextImage.setOnClickListener(v-> playNextMusic());
        playMusic();

    }

    private void playMusic(){
    try{
        mediaPlayer.reset();
        mediaPlayer.setDataSource(currentSong.getPath());
        mediaPlayer.prepare();
        mediaPlayer.start();
        seekBar.setProgress(0);
        seekBar.setMax(mediaPlayer.getDuration());
       }
    catch (IOException E)
    {
    E.printStackTrace();
       }
    }
    private void playNextMusic(){

        if(myMadiaPlayer.currentIndex==songList.size()-1)
            return;
        myMadiaPlayer.currentIndex+=1;
        mediaPlayer.reset();
        setResWithMusic();
    }
    private void playPreviousMusic(){

        if(myMadiaPlayer.currentIndex==0)
            return;
        myMadiaPlayer.currentIndex -=1;
        mediaPlayer.reset();
        setResWithMusic();
    }
    private void pausePlay(){

        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();

        else
            mediaPlayer.start();
    }
    public static String ConverToMMS(String duration){
        Long Mills=Long.parseLong(duration);
       return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(Mills)%
                TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(Mills)%TimeUnit.MINUTES.toSeconds(1));
    }

    @Override
    protected void onResume() {
        load_setting();
        super.onResume();
    }
    public void getRefernces(){
        titleSong=findViewById(R.id.song_title);
        current_time=findViewById(R.id.current_time);
        totalTime=findViewById(R.id.total_time);
        seekBar=findViewById(R.id.seekpar);
        title_img=findViewById(R.id.music_icon_pac);
        nextImage=findViewById(R.id.next);
        previous_img=findViewById(R.id.previous);
        pause_Img=findViewById(R.id.pause);
        titleSong.setSelected(true);
        musicl = findViewById(R.id.musicl);
        load_setting();
        songList=(ArrayList<MediaModel>) getIntent().getSerializableExtra("LIST");
    }
}