package com.example.myproject.JsonWork;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myproject.AdditionalEvent.AdditionalEvent;
import com.example.myproject.SubClasses.CustomButton;
import com.example.myproject.SubClasses.Event;
import com.example.myproject.AdditionalEvent.NoteEvent;
import com.example.myproject.R;
import com.example.myproject.SubClasses.Reaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ReadJsonScene {
    private static SharedPreferences _sharedPreferences;
    private static final String SAVED_TITLES = "TITLES";

    public static  ArrayList<Event> ReadSceneJSONFile(Context context) throws IOException, JSONException{
        String jsonText = ReadText(context, R.raw.sorce);

        String titles =  context.getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("TITLES", null);

        JSONObject jsonRoot = new JSONObject(jsonText);
        JSONArray jsonEvents = jsonRoot.getJSONArray("Event");

        ArrayList<Event> events = new ArrayList<>();

        for (int i = 0; i< jsonEvents.length(); i++) {
            JSONObject _jsonEvent = jsonEvents.getJSONObject(i);

            Event _event = ReadEvent(_jsonEvent);

            if (titles!= null) {
                if(titles.contains(_event.getTitleName()))
                    events.add(_event);
            }
            else{
                events.add((_event));
            }
        }

        return events;
    }

    public static void OverWriteParams(Context context, ArrayList<Event> events) throws  IOException, JSONException{
        _sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = _sharedPreferences.edit();
        if(events == null){
            editor.putString(SAVED_TITLES, null);
        }else {
            JSONArray _lastEventsNames = new JSONArray();

            for (int i = 0; i < events.size(); i++) {
                _lastEventsNames.put(events.get(i).getTitleName());
            }

            editor.putString(SAVED_TITLES, _lastEventsNames.toString());
        }
        editor.apply();
    }

    private static CustomButton ReadButtonJson(JSONObject button) throws  JSONException {
        String[][] check;
        Reaction[] reactions;

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
            String[][] changed;
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

        CustomButton customButton = new CustomButton(buttonName,reactions, check);

        return customButton;
    }

    private static Event ReadEvent(JSONObject jsonEvent) throws  JSONException {

        String titleName = jsonEvent.getString("TitleName");
        String imageName = jsonEvent.getString("ImageName");
        String eventText = jsonEvent.getString("EventText");

        boolean isLoop = jsonEvent.getBoolean("IsLoop");

        String typeEvent = jsonEvent.getString("EventType");

        CustomButton[] buttons =  ReadButton(jsonEvent);
        if(buttons != null) {
            eventText = RequiredParameters(buttons[0].getToCheck(), eventText);
        }

        boolean hasAddEvent = jsonEvent.getBoolean("HasAddEvent");

        Event mEvent = new Event(titleName, imageName, eventText, buttons, isLoop, typeEvent, hasAddEvent);

        if(isLoop) {
            int frequency = jsonEvent.getInt("Frequency");
            mEvent.setFrequency(frequency);
        }

        String addMusic = jsonEvent.getBoolean("HasAddMusic") ? jsonEvent.getString("AddMusic") : null;
        if(addMusic != null){
            mEvent.setAddMusicName(addMusic);
        }

        String nameToSet = !jsonEvent.getString("SetEvent").equals("Nothing") ? jsonEvent.getString("SetEvent") :null;
        if(nameToSet != null){
            mEvent.setNameEventToSet(nameToSet);
        }

        if(hasAddEvent){
            String addEventType =  jsonEvent.getString("AddEventType");
            mEvent.setTypeAddEvent(addEventType);

            if (addEventType.equals("ReactEvent")) {
                AdditionalEvent _addEvent = ReadAddEvent(jsonEvent);
                mEvent.setAddEvent(_addEvent);
            } else if (addEventType.equals("NoteEvent")) {
                NoteEvent _noteEvent = ReadNoteEvent(jsonEvent);
                mEvent.setNoteEvent(_noteEvent);
            }

        }

        return mEvent;
    }

    private static String RequiredParameters(String[][] _toCheckAnnotation , String _eventText) {
        if (_toCheckAnnotation != null && _toCheckAnnotation[0][0].equals("RandomValue")) {
            _eventText += "\n\nТребуется:\n";
            StringBuilder _eventTextBuilder = new StringBuilder(_eventText);
            for (String[] strings : _toCheckAnnotation) {
                if (strings[0].equals("Money")) {
                    _eventTextBuilder.append(String.format("%s: %s.\n", "Грошей", strings[1]));
                }

                if (strings[0].equals("Popularity")) {
                    _eventTextBuilder.append("Быть достаточно популярным.\n");
                }

                if (strings[0].equals("FatherRelations")) {
                    _eventTextBuilder.append("Ладить с отцом\n");
                }

                if (strings[0].equals("FightingSkill")) {
                    _eventTextBuilder.append(String.format("%s: %s.\n", "Хорошо обращаться с мечом", strings[1]));
                }

                if (strings[0].equals("RandomValue")) {
                    _eventTextBuilder.append("Подбросить кубик");
                }
            }
            _eventText = _eventTextBuilder.toString();
        }
        return  _eventText;
    }

    private static NoteEvent ReadNoteEvent(JSONObject _jsonEvent) throws JSONException {
        JSONObject _noteEventJson = _jsonEvent.getJSONObject("NoteEvent");
        String _title = _noteEventJson.getString("Title");
        String _description = _noteEventJson.getString("Description");
        String _imageNoteName = _noteEventJson.getString("ImageName");

        return new NoteEvent(_title, _description, _imageNoteName);
    }

    private static AdditionalEvent ReadAddEvent(JSONObject _jsonEvent) throws JSONException {
        String[][] check;
        String[][] changed;

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

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject button = (JSONObject) jsonArray.get(i);

            _buttons[i] = ReadButtonJson(button);

            if(_buttons[i] == null)
                return null;

            if(button.getBoolean("HasContinue")) { //Настраиваем кнопки для ивента-реакции
                JSONArray reactionJSON = button.getJSONArray("EventReaction");
                CustomButton[][] _buttonsNew = new CustomButton[reactionJSON.length()][];
                for (int j = 0;j<reactionJSON.length();j++) {
                    JSONObject reaction = (JSONObject) reactionJSON.get(j);
                    JSONObject reactButtonsJson = reaction.getJSONObject("ReactionButton");
                    _buttonsNew[j] = ReadButton(reactButtonsJson);
                }

                _buttons[i].setReactionEventButtons(_buttonsNew);
            }
        }

        return  _buttons;
    }

    private static String ReadText(Context context, int resId) throws IOException {
        InputStream is = context.getResources().openRawResource(resId);
        BufferedReader br= new BufferedReader(new InputStreamReader(is));
        StringBuilder sb= new StringBuilder();
        String s;
        while((  s = br.readLine())!=null) {
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }
}
