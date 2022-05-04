package com.example.myproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class AdditionalEventFragment extends AddEventParent {
    private TextView params;
    private AdditionalEvent addEvent;


    public AdditionalEventFragment(OnDestroyed events, AdditionalEvent addEvent){
        super(events);
        this.addEvent = addEvent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.add_event_fragment, container, false);

        SetViewCreatingAnim();
        SetDestroyButton();

        InitSceneParam();
        InitAdditionalEvent();

        return view;
    }

    private void InitSceneParam(){
        title =  view.findViewById(R.id.Title);
        params =  view.findViewById(R.id.Params);
        description =  view.findViewById(R.id.Description);
    }

    private void InitAdditionalEvent(){
        title.setText(addEvent.getTitle());
        params.setText(addEvent.getParams());
        description.setText(addEvent.getDescription());
    }

}