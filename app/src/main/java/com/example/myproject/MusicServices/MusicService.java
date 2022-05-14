package com.example.myproject.MusicServices;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import androidx.annotation.Nullable;

import com.example.myproject.R;

public class MusicService extends Service {
    private MediaPlayer _mPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        _mPlayer= MediaPlayer.create(this, R.raw.mainsoundtrack);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        _mPlayer.setLooping(true);
        _mPlayer.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _mPlayer.pause();
    }
}