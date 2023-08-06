package com.memorycardgame.views.activity;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.memorycardgame.views.fragment.ResultFragment;
import com.memorycardgame.views.fragment.HomeFragment;
import com.memorycardgame.R;
import com.memorycardgame.views.fragment.SplashFragment;


public class MainActivity extends BaseActivity {

    MediaPlayer mediaPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        transitFrag(R.id.fragment, new SplashFragment());

        playSound("memory_card_bg_tone.mpeg");


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.release();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.release();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        playSound("memory_card_bg_tone.mpeg");

    }


    //function for playing the sound from google
    void playSound(String fileName) {

        mediaPlayer = new MediaPlayer();
        try {
            AssetFileDescriptor afd = this.getAssets().openFd(fileName);

            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mediaPlayer.start();
    }

    @Override
    public void onBackPressed() {


        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
        if (currentFragment instanceof HomeFragment) {

            AlertDialog.Builder exit = new AlertDialog.Builder(this);
            exit.setTitle("Exit from application?");
            exit.setPositiveButton("Yes", (dialog, which) -> finish());
            exit.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            exit.show();
        } else if (currentFragment instanceof ResultFragment) {
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            super.onBackPressed();
        } else {
            super.onBackPressed();
        }

    }
}
