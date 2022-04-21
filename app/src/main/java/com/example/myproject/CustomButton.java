package com.example.myproject;

public class CustomButton {
    private String name;
    private String reactTitle;
    private String reaction;
    private String[][] willChanged;
    private CustomButton[] reactionEventButtons;

    public CustomButton[] getReactionEventButtons() {
        return reactionEventButtons;
    }

    public void setReactionEventButtons(CustomButton[] reactionEventButtons) {
        this.reactionEventButtons = reactionEventButtons;
    }

    public String[][] getWillChanged() {
        return willChanged;
    }

    public void setWillChanged(String[][] willChanged) {
        this.willChanged = willChanged;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReactTitle(String reactTitle) {
        this.reactTitle = reactTitle;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public String getName() {
        return name;
    }

    public String getReactTitle() {
        return reactTitle;
    }

    public String getReaction() {
        return reaction;
    }

    public CustomButton(String name, String reactTitle, String reaction, String[][] willChanged) {
        this.name = name;
        this.reactTitle = reactTitle;
        this.reaction = reaction;
        this.willChanged = willChanged;
    }
}
