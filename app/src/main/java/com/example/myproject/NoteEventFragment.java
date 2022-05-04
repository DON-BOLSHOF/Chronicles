package com.example.myproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class NoteEventFragment  extends  AddEventParent{
    private ImageView noteImage;
    private NoteEvent noteEvent;

    public NoteEventFragment(OnDestroyed events, NoteEvent event) {
        super(events);

        noteEvent = event;
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
        view = inflater.inflate(R.layout.note_event_fragment, container, false);

        SetViewCreatingAnim();
        SetDestroyButton();

        InitSceneParam();
        InitNoteEvent();

        return view;
    }

    private void InitSceneParam(){
        title =  view.findViewById(R.id.Title);
        description =  view.findViewById(R.id.Description);
        noteImage = view.findViewById(R.id.NoteImage);
    }

    private void InitNoteEvent(){
        title.setText(noteEvent.getTitle());
        description.setText(noteEvent.getDescription());
        SetImage(noteEvent.getImageName());
    }

    private void SetImage(String img) { //Поставить картину
        int resID = getResources().getIdentifier(img , "drawable", MainActivity.PACKAGE_NAME);
        noteImage.setImageResource(resID);
    }
}
