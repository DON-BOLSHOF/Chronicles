package com.example.myproject.SubClasses;

import com.example.myproject.AdditionalEvent.AdditionalEvent;
import com.example.myproject.AdditionalEvent.NoteEvent;

public class Event {
    private String _titleName;
    private String _imageName;
    private String _eventText;
    private CustomButton[] _buttons;
    private boolean _isLoop;
    private int _frequency; //С этого момента дополнительные переменные, которые нужно проверить, не в конструктор будем записывать, а через сет делать
    private String _eventType;
    private boolean _hasAddEvent;
    private String _typeAddEvent= null;
    private AdditionalEvent _addEvent= null;
    private NoteEvent _noteEvent= null;
    private String _addMusicName= null;
    private String _nameEventToSet= null;

    public Event(String _titleName, String _imageName, String _eventText, CustomButton[] _buttons, boolean _isLoop, String _eventType, boolean _hasAddEvent) {
        this._titleName = _titleName;
        this._imageName = _imageName;
        this._eventText = _eventText;
        this._buttons = _buttons;
        this._eventType = _eventType;
        this._isLoop = _isLoop;
        this._hasAddEvent = _hasAddEvent;
    }

    public int get_frequency() {
        return _frequency;
    }

    public void set_frequency(int _frequency) {
        this._frequency = _frequency;
    }

    public boolean is_hasAddEvent() {
        return _hasAddEvent;
    }

    public void set_hasAddEvent(boolean _hasAddEvent) {
        this._hasAddEvent = _hasAddEvent;
    }

    public String get_typeAddEvent() {
        return _typeAddEvent;
    }

    public void set_typeAddEvent(String _typeAddEvent) {
        this._typeAddEvent = _typeAddEvent;
    }

    public String get_nameEventToSet() {
        return _nameEventToSet;
    }

    public void set_nameEventToSet(String _nameEventToSet) {
        this._nameEventToSet = _nameEventToSet;
    }

    public String get_eventType() {
        return _eventType;
    }

    public void set_eventType(String _eventType) {
        this._eventType = _eventType;
    }

    public NoteEvent get_noteEvent() {
        return _noteEvent;
    }

    public void set_noteEvent(NoteEvent _noteEvent) {
        this._noteEvent = _noteEvent;
    }

    public AdditionalEvent get_addEvent() {
        return _addEvent;
    }

    public String get_addMusicName() {
        return _addMusicName;
    }

    public void set_addMusicName(String _addMusicName) {
        this._addMusicName = _addMusicName;
    }

    public void set_addEvent(AdditionalEvent _addEvent) {
        this._addEvent = _addEvent;
    }

    public String get_titleName() {
        return _titleName;
    }

    public String get_imageName() {
        return _imageName;
    }

    public String get_eventText() {
        return _eventText;
    }

    public void set_titleName(String _titleName) {
        this._titleName = _titleName;
    }

    public void set_imageName(String _imageName) {
        this._imageName = _imageName;
    }

    public void set_eventText(String _eventText) {
        this._eventText = _eventText;
    }

    public CustomButton[] get_buttons() {
        return _buttons;
    }

    public void set_buttons(CustomButton[] _buttons) {
        this._buttons = _buttons;
    }

    public boolean is_isLoop() {
        return _isLoop;
    }

    public void set_isLoop(boolean _isLoop) {
        this._isLoop = _isLoop;
    }
}
