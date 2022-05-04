package com.example.myproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;

import java.io.IOException;

public class ActionActivity extends AppCompatActivity implements FragmentSceneManager.AddMusic {
    private MediaPlayer _mPlayer;
    private Boolean _isAddMusicPlaying = false;
    protected static ImageView blackout;

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected void onPause() {
        super.onPause();

        Intent intent = new Intent(this, MusicService.class); //Выключение саундрека
        stopService(intent);

        StopAddMusic();

        try {
            ReadJsonScene.OverWriteParams(this, MainActivity.chapterEvents);
            ReadJsonHero.OverWriteParams(this, MainActivity.character);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void onResume() {
        super.onResume();
        SetMainMusic();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_activity);

        blackout = findViewById(R.id.BackgroundBlackout);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//Set Portrait
        getSupportActionBar().hide(); //Удалим название проекта сверху

        SetScene();
    }

    private void SetScene(){
        FragmentSceneManager _scene = new FragmentSceneManager(this);
        FragmentTransaction _ft = getSupportFragmentManager().beginTransaction();
        _ft.replace(R.id.sceneLayout, _scene);
        _ft.commit();
    }

    private  void SetMainMusic(){
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
    }

    @Override
    public void SetAddMusic(String musicName){
        int resID = getResources().getIdentifier(musicName , "raw", MainActivity.PACKAGE_NAME);
        _mPlayer = MediaPlayer.create(this, resID);
        _mPlayer.start();
        _isAddMusicPlaying = true;
    }

    @Override
    public void StopAddMusic(){
        if (_isAddMusicPlaying) { //Выключение побочных звуков запущенных игрой
            _mPlayer.pause();
        }
    }
}
