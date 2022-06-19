package com.example.myproject.AdditionalEvent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.myproject.MainActivities.MainActivity;
import com.example.myproject.R;

public class NoteEventFragment  extends  AddEventParent{
    private ImageView _noteImage;
    private NoteEvent _noteEvent;

    public NoteEventFragment(OnDestroyed events, NoteEvent event) {
        super(events);

        _noteEvent = event;
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
        _noteImage = view.findViewById(R.id.NoteImage);
    }

    private void InitNoteEvent(){
        title.setText(_noteEvent.getTitle());
        description.setText(_noteEvent.getDescription());
        SetImage(_noteEvent.getImageName());
    }

    private void SetImage(String img) { //Поставить картину
        int resID = getResources().getIdentifier(img , "drawable", MainActivity.PACKAGE_NAME);
        _noteImage.setImageResource(resID);
    }
}
