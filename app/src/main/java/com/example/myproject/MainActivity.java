package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    protected static Hero character; //Да не особо хорошо использовать static переменные, но так с ними
    protected static ArrayList<Event> chapterEvents; // удобней работать в внутри фрагмента.
    protected static Event currentEvent;

    public static String PACKAGE_NAME;

    private String _titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PACKAGE_NAME = getPackageName();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//Set Portrait

        Button _startButton = (Button) findViewById(R.id.startButton);
        Button _continueButton = (Button) findViewById(R.id.continueButton);

        _titles = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("TITLES", null);
        _startButton.setOnClickListener(view -> {

            OverWriteParams(); // Перезапись sharedPrefs

            try {
                chapterEvents = new ArrayList<>(ReadJsonScene.ReadNewSceneJSONFile(MainActivity.this));
                character = ReadJsonHero.SetNewGameHero(MainActivity.this);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            currentEvent = chapterEvents.get(0);
            Intent intent = new Intent(MainActivity.this, ActionActivity.class);
            startActivity(intent);
        });

        if(_titles != null) {
            _continueButton.setOnClickListener(view -> {
                try {
                    character = ReadJsonHero.SetContinueHero(MainActivity.this);
                    chapterEvents = new ArrayList<>(ReadJsonScene.ReadContinueSceneJSONFile(MainActivity.this));
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                if(_titles.contains("Добро пожаловать в Декруа") || _titles.contains("Пробуждение"))  // Да костыль
                    currentEvent = chapterEvents.get(0);
                else
                    currentEvent = chapterEvents.get(RandomEvent());

                Intent intent = new Intent(MainActivity.this, ActionActivity.class);
                startActivity(intent);
            });
        }else{
            _continueButton.setOnClickListener(view -> Toast.makeText(MainActivity.this, "Ваш путь еще не начался...", Toast.LENGTH_SHORT).show());
        }
    }

    private int RandomEvent(){
        Random _rand = new Random();
        return _rand.nextInt(chapterEvents.size());
    }

    private void OverWriteParams(){
        try {
            ReadJsonScene.OverWriteParams(MainActivity.this, null);
            ReadJsonHero.OverWriteParams(MainActivity.this, null);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}