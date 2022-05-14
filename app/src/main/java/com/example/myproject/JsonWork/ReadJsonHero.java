package com.example.myproject.JsonWork;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myproject.SubClasses.Hero;
import com.example.myproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
            _inputHero.put("Popularity", hero.getPopularity());
            _inputHero.put("Money", hero.getMoney());
            _inputHero.put("FightSkills", hero.getFightSkill());
            _inputHero.put("ShopLvl", hero.getShopLvl());
            _inputHero.put("HasEquip", hero.isHasEquip());
            _inputHero.put("HasHorse", hero.isHasHorse());
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
        int popularity = _jsonHero.getInt("Popularity");
        int money = _jsonHero.getInt("Money");
        int fightSkill = _jsonHero.getInt("FightSkills");
        int shopLvl = _jsonHero.getInt("ShopLvl");
        boolean hasEquip = _jsonHero.getBoolean("HasEquip");
        boolean HasHorse = _jsonHero.getBoolean("HasHorse");

        Hero mHero = new Hero(fatherRel, popularity, money, fightSkill, shopLvl,hasEquip,HasHorse);
        return  mHero;
    }

    public static void OverWriteParams(Context context, Hero hero) throws  IOException, JSONException
    {
        sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(hero != null) {
            String JsonHero = HeroToJson(hero);

            editor.putString(SAVED_HERO, JsonHero);
            editor.commit();
        }
        else{
            editor.putString(SAVED_HERO, null);
            editor.commit();
        }
    }

    public static Hero SetNewGameHero(Context context) throws JSONException, IOException {
        String jsonText = readText(context, R.raw.hero);

        JSONObject _jsonRoot = new JSONObject(jsonText);
        int _fatherRel = _jsonRoot.getInt("FatherRelations");
        int _money = _jsonRoot.getInt("Money");
        int _popularity = _jsonRoot.getInt("Popularity");
        int _fightSkill = _jsonRoot.getInt("FightSkills");
        int shopLvl = _jsonRoot.getInt("ShopLvl");
        boolean hasEquip = _jsonRoot.getBoolean("HasEquip");
        boolean HasHorse = _jsonRoot.getBoolean("HasHorse");

        Hero _hero = new Hero(_fatherRel, _popularity, _money, _fightSkill, shopLvl,hasEquip,HasHorse);

        return _hero;
    }
}
