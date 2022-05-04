package com.example.myproject;

public class Event {
    private String _titleName;
    private String _imageName;
    private String _eventText;
    private CustomButton[] _buttons;
    private boolean _isLoop;
    private String _eventType;
    private boolean _hasAddEvent;
    private String _typeAddEvent;
    private AdditionalEvent _addEvent;
    private NoteEvent _noteEvent;
    private String _addMusicName;
    private String _nameEventToSet;

    public Event(String _titleName, String _imageName, String _eventText, CustomButton[] _buttons, boolean _isLoop, String _eventType, boolean _hasAddEvent, String _typeAddEvent, AdditionalEvent _addEvent, NoteEvent _noteEvent, String _addMusicName, String _nameEventToSet) {
        this._titleName = _titleName;
        this._imageName = _imageName;
        this._eventText = _eventText;
        this._buttons = _buttons;
        this._eventType = _eventType;
        this._isLoop = _isLoop;
        this._hasAddEvent = _hasAddEvent;
        this._typeAddEvent = _typeAddEvent;
        this._addEvent = _addEvent;
        this._noteEvent = _noteEvent;
        this._addMusicName = _addMusicName;
        this._nameEventToSet = _nameEventToSet;
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
