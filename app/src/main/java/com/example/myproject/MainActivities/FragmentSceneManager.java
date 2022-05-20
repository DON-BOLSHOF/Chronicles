package com.example.myproject.MainActivities;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.myproject.AdditionalEvent.AddEventParent;
import com.example.myproject.AdditionalEvent.AdditionalEvent;
import com.example.myproject.AdditionalEvent.AdditionalEventFragment;
import com.example.myproject.AdditionalEvent.DiceRollFragment;
import com.example.myproject.AdditionalEvent.NoteEvent;
import com.example.myproject.AdditionalEvent.NoteEventFragment;
import com.example.myproject.R;
import com.example.myproject.Settings.SettingsActivity;
import com.example.myproject.SubClasses.CustomButton;
import com.example.myproject.SubClasses.Event;
import com.example.myproject.SubClasses.Reaction;

import java.util.Random;

public class FragmentSceneManager extends Fragment implements AddEventParent.OnDestroyed {
    private long mLastClickTime = 0; // Нужно предотвратить двойное нажатие кнопки

    private Animation animation;
    private ImageView _eventImage;
    private TextView _eventTitle;
    private LinearLayout _eventButtonLayout;
    private Button buttons[];
    private TextView _eventText;
    private TextView _characterMoney;
    private TextView _characterRelFather;
    private TextView _characterPopularity;
    private ScrollView _textScrollView;
    private View _view;
    private AdditionalEventFragment additionalEventFragment;
    private NoteEventFragment noteEventFragment;
    private AddMusic _eventMusic;
    private ImageButton _preffButton;

    public FragmentSceneManager(AddMusic addMusic){
        _eventMusic = addMusic;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.fragment_scene,
                container, false);

        InitSceneParam();
        InitPreffButton();
        InitScene(MainActivity.currentEvent);

        return _view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
/////////////////Initialization/////////////////////////////////////////////////////////////////////////////////////
    private void InitSceneParam(){
        _eventText =  _view.findViewById(R.id.EventText);
        _eventTitle =  _view.findViewById(R.id.Title);
        _eventImage =  _view.findViewById(R.id.EventImage);
        _eventButtonLayout =   _view.findViewById(R.id.ButtonLayout);
        _textScrollView = _view.findViewById(R.id.TextScrollView);
        _characterMoney =  _view.findViewById(R.id.TextMoney);
        _characterRelFather =  _view.findViewById(R.id.TextFatherRel);
        _characterPopularity =  _view.findViewById(R.id.TextPopularity);
        _preffButton = _view.findViewById(R.id.PreffsButton);
    }

    private void InitPreffButton(){
      _preffButton.setOnClickListener(view -> {
          Intent mIntent = new Intent(getContext(), SettingsActivity.class);
          startActivity(mIntent);
      });
    }

    private void InitScene(Event scene){
        SetTitle(scene.get_titleName());
        SetImage(scene.get_imageName());
        SetEventText(scene.get_eventText());
        SetButtons(scene.get_buttons());
        if(scene.is_hasAddEvent()) {
            if (scene.get_typeAddEvent().equals("ReactEvent"))
                SetAdditionalEvent(scene.get_addEvent());
            else
                SetNoteEvent(scene.get_noteEvent());
        }
        if(scene.get_addMusicName() != null)
            EventMusic(scene.get_addMusicName());
        SetMoney();
        SetRelFather();
        SetPopularity();
        ScrollViewUP(_textScrollView);
    } // Чтобы лучше смотрелось

/////////////////Music///////////////////////////////////////////////////////////////////////////////////////////////

    public interface AddMusic{
        void SetAddMusic(String musicName);
        void StopAddMusic();
    }

    private void EventMusic(String name){
        _eventMusic.SetAddMusic(name);
    }
/////////////////View////////////////////////////////////////////////////////////////////////////////////////////////
    private void SetEvent(String eventName){ //Найти и поставить ивент по названию
        for (int i=0; i<MainActivity.chapterEvents.size(); i++){
            Event temp = MainActivity.chapterEvents.get(i);
            if(temp.get_titleName().equals(eventName)) {
                MainActivity.currentEvent = temp;
                InitScene(temp);
                return;
            }
        }
        Log.d("EventsManager","Can't find this event, run random one");
        SetRandomEvent();
        InitScene(MainActivity.currentEvent);
    }

    private void ViewFogging(){
        animation = AnimationUtils.loadAnimation(requireActivity(), R.anim.alphadel);
        _view.startAnimation(animation);
    }

    private void ViewCreating(){
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

    private void SetButtons(CustomButton[] buttons){ //Сотворить кнопки
        if(buttons == null) {
            SetContinueButton();
            return;
        }

        int numbers = buttons.length;
        this.buttons = new Button[numbers];
        for(int i=0;i<numbers;i++){
            this.buttons[i] = new Button(requireActivity());
            this.buttons[i].setText(buttons[i].getName());

            int finalI = i; //Android studio предложил это решение

            this.buttons[i].setOnClickListener(v -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1550){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                Fogging();

                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    SetEventReaction(buttons[finalI]);
                }, 1500);
          });
        }

        for(int i=0; i<numbers; i++){
            SetCreating(this.buttons[i]);
        }

    }

    private void SetContinueButton(){
        Button tempButton = new Button(requireActivity());
        tempButton.setText("Продолжить");

        SetCreating(tempButton);

        tempButton.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1550){
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            _eventMusic.StopAddMusic();

            _eventButtonLayout.removeView(tempButton);
            ViewFogging();

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                String tempEvent = MainActivity.currentEvent.get_nameEventToSet();
                CheckLoop(MainActivity.currentEvent); //Проверь и удали если не луп
                if(tempEvent != null)
                    SetEvent(tempEvent);
                else {
                    SetRandomEvent();
                    InitScene(MainActivity.currentEvent);
                }
                ViewCreating();
                ScrollViewUP(_textScrollView);
            }, 1500);
        });
    }

    private void SetAnimation(Animation animation){ //Нет общего класса наподобие GameObject у которого можно было бы вызывать анимацию, поэтому хардкодим
        _eventImage.startAnimation(animation);
        _eventText.startAnimation(animation);
        _eventTitle.startAnimation(animation);
    }

    private void Fogging(){
        animation = AnimationUtils.loadAnimation(requireActivity(), R.anim.alphadel);
        SetAnimation(animation);

        for (Button button : buttons) {
            button.startAnimation(animation);
        }
    }

    private void ScrollViewUP(ScrollView mScrollView){
        mScrollView.fullScroll(View.FOCUS_UP);
    }

    private void SetCreating(Button button){
        _eventButtonLayout.addView(button);

        animation = AnimationUtils.loadAnimation(requireActivity(), R.anim.alphacreate);

        ScrollViewUP(_textScrollView);

        SetAnimation(animation);
        button.startAnimation(animation);
    }

    private void SetEventText(String text){
        _eventText.setText(text);
    }

    private void SetTitle(String text){
        _eventTitle.setText(text);
    }

    private void SetEventReaction(CustomButton button){
        SetNonImage();
        Reaction buttonReact;

        for (Button value : buttons) {
            _eventButtonLayout.removeView(value);
        }

        if(button.getReactionEventButtons() == null)
            SetContinueButton();

        CustomButton[] reaction = null;

        if(button.getToCheck() != null) {
            if (HasPassTheRollPositive(button.getToCheck())) {

                buttonReact = button.getReaction()[0];

                if (MainActivity.currentEvent.get_eventType().equals("StoryEvent"))
                    MainActivity.currentEvent.set_isLoop(false);

                if (button.getReactionEventButtons() != null) {
                    reaction = button.getReactionEventButtons()[0];
                }
            } else {
                buttonReact = button.getReaction()[1];
                if (button.getReactionEventButtons() != null) {
                    reaction = button.getReactionEventButtons()[1];
                }
            }
        }
        else {
            buttonReact = button.getReaction()[0];
            if (button.getReactionEventButtons() != null) {
                reaction = button.getReactionEventButtons()[0];
            }
        }

        if (reaction != null)
            SetButtons(reaction);

        SetEventText(buttonReact.getReactionText());
        ScrollViewUP(_textScrollView);
        SetTitle(buttonReact.getReactTitle());
        SetUpdateParams(buttonReact.getWillChanged());
    }

    private void SetRandomEvent(){
        Random _rand = new Random();
        int _value = _rand.nextInt(MainActivity.chapterEvents.size());
        int _current = MainActivity.chapterEvents.indexOf(MainActivity.currentEvent);

        while (_current == _value  || !CheckAdditionalEvent(MainActivity.chapterEvents.get(_value)) || !CheckFrequency(MainActivity.chapterEvents.get(_value)))
            _value = _rand.nextInt(MainActivity.chapterEvents.size());


        MainActivity.currentEvent = MainActivity.chapterEvents.get(_value);
    }
/////////////////AdditionalEvent/////////////////////////////////////////////////////////////////////////////////////

    private void SetAdditionalEvent(AdditionalEvent addEvent){
        SetBlackoutOn();
        SetBackgroundDisabled();
        SetAdditionalFragment(addEvent);
        SetUpdateParams(addEvent.getChanged());
    }

    private void SetNoteEvent(NoteEvent noteEvent){
        SetBlackoutOn();
        SetBackgroundDisabled();
        SetNoteEventFragment(noteEvent);
    }

    private void SetAdditionalFragment(AdditionalEvent addEvent){
        additionalEventFragment = new AdditionalEventFragment(this, addEvent);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.AddEvent, additionalEventFragment)
                .addToBackStack(null)
                .commit();
    }

    private void SetNoteEventFragment(NoteEvent noteEvent){
        noteEventFragment = new NoteEventFragment(this,  noteEvent);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.AddEvent, noteEventFragment)
                .addToBackStack(null)
                .commit();
    }

    private void SetDiceRollFragment(int rollValue, String[][] reactions){
        DiceRollFragment rollFragment = new DiceRollFragment(this, rollValue, reactions);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.AddEvent, rollFragment)
                .addToBackStack(null)
                .commit();
    }

    private void SetRoll(){
        SetBlackoutOn();
        SetBackgroundDisabled();
    }

    private void SetBackgroundDisabled(){
        for (int i = 0; i < _eventButtonLayout.getChildCount(); i++) {
            View child = _eventButtonLayout.getChildAt(i);
            child.setClickable(false);
        }
    }

    private void SetBlackoutOn(){
        animation = AnimationUtils.loadAnimation(requireActivity(), R.anim.alphablackoutcreate);
        ActionActivity.blackout.startAnimation(animation);
        ActionActivity.blackout.setAlpha(0.5f);
    }

    private void SetBackgroundEnabled(){
        for (int i = 0; i < _eventButtonLayout.getChildCount(); i++) {
            View child = _eventButtonLayout.getChildAt(i);
            child.setClickable(true);
        }
    }

    private void SetBlackoutOff(){ //Встроенная что-то не хочет работать после    ActionActivity.blackout.setAlpha(0.0f), а без этого просто
        AlphaAnimation tempAnimation = new AlphaAnimation(1.0f, 0.0f); // проигрывается анимация и возвращается альфа на место.
        tempAnimation.setDuration(1500);
        tempAnimation.setStartOffset(1000);
        tempAnimation.setFillAfter(true);
        ActionActivity.blackout.startAnimation(tempAnimation);
    }

    @Override
    public void OnDestroyAddEvent() {
        SetBackgroundEnabled();
        SetBlackoutOff();
    }
/////////////////Parameters/////////////////////////////////////////////////////////////////////////////////////

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
            for (String[] changeStat : changeStats) {
                switch (changeStat[0]) {
                    case "Money": {
                        if(MainActivity.character.getMoney() + Integer.parseInt(changeStat[1])<0)
                            AnimateNumberScroll(MainActivity.character.getMoney(), 0, _characterMoney);
                        else
                            AnimateNumberScroll(MainActivity.character.getMoney(), MainActivity.character.getMoney() + Integer.parseInt(changeStat[1]), _characterMoney);

                        MainActivity.character.boostMoney(Integer.parseInt(changeStat[1]));
                        break;
                    }
                    case "FatherRelations": {
                        AnimateNumberScroll(MainActivity.character.getFatherRel(), MainActivity.character.getFatherRel() + Integer.parseInt(changeStat[1]), _characterRelFather);

                        MainActivity.character.boostFatherRel(Integer.parseInt(changeStat[1]));
                        break;
                    }
                    case "Popularity": {
                        AnimateNumberScroll(MainActivity.character.getPopularity(), MainActivity.character.getPopularity() + Integer.parseInt(changeStat[1]), _characterPopularity);

                        MainActivity.character.boosPopularity(Integer.parseInt(changeStat[1]));
                        break;
                    }
                    case "FightingSkill": {
                        MainActivity.character.boostFightSkill((Integer.parseInt(changeStat[1])));
                        break;
                    }
                    case "ShopLvl": {
                        MainActivity.character.boostShopLvl((Integer.parseInt(changeStat[1])));
                        if(Integer.parseInt(changeStat[1]) == 0) //Самый худший костыль, из всех, но в силу архитектуры чтения json-файла придется использовать это
                            MainActivity.character.boostMoney(MainActivity.character.getShopLvl() * 2);
                        break;
                    }
                    case "HasEquip": {
                        MainActivity.character.setHasEquip(Boolean.parseBoolean(changeStat[1]));
                        break;
                    }
                    case "HasHorse": {
                        MainActivity.character.setHasHorse(Boolean.parseBoolean(changeStat[1]));
                        break;
                    }
                }
            }
        }
    }

    private void CheckLoop(Event event){
        if(!event.is_isLoop())
            RemoveEvent(event);
    }

    private void RemoveEvent(Event event){
        MainActivity.chapterEvents.remove(event);
    }

    private boolean CheckAdditionalEvent(Event testEvent){
        if(testEvent.get_addEvent() == null) //Нет addEvent значит ничего проверять не надо
            return true;
        else{
            String[][] toCheck = testEvent.get_addEvent().getCheck();
            String type = testEvent.get_addEvent().getType();

            if(type.equals("Neutral"))
                return true; //Нечего тут проверять

            if(type.equals("Positive"))
                return HasPassTheRollPositive(toCheck); //Если не прошел ивент будет вызываться
            else
                return HasPassTheRollNegative(toCheck);
        }
    }

    private boolean HasPassTheRollPositive(String[][] _reactions){
       boolean _hasPass = true;
       boolean _isItMoneyCheck = false;
       int moneyToWindraw = 0;
       int rollValue = 0;

        for (String[] reaction : _reactions) {
            String _temp = reaction[0];
            switch (_temp) {
                case "Money": {
                    int _paramsToCheck = Integer.parseInt(reaction[1]);
                    int _myParams = MainActivity.character.getMoney();
                    if (_myParams < _paramsToCheck) {
                        _hasPass = false;
                    } else {
                        _isItMoneyCheck = true;
                        moneyToWindraw = _paramsToCheck;
                    }
                    break;
                }
                case "Popularity": {
                    int _paramsToCheck = Integer.parseInt(reaction[1]);
                    int _myParams = MainActivity.character.getPopularity(); // Добавим щепотку реализма в роли случайности, условное подбрасывание кубика
                    if (_myParams+rollValue < _paramsToCheck) {                              // Как в ДНД.
                        _hasPass = false;
                    }
                    break;
                }
                case "FatherRelations": {
                    int _paramsToCheck = Integer.parseInt(reaction[1]);
                    int _myParams = MainActivity.character.getFatherRel();
                    if (_myParams+rollValue < _paramsToCheck) {
                        _hasPass = false;
                    }
                    break;
                }
                case "FightingSkill": {
                    int _paramsToCheck = Integer.parseInt(reaction[1]);
                    int _myParams = MainActivity.character.getFightSkill();
                    if (_myParams+rollValue < _paramsToCheck) {
                        _hasPass = false;
                    }
                    break;
                }
                case "ShopLvl": {
                    int _paramsToCheck = Integer.parseInt(reaction[1]);
                    int _myParams = MainActivity.character.getShopLvl();
                    if (_myParams < _paramsToCheck) {
                        _hasPass = false;
                    }
                    break;
                }
                case "HasEquip": {
                    boolean _myParam = Boolean.parseBoolean(reaction[1]);
                    if (MainActivity.character.isHasEquip() != _myParam)
                        _hasPass = false;
                    break;
                }
                case "HasHorse": {
                    boolean _myParam = Boolean.parseBoolean(reaction[1]);
                    if (MainActivity.character.isHasHorse() != _myParam)
                        _hasPass = false;
                    break;
                }
                case "RandomEvent": {
                    int _paramsToCheck = Integer.parseInt(reaction[1]);
                    if(Roll() < _paramsToCheck)
                        _hasPass = false;
                    break;
                }
                case "RandomValue": {
                    rollValue = Roll();

                    SetRoll();
                    SetDiceRollFragment(rollValue,_reactions);
                    break;
                }
            }
        }

       if(_hasPass && _isItMoneyCheck)
           WindrowMoney(moneyToWindraw);

       return _hasPass;
    }

    private void WindrowMoney(int money){
        MainActivity.character.boostMoney(-money);
    }

    private boolean HasPassTheRollNegative(String[][] _reactions){
       boolean _hasPass = true;

        for (String[] reaction : _reactions) {
            String _temp = reaction[0];
            int rollValue = 0;
            switch (_temp) {
                case "Money": {
                    int _paramsToCheck = Integer.parseInt(reaction[1]);
                    int _myParams = MainActivity.character.getMoney();
                    if (_myParams > _paramsToCheck) {
                        _hasPass = false;
                    }
                    break;
                }
                case "Popularity": {
                    int _paramsToCheck = Integer.parseInt(reaction[1]);
                    int _myParams = MainActivity.character.getPopularity(); // Добавим щепотку реализма в роли случайности, условное подбрасывание кубика
                    if (_myParams - rollValue > _paramsToCheck) {                              // Как в ДНД.
                        _hasPass = false;
                    }
                    break;
                }
                case "FatherRelations": {
                    int _paramsToCheck = Integer.parseInt(reaction[1]);
                    int _myParams = MainActivity.character.getFatherRel();
                    if (_myParams - rollValue > _paramsToCheck) {
                        _hasPass = false;
                    }
                    break;
                }
                case "FightingSkill": {
                    int _paramsToCheck = Integer.parseInt(reaction[1]);
                    int _myParams = MainActivity.character.getFightSkill();
                    if (_myParams - rollValue > _paramsToCheck) {
                        _hasPass = false;
                    }
                    break;
                }
                case "ShopLvl": {
                    int _paramsToCheck = Integer.parseInt(reaction[1]);
                    int _myParams = MainActivity.character.getShopLvl();
                    if (_myParams > _paramsToCheck) {
                        _hasPass = false;
                    }
                    break;
                }
                case "HasEquip": {
                    boolean _myParam = Boolean.parseBoolean(reaction[1]);
                    if (MainActivity.character.isHasEquip() == _myParam)
                        _hasPass = false;
                    break;
                }
                case "HasHorse": {
                    boolean _myParam = Boolean.parseBoolean(reaction[1]);
                    if (MainActivity.character.isHasHorse() == _myParam)
                        _hasPass = false;
                    break;
                }
                case "RandomEvent": {
                    int _paramsToCheck = Integer.parseInt(reaction[1]);
                    if(Roll() > _paramsToCheck)
                        _hasPass = false;
                    break;
                }
                case "RandomValue": {
                    rollValue = Roll();
                    SetRoll();
                    SetDiceRollFragment(rollValue, _reactions);
                    break;
                }
            }
        }

       return _hasPass;
    }

    private int Roll(){
        Random _rand = new Random();
        return  _rand.nextInt(20) ;
    }

    private boolean CheckFrequency(Event mEvent){
        if(!mEvent.is_isLoop()) //Если нет frequency
            return true;

        int frequency = mEvent.get_frequency();

        return FrequencyRoll()>=Math.abs(frequency-6); //Если частота меньше то шанс ролла меньше должен быть
    }

    private int FrequencyRoll(){
        Random _rand = new Random();
        return  _rand.nextInt(6);
    }
////////////////////////////Animations///////////////////////////////////////////////////////////
    private void AnimateNumberScroll(int initialValue, int finalValue, final TextView textview) {

        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue, finalValue);
        valueAnimator.setDuration(1000);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                textview.setText(valueAnimator.getAnimatedValue().toString());

            }
        });
    valueAnimator.start();

    }
}