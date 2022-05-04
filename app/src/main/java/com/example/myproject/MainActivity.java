package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Button _startButton, _continueButton;

    protected static Hero character; //Да не особо хорошо использовать static переменные, но так с ними
    protected static ArrayList<Event> chapterEvents; // удобней работать в внутри фрагмента.
    protected static Event currentEvent;

    public static String PACKAGE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PACKAGE_NAME = getPackageName();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//Set Portrait
        getSupportActionBar().hide(); //Удалим название проекта сверху

        _startButton = (Button) findViewById(R.id.startButton);
        _continueButton = (Button) findViewById(R.id.continueButton);

        String _titles = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("TITLES", null);
        _startButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                OverWriteParams(); // Перезапись sharedPrefs

                try {
                    chapterEvents = new ArrayList<Event>(ReadJsonScene.ReadNewSceneJSONFile(MainActivity.this));
                    character = ReadJsonHero.SetNewGameHero(MainActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                currentEvent = chapterEvents.get(0);
                Intent intent = new Intent(MainActivity.this, ActionActivity.class);
                startActivity(intent);
            }
        });

        if(_titles != null) {
            _continueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        character = ReadJsonHero.SetContinueHero(MainActivity.this);
                        chapterEvents = new ArrayList<Event>(ReadJsonScene.ReadContinueSceneJSONFile(MainActivity.this));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(_titles.contains("Добро пожаловать в Декруа") || _titles.contains("Пробуждение"))  // Да костыль
                        currentEvent = chapterEvents.get(0);
                    else
                        currentEvent = chapterEvents.get(RandomEvent());

                    Intent intent = new Intent(MainActivity.this, ActionActivity.class);
                    startActivity(intent);
                }
            });
        }else{
            _continueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MainActivity.this, "Ваш путь еще не начался...", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private int RandomEvent(){
        Random _rand = new Random();
        int _value = _rand.nextInt(chapterEvents.size());
        return _value;
    }

    private void OverWriteParams(){
        try {
            ReadJsonScene.OverWriteParams(MainActivity.this, null);
            ReadJsonHero.OverWriteParams(MainActivity.this, null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}