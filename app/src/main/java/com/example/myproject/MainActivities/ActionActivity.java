package com.example.myproject.MainActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.example.myproject.JsonWork.ReadJsonHero;
import com.example.myproject.JsonWork.ReadJsonScene;
import com.example.myproject.MusicServices.MusicService;
import com.example.myproject.R;

import org.json.JSONException;

import java.io.IOException;

public class ActionActivity extends AppCompatActivity implements FragmentSceneManager.AddMusic {
    private MediaPlayer _mPlayer;
    private Boolean _isAddMusicPlaying = false;
    private Boolean _hasPermissionToMusic = true;
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
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    protected void onResume() {
        super.onResume();

        CheckMusicPermission();

        if(_hasPermissionToMusic)
            SetMainMusic();

        SetScene();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_activity);

        CheckMusicPermission();

        blackout = findViewById(R.id.BackgroundBlackout);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//Set Portrait

        CheckPreferences();
    }

    private void CheckMusicPermission(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        String _accept = pref.getString("music", null);
        _hasPermissionToMusic = _accept != null && _accept.equals("включен");
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
        if(_hasPermissionToMusic) {
            int resID = getResources().getIdentifier(musicName, "raw", MainActivity.PACKAGE_NAME);
            _mPlayer = MediaPlayer.create(this, resID);
            _mPlayer.start();
            _isAddMusicPlaying = true;
        }
    }

    @Override
    public void StopAddMusic(){
        if (_isAddMusicPlaying) { //Выключение побочных звуков запущенных игрой
            _mPlayer.pause();
        }
    }

    private void CheckPreferences(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        String _size = pref.getString("size", null);
        if(_size != null){
            Resources res = getResources();
            Configuration configuration = new Configuration(res.getConfiguration());
            switch (_size){
                case "маленький":{
                    configuration.fontScale = 0.8f;
                    res.updateConfiguration(configuration, res.getDisplayMetrics());
                    break;
                }
                case "средний":{
                    configuration.fontScale = 1f;
                    res.updateConfiguration(configuration, res.getDisplayMetrics());
                    break;
                }
                case "большой":{
                    configuration.fontScale = 1.2f;
                    res.updateConfiguration(configuration, res.getDisplayMetrics());
                    break;
                }
            }
        }
    }
}
