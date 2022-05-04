package com.example.myproject;

public class Reaction {
    private String reactTitle;
    private String reactionText;
    private String[][] willChanged;

    public String getReactTitle() {
        return reactTitle;
    }

    public void setReactTitle(String reactTitle) {
        this.reactTitle = reactTitle;
    }

    public String getReactionText() {
        return reactionText;
    }

    public void setReactionText(String reaction) {
        this.reactionText = reaction;
    }

    public String[][] getWillChanged() {
        return willChanged;
    }

    public void setWillChanged(String[][] willChanged) {
        this.willChanged = willChanged;
    }

    public Reaction(String reactTitle, String reactionText, String[][] willChanged) {
        this.reactTitle = reactTitle;
        this.reactionText = reactionText;
        this.willChanged = willChanged;
    }
}
