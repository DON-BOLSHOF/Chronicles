package com.example.myproject.MainActivities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myproject.JsonWork.ReadJsonHero;
import com.example.myproject.JsonWork.ReadJsonScene;
import com.example.myproject.R;
import com.example.myproject.SubClasses.Event;
import com.example.myproject.SubClasses.Hero;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static Hero character; //Да не особо хорошо использовать static переменные, но так с ними
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

        Button startButton = findViewById(R.id.startButton);
        Button continueButton =findViewById(R.id.continueButton);

        _titles = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("TITLES", null);
        startButton.setOnClickListener(view -> {

            if (_titles != null)
                OverWriteParams(); // Перезапись sharedPrefs

            try {
                chapterEvents = new ArrayList<>(ReadJsonScene.ReadSceneJSONFile(MainActivity.this));
                character = ReadJsonHero.SetNewGameHero(MainActivity.this);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            currentEvent = chapterEvents.get(0);
            Intent intent = new Intent(MainActivity.this, ActionActivity.class);
            startActivity(intent);
        });

        if(_titles != null) {
            continueButton.setOnClickListener(view -> {
                try {
                    character = ReadJsonHero.SetContinueHero(MainActivity.this);
                    chapterEvents = new ArrayList<>(ReadJsonScene.ReadSceneJSONFile(MainActivity.this));
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                if(_titles.contains("Добро пожаловать в Декруа") || _titles.contains("Пробуждение"))  // Да костыль
                    currentEvent = chapterEvents.get(0);
                else {
                    Random rand = new Random();
                    currentEvent = chapterEvents.get(rand.nextInt(chapterEvents.size()));
                }

                Intent intent = new Intent(MainActivity.this, ActionActivity.class);
                startActivity(intent);
            });
        }else{
            continueButton.setOnClickListener(view -> Toast.makeText(MainActivity.this, "Ваш путь еще не начался...", Toast.LENGTH_SHORT).show());
        }
    }

    private void OverWriteParams(){
        try {
            ReadJsonScene.OverWriteParams(MainActivity.this, null);
            ReadJsonHero.OverWriteParams(MainActivity.this, null);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}