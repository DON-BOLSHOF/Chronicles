package com.example.myproject.AdditionalEvent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.myproject.SpriteAnimation.GameView;

public class DiceRoll extends Fragment
{
    private int rollValue;
    public DiceRoll(int rollValue){
        this.rollValue = rollValue;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = new GameView(getContext(), rollValue);
        return inflate;
    }
}
