package com.example.myproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class FragmentSceneManager extends Fragment {

    private Animation animation;
    private ImageView _eventImage;
    private TextView _eventTitle;
    private LinearLayout _eventButtonLayout;
    private Button _buttons[];
    private TextView _eventText;
    private TextView _characterMoney;
    private TextView _characterRelFather;
    private TextView _characterPopularity;
    private View _view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.fragment_scene,
                container, false);

        InitSceneParam();
        InitScene(MainActivity.currentEvent);

        return _view;
    }

    public void onResume() {
        super.onResume();
    }

    private void InitSceneParam(){
        _eventText = (TextView) _view.findViewById(R.id.EventText);
        _eventTitle = (TextView) _view.findViewById(R.id.Title);
        _eventImage = (ImageView) _view.findViewById(R.id.EventImage);
        _eventButtonLayout =  (LinearLayout) _view.findViewById(R.id.ButtonLayout);
        _characterMoney = (TextView) _view.findViewById(R.id.TextMoney);
        _characterRelFather = (TextView) _view.findViewById(R.id.TextFatherRel);
        _characterPopularity = (TextView) _view.findViewById(R.id.TextPopularity);
    }

    private void InitScene(Event scene){
        SetTitle(scene.get_titleName());
        SetImage(scene.get_imageName());
        SetEventText(scene.get_eventText());
        SetButtons(scene.get_buttons(), scene.get_buttons().length);
        SetMoney();
        SetRelFather();
        SetPopularity();

    } // Чтобы лучше смотрелось

    private void SetViewFogging(){
        animation = AnimationUtils.loadAnimation(requireActivity(), R.anim.alphadel);
        _view.startAnimation(animation);
    }

    private void SetViewCreating(){
        animation = AnimationUtils.loadAnimation(requireActivity(), R.anim.alphacreate);
        _view.startAnimation(animation);
    }

    private void SetImage(String img) { //Поставить картину
        int resID = getResources().getIdentifier(img , "drawable", MainActivity.PACKAGE_NAME);
        _eventImage.setImageResource(resID);
    }

    private void SetNonImage(){
        _eventImage.setImageResource(0);
    }

    private void SetButtons(CustomButton[] buttons, int numbers){ //Сотворить кнопки
        _buttons = new Button[numbers];
        for(int i=0;i<numbers;i++){
            _buttons[i] = new Button(requireActivity());
            _buttons[i].setText(buttons[i].getName());

            int finalI = i; //Android studio предложил это решение

            _buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetFogging();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SetEventReaction(buttons[finalI]);

                            if(buttons[finalI].getReactionEventButtons() != null){
                                CustomButton[] reaction = buttons[finalI].getReactionEventButtons();
                                SetButtons(reaction, reaction.length);
                            }else {

                                Button tempButton = new Button(requireActivity());
                                tempButton.setText("Продолжить");

                                SetCreating(tempButton);

                                tempButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        _eventButtonLayout.removeView(tempButton);
                                        SetViewFogging();

                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                SetRandomEvent();
                                                InitScene(MainActivity.currentEvent);
                                                SetViewCreating();
                                            }
                                        }, 1500);
                                    }
                                });
                            }
                        }
                    }, 1500);
                }
            });
        }

        for(int i=0; i<numbers; i++){
            _eventButtonLayout.addView(_buttons[i], i);
        }
    }

    private void SetAnimation(Animation animation){ //Нет общего класса наподобие GameObject у которого можно было бы вызывать анимацию, поэтому хардкодим
        _eventImage.startAnimation(animation);
        _eventText.startAnimation(animation);
        _eventTitle.startAnimation(animation);
    }

    private void SetEventText(String text){
        _eventText.setText(text);
    }

    private void SetTitle(String text){
        _eventTitle.setText(text);
    }

    private void SetMoney(){
        _characterMoney.setText(Integer.toString(MainActivity.character.getMoney()));
    }

    private void SetRelFather(){
        _characterRelFather.setText(Integer.toString(MainActivity.character.getFatherRel()));
    }

    private void SetPopularity(){
        _characterPopularity.setText(Integer.toString(MainActivity.character.getPopularity()));
    }

    private void SetUpdateParams(String[][] changeStats){
        if(changeStats != null){
            for (int j = 0; j<changeStats.length; j++){
                switch (changeStats[j][0]){
                    case "Money": {
                        MainActivity.character.boostMoney(Integer.parseInt(changeStats[j][1]));
                        break;
                    }
                    case "FatherRelations":{
                        MainActivity.character.boostFatherRel(Integer.parseInt(changeStats[j][1]));
                        break;
                    }
                    case  "Popularity":{
                        MainActivity.character.boosPopularity(Integer.parseInt(changeStats[j][1]));
                        break;
                    }
                    case  "FightingSkill":{
                        MainActivity.character.boostFightSkill((Integer.parseInt(changeStats[j][1])));
                    }
                }
            }
        }
    }

    private void SetRandomEvent(){
        if(!MainActivity.currentEvent.is_isLoop())
            MainActivity.chapterEvents.remove(MainActivity.currentEvent);

        Random _rand = new Random();
        int _value = _rand.nextInt(MainActivity.chapterEvents.size());
        int _current = MainActivity.chapterEvents.indexOf(MainActivity.currentEvent);

        while (_current == _value)
            _value = _rand.nextInt(MainActivity.chapterEvents.size());


        MainActivity.currentEvent = MainActivity.chapterEvents.get(_value);
    }

    private void SetEventReaction(CustomButton button){
        SetNonImage();
        SetEventText(button.getReaction());
        SetTitle(button.getReactTitle());

        SetUpdateParams(button.getWillChanged());

        for(int i=0; i<_buttons.length; i++){
            _eventButtonLayout.removeView(_buttons[i]);
        }
    }

    private void SetFogging(){
        animation = AnimationUtils.loadAnimation(requireActivity(), R.anim.alphadel);
        SetAnimation(animation);

        for(int i=0; i<_buttons.length; i++){
            _buttons[i].startAnimation(animation);
        }
    }

    private void SetCreating(Button button){
        _eventButtonLayout.addView(button);

        animation = AnimationUtils.loadAnimation(requireActivity(), R.anim.alphacreate);

        SetAnimation(animation);
        button.startAnimation(animation);
    }
}