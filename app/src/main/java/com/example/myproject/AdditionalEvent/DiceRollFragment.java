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
    private int rollValue;
    private String[][] reactions;
    private TextView RollText;
    
    public DiceRollFragment(OnDestroyed events, int RollValue, String[][] reactions) {
        super(events);
        this.reactions = reactions;
        this.rollValue = RollValue;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DiceRoll roll = new DiceRoll(rollValue);
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
        
        RollText = view.findViewById(R.id.RollText);
        String text = "";
        for(int i = 0; i<reactions.length; i++){
            switch (reactions[i][0]) {
                case "Popularity": {//Roll возможен лишь с этими параметрами.
                    text += String.format("%s: %s + %s < %s\n","Популярность", Integer.toString(rollValue+1), MainActivity.character.getPopularity(), reactions[i][1]);
                    break;
                }
                case "FightingSkill": {
                    text += String.format("%s: %s + %s %s %s\n","Уровень фехтования", Integer.toString(rollValue+1), MainActivity.character.getFightSkill(), MainActivity.character.getFightSkill() + rollValue < Integer.parseInt(reactions[i][1])?"<":">" , reactions[i][1]);
                    break;
                }
            }
        }

        RollText.setText(text);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        RollText.setAnimation(animation);

        SetViewCreatingAnim();
        SetDestroyButton();

        return view;
    }
}
