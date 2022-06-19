package com.example.myproject.SubClasses;

import com.example.myproject.AdditionalEvent.AdditionalEvent;
import com.example.myproject.AdditionalEvent.NoteEvent;

public class Event {
    private String titleName;
    private String imageName;
    private String eventText;
    private CustomButton[] buttons;
    private boolean isLoop;
    private int frequency; //С этого момента дополнительные переменные, которые нужно проверить, не в конструктор будем записывать, а через сет делать
    private String eventType;
    private boolean hasAddEvent;
    private String typeAddEvent;
    private AdditionalEvent addEvent;
    private NoteEvent noteEvent;
    private String addMusicName;
    private String nameEventToSet;

    public Event(String _titleName, String _imageName, String _eventText, CustomButton[] _buttons, boolean _isLoop, String _eventType, boolean _hasAddEvent) {
        this.titleName = _titleName;
        this.imageName = _imageName;
        this.eventText = _eventText;
        this.buttons = _buttons;
        this.eventType = _eventType;
        this.isLoop = _isLoop;
        this.hasAddEvent = _hasAddEvent;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public boolean isHasAddEvent() {
        return hasAddEvent;
    }

    public void setHasAddEvent(boolean hasAddEvent) {
        this.hasAddEvent = hasAddEvent;
    }

    public String getTypeAddEvent() {
        return typeAddEvent;
    }

    public void setTypeAddEvent(String typeAddEvent) {
        this.typeAddEvent = typeAddEvent;
    }

    public String getNameEventToSet() {
        return nameEventToSet;
    }

    public void setNameEventToSet(String nameEventToSet) {
        this.nameEventToSet = nameEventToSet;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public NoteEvent getNoteEvent() {
        return noteEvent;
    }

    public void setNoteEvent(NoteEvent noteEvent) {
        this.noteEvent = noteEvent;
    }

    public AdditionalEvent getAddEvent() {
        return addEvent;
    }

    public String getAddMusicName() {
        return addMusicName;
    }

    public void setAddMusicName(String addMusicName) {
        this.addMusicName = addMusicName;
    }

    public void setAddEvent(AdditionalEvent addEvent) {
        this.addEvent = addEvent;
    }

    public String getTitleName() {
        return titleName;
    }

    public String getImageName() {
        return imageName;
    }

    public String getEventText() {
        return eventText;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setEventText(String eventText) {
        this.eventText = eventText;
    }

    public CustomButton[] getButtons() {
        return buttons;
    }

    public void setButtons(CustomButton[] buttons) {
        this.buttons = buttons;
    }

    public boolean isLoop() {
        return !isLoop;
    }

    public void setLoop(boolean loop) {
        this.isLoop = loop;
    }
}
