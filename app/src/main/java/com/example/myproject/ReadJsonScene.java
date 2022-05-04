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
        sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(events == null){
            editor.putString(SAVED_TITLES, null);
            editor.commit();
        }else {
            JSONArray _lastEventsNames = new JSONArray();

            for (int i = 0; i < events.size(); i++) {
                _lastEventsNames.put(events.get(i).get_titleName());
            }

            editor.putString(SAVED_TITLES, _lastEventsNames.toString());
            editor.commit();
        }
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
        String[][] check = null;
        Reaction[] reactions = null;

        String buttonName = button.getString("ButtonName");

        if (buttonName.equals("Nothing"))
            return null;

        JSONObject checkEvent = button.getJSONObject("WhatCheck");
        JSONArray toCheck = checkEvent.getJSONArray("ToCheck");
        JSONArray checkFor = checkEvent.getJSONArray("Params");
        check = new String[toCheck.length()][2];

        for(int z = 0;z<toCheck.length();z++){
            String checkParam = toCheck.getString(z);
            if(checkParam.equals("Nothing")) {
                check = null;
                break;
            }
            String checkValue = checkFor.getString(z);
            check[z][0] = checkParam;
            check[z][1] = checkValue;
        }

        JSONArray reactionJSON = button.getJSONArray("EventReaction");
        reactions = new Reaction[reactionJSON.length()];

        for(int i = 0; i< reactionJSON.length(); i++) {
            String[][] changed = null;
            JSONObject object = reactionJSON.getJSONObject(i);
            String reactTitle = object.getString("Title");
            String reactText = object.getString("ReactionText");

            JSONObject reactEvent = object.getJSONObject("ParameterReact");
            JSONArray willChanged = reactEvent.getJSONArray("WillChanged");
            JSONArray willChangedFor = reactEvent.getJSONArray("WillChangedFor");
            changed = new String[willChanged.length()][2]; // [Имя][Значение] размерность одинаковая будет

            for (int j = 0; j < willChanged.length(); j++) {
                String reactParam = willChanged.getString(j);
                if (reactParam.equals("Nothing")) {
                    changed = null;
                    break;
                }
                String reactValue = willChangedFor.getString(j);
                changed[j][0] = reactParam;
                changed[j][1] = reactValue;
            }

            reactions[i] = new Reaction(reactTitle, reactText, changed);
        }

        return new CustomButton(buttonName,reactions, check);
    }

    private static Event ReadEvent(JSONObject _jsonEvent) throws  JSONException {

        String _titleName = _jsonEvent.getString("TitleName");
        String _imageName = _jsonEvent.getString("ImageName");
        String _eventText = _jsonEvent.getString("EventText");
        Boolean _isLoop = _jsonEvent.getBoolean("IsLoop");
        String _typeEvent = _jsonEvent.getString("EventType");
        String _addMusic = _jsonEvent.getBoolean("HasAddMusic") ? _jsonEvent.getString("AddMusic") : null;
        String _nameToSet = !_jsonEvent.getString("SetEvent").equals("Nothing") ? _jsonEvent.getString("SetEvent") :null;

        CustomButton[] _buttons =  ReadButton(_jsonEvent);

        boolean _hasAddEvent = _jsonEvent.getBoolean("HasAddEvent");
        String _addEventType = _hasAddEvent ? _jsonEvent.getString("AddEventType") : null;

        AdditionalEvent _addEvent = null;
        NoteEvent _noteEvent = null;
        if(_hasAddEvent) {
            if (_addEventType.equals("ReactEvent")) {
                _addEvent = ReadAddEvent(_jsonEvent);
            } else if (_addEventType.equals("NoteEvent")) {
                _noteEvent = ReadNoteEvent(_jsonEvent);
            }
        }

        return new Event(_titleName, _imageName, _eventText, _buttons, _isLoop, _typeEvent, _hasAddEvent, _addEventType, _addEvent, _noteEvent, _addMusic, _nameToSet);
    }

    private static NoteEvent ReadNoteEvent(JSONObject _jsonEvent) throws JSONException {
        JSONObject _noteEventJson = _jsonEvent.getJSONObject("NoteEvent");
        String _title = _noteEventJson.getString("Title");
        String _description = _noteEventJson.getString("Description");
        String _imageNoteName = _noteEventJson.getString("ImageName");

        return new NoteEvent(_title, _description, _imageNoteName);
    }

    private static AdditionalEvent ReadAddEvent(JSONObject _jsonEvent) throws JSONException {
        String[][] check = null;
        String[][] changed = null;

        JSONObject checkEvent = _jsonEvent.getJSONObject("WhatCheck");
        JSONArray toCheck = checkEvent.getJSONArray("ToCheck");
        JSONArray checkFor = checkEvent.getJSONArray("Params");
        check = new String[toCheck.length()][2];

        for(int z = 0;z<toCheck.length();z++){
            String checkParam = toCheck.getString(z);
            if(checkParam.equals("Nothing")) {
                check = null;
                break;
            }
            String checkValue = checkFor.getString(z);
            check[z][0] = checkParam;
            check[z][1] = checkValue;
        }

        JSONObject _addEventJson = _jsonEvent.getJSONObject("AdditionalEvent");
        String _title = _addEventJson.getString("Title");
        String _params = _addEventJson.getString("Params");
        String _description = _addEventJson.getString("Description");

        JSONObject reactEvent = _addEventJson.getJSONObject("ParameterReact");
        JSONArray willChanged = reactEvent.getJSONArray("WillChanged");
        JSONArray willChangedFor = reactEvent.getJSONArray("WillChangedFor");
        changed = new String[willChanged.length()][2]; // [Имя][Значение] размерность одинаковая будет

        for (int j = 0; j < willChanged.length(); j++) {
            String reactParam = willChanged.getString(j);
            if (reactParam.equals("Nothing")) {
                changed = null;
                break;
            }
            String reactValue = willChangedFor.getString(j);
            changed[j][0] = reactParam;
            changed[j][1] = reactValue;
        }

        String _type = _jsonEvent.getString("TypeOfAddEvent"); //Для проверки на какой ивент

        return new AdditionalEvent(_title, _params, _description, _type, check, changed);
    }

    private static CustomButton[] ReadButton(JSONObject _jsonEvent) throws JSONException {

        JSONArray jsonArray = _jsonEvent.getJSONArray("Buttons");
        CustomButton[] _buttons = new CustomButton[jsonArray.length()];

        for (int j = 0; j < jsonArray.length(); j++) {
            JSONObject button = (JSONObject) jsonArray.get(j);

            _buttons[j] = ReadButtonJson(button);

            if(_buttons[j] == null)
                return null;

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
