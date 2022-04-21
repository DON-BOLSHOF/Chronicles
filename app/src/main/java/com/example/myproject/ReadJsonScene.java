package com.example.myproject;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ReadJsonScene {
    private static SharedPreferences sharedPreferences;
    private static final String SAVED_TITLES = "TITLES";

    public static ArrayList<Event> ReadContinueSceneJSONFile(Context context) throws IOException, JSONException {

        // Read content of company.json
        String jsonText = ReadText(context, R.raw.sorce);

        JSONObject _jsonRoot = new JSONObject(jsonText);
        JSONArray _jsonEvents = _jsonRoot.getJSONArray("Event");

        String _titles = ReadSharedPrefTitles(context);

        ArrayList<Event> events = new ArrayList<Event>();

        for (int i = 0; i< _jsonEvents.length(); i++) {
            JSONObject _jsonEvent = _jsonEvents.getJSONObject(i);

            Event _event = ReadEvent(_jsonEvent);

            if (_titles.contains(_event.get_titleName()))
                events.add(_event);

        }

        return events;
    }

    public static  ArrayList<Event> ReadNewSceneJSONFile(Context context) throws IOException, JSONException{
        String jsonText = ReadText(context, R.raw.sorce);

        JSONObject _jsonRoot = new JSONObject(jsonText);
        JSONArray _jsonEvents = _jsonRoot.getJSONArray("Event");

        ArrayList<Event> events = new ArrayList<Event>();

        for (int i = 0; i< _jsonEvents.length(); i++) {
            JSONObject _jsonEvent = _jsonEvents.getJSONObject(i);

            Event _event = ReadEvent(_jsonEvent);

            events.add(_event);
        }

        return events;
    }

    public static void OverWriteParams(Context context, ArrayList<Event> events) throws  IOException, JSONException{

        JSONArray _lastEventsNames = new JSONArray();

        for (int i = 0;i <events.size(); i++){
            _lastEventsNames.put(events.get(i).get_titleName());
        }

        sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SAVED_TITLES, _lastEventsNames.toString());
        editor.commit();
    }

    protected static String ReadSharedPrefTitles(Context context){
        sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);

        if (sharedPreferences != null) {
            String savedText = sharedPreferences.getString(SAVED_TITLES, null);
            return savedText;
        }else
            return null;
    }

    private static CustomButton ReadButtonJson(JSONObject button) throws  JSONException {
        String[][] changed = null;

        String buttonName = button.getString("ButtonName");
        JSONObject reaction = button.getJSONObject("EventReaction");
        String reactTitle = reaction.getString("Title");
        String reactText = reaction.getString("ReactionText");

        JSONObject reacEvent = reaction.getJSONObject("ParameterReact");
        JSONArray willChanged = reacEvent.getJSONArray("WillChanged");
        JSONArray willChangedFor = reacEvent.getJSONArray("WillChangedFor");
        changed = new String[willChanged.length()][2]; // [Имя][Значение] размерность одинаковая будет

        for(int z = 0;z<willChanged.length();z++){
            String reactParam = willChanged.getString(z);
            if(reactParam.equals("Nothing")) {
                changed = null;
                break;
            }
            String reactValue = willChangedFor.getString(z);
            changed[z][0] = reactParam;
            changed[z][1] = reactValue;
        }

        return new CustomButton(buttonName, reactTitle, reactText, changed);
    }

    private static Event ReadEvent(JSONObject _jsonEvent) throws  JSONException {

        String _titleName = _jsonEvent.getString("TitleName");
        String _imageName = _jsonEvent.getString("ImageName");
        String _eventText = _jsonEvent.getString("EventText");
        Boolean _isLoop = _jsonEvent.getBoolean("IsLoop");

        CustomButton[] _buttons = ReadButton(_jsonEvent);

        return new Event(_titleName, _imageName, _eventText, _buttons, _isLoop);
    }

    private static CustomButton[] ReadButton(JSONObject _jsonEvent) throws JSONException {

        JSONArray jsonArray = _jsonEvent.getJSONArray("Buttons");
        CustomButton[] _buttons = new CustomButton[jsonArray.length()];

        for (int j = 0; j < jsonArray.length(); j++) {
            JSONObject button = (JSONObject) jsonArray.get(j);

            _buttons[j] = ReadButtonJson(button);

            if(button.getBoolean("HasContinue")) {
                CustomButton[] _buttonsNew = ReadButton(button.getJSONObject("ReactionButton"));
                _buttons[j].setReactionEventButtons(_buttonsNew);
            }
        }

        return  _buttons;
    }

    private static String ReadText(Context context, int resId) throws IOException {
        InputStream is = context.getResources().openRawResource(resId);
        BufferedReader br= new BufferedReader(new InputStreamReader(is));
        StringBuilder sb= new StringBuilder();
        String s= null;
        while((  s = br.readLine())!=null) {
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }
}
