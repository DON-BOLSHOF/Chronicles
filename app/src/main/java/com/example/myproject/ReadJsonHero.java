package com.example.myproject;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ReadJsonHero {
    private static SharedPreferences sharedPreferences;
    private static final String SAVED_HERO = "HERO";

    public static Hero SetContinueHero(Context context) throws IOException, JSONException {
        Hero _hero;

        _hero = ReadHeroJson(context);

        return _hero;
    }

    private static String readText(Context context, int resId) throws IOException {
        InputStream _is = context.getResources().openRawResource(resId);
        BufferedReader _br= new BufferedReader(new InputStreamReader(_is));
        StringBuilder _sb= new StringBuilder();
        String s= null;
        while((  s = _br.readLine())!=null) {
            _sb.append(s);
            _sb.append("\n");
        }
        return _sb.toString();
    }

    private static String HeroToJson(Hero hero){
        JSONObject _inputHero = new JSONObject();
        try {
            _inputHero.put("FatherRelations", hero.getFatherRel());
            _inputHero.put("Reputation", hero.getPopularity());
            _inputHero.put("Money", hero.getMoney());
            _inputHero.put("FightSkills", hero.getFightSkill());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  _inputHero.toString();
    }

    protected static String ReadSharedHero(Context context){
        sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);

        if (sharedPreferences != null) {
            String savedText = sharedPreferences.getString(SAVED_HERO, null);
            return savedText;
        }else
            return null;
    }

    public static Hero ReadHeroJson(Context context) throws JSONException {
        String _hero = ReadSharedHero(context);
        if(_hero == null)
            return null;

        JSONObject _jsonHero = new JSONObject(_hero);

        int fatherRel = _jsonHero.getInt("FatherRelations");
        int popularity = _jsonHero.getInt("Reputation");
        int money = _jsonHero.getInt("Money");
        int fightSkill = _jsonHero.getInt("FightSkills");

        Hero mHero = new Hero(fatherRel, popularity, money, fightSkill);
        return  mHero;
    }

    public static void OverWriteParams(Context context, Hero hero) throws  IOException, JSONException
    {
        sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String JsonHero = HeroToJson(hero);

        editor.putString(SAVED_HERO, JsonHero);
        editor.commit();
    }

    public static Hero SetNewGameHero(Context context) throws JSONException, IOException {
        String jsonText = readText(context, R.raw.hero);

        JSONObject _jsonRoot = new JSONObject(jsonText);
        int _fatherRel = _jsonRoot.getInt("FatherRelations");
        int _money = _jsonRoot.getInt("Money");
        int _rep = _jsonRoot.getInt("Reputation");
        int _fightSkill = _jsonRoot.getInt("FightSkills");
        Hero _hero = new Hero(_fatherRel, _rep, _money, _fightSkill);
        return _hero;
    }
}
