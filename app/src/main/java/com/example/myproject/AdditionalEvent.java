package com.example.myproject;

public class AdditionalEvent {
    private String title;
    private String params;
    private  String description;
    private String type;
    private  String[][] check;
    private  String[][] changed;

    public AdditionalEvent(String title, String params, String description, String type, String[][] check, String[][] changed) {
        this.title = title;
        this.params = params;
        this.description = description;
        this.type = type;
        this.check = check;
        this.changed = changed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[][] getCheck() {
        return check;
    }

    public void setCheck(String[][] check) {
        this.check = check;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[][] getChanged() {
        return changed;
    }

    public void setChanged(String[][] changed) {
        this.changed = changed;
    }
}
