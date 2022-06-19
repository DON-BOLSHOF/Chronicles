package com.example.myproject.AdditionalEvent;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myproject.R;

public class AdditionalEventFragment extends AddEventParent {
    private TextView _params;
    private AdditionalEvent _addEvent;


    public AdditionalEventFragment(OnDestroyed events, AdditionalEvent addEvent){
        super(events);
        _addEvent = addEvent;
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
        _params =  view.findViewById(R.id.Params);
        description =  view.findViewById(R.id.Description);
    }

    private void InitAdditionalEvent(){
        title.setText(_addEvent.getTitle());
        _params.setText(_addEvent.getParams());
        description.setText(_addEvent.getDescription());
    }

}