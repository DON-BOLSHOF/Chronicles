package com.example.myproject;

public class CustomButton {
    private String name;
    private Reaction[] reaction;
    private String[][] toCheck;
    private CustomButton[] reactionEventButtons;

    public CustomButton[] getReactionEventButtons() {
        return reactionEventButtons;
    }

    public void setReactionEventButtons(CustomButton[] reactionEventButtons) {
        this.reactionEventButtons = reactionEventButtons;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Reaction[] getReaction() {
        return reaction;
    }

    public void setReaction(Reaction[] reaction) {
        this.reaction = reaction;
    }

    public String[][] getToCheck() {
        return toCheck;
    }

    public void setToCheck(String[][] toCheck) {
        this.toCheck = toCheck;
    }

    public CustomButton(String name, Reaction[] reaction, String[][] toCheck) {
        this.name = name;
        this.reaction = reaction;
        this.toCheck = toCheck;
    }
}
