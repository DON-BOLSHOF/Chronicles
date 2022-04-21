package com.example.myproject;

public class Event {
    private String _titleName;
    private String _imageName;
    private String _eventText;
    private CustomButton[] _buttons;
    private boolean _isLoop;

    public Event(String _titleName, String _imageName, String _eventText, CustomButton[] _buttons, boolean _isLoop) {
        this._titleName = _titleName;
        this._imageName = _imageName;
        this._eventText = _eventText;
        this._buttons = _buttons;
        this._isLoop = _isLoop;
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
