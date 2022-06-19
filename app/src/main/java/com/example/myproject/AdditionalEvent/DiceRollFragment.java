package com.example.myproject.AdditionalEvent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.myproject.MainActivities.MainActivity;
import com.example.myproject.R;

public class DiceRollFragment extends AddEventParent {
    private int _rollValue;
    private String[][] _reactions;
    private TextView _rollText;
    
    public DiceRollFragment(OnDestroyed events, int RollValue, String[][] reactions) {
        super(events);
        _reactions = reactions;
        _rollValue = RollValue;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DiceRoll roll = new DiceRoll(_rollValue);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.RollCube, roll)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.roll_fragment, container, false);
        
        _rollText = view.findViewById(R.id.RollText);
        StringBuilder text = new StringBuilder();
        for (String[] reaction : _reactions) {
            switch (reaction[0]) {
                case "Popularity": {//Roll возможен лишь с этими параметрами.
                    text.append(String.format("%s: %s + %s < %s\n", "Популярность", (_rollValue + 1), MainActivity.character.getPopularity(), reaction[1]));
                    break;
                }
                case "FightingSkill": {
                    text.append(String.format("%s: %s + %s %s %s\n", "Уровень фехтования", (_rollValue + 1), MainActivity.character.getFightSkill(), MainActivity.character.getFightSkill() + _rollValue < Integer.parseInt(reaction[1]) ? "<" : ">", reaction[1]));
                    break;
                }
            }
        }

        _rollText.setText(text.toString());
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        _rollText.setAnimation(animation);

        SetViewCreatingAnim();
        SetDestroyButton();

        return view;
    }
}
