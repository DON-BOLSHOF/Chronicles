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
    private long _mLastClickTime = 0; // Нужно предотвратить двойное нажатие кнопки

    private Animation _animation;

    private ImageView _eventImage;
    private TextView _eventTitle;
    private LinearLayout _eventButtonLayout;
    private Button _buttons[];
    private TextView _eventText;

    private TextView _characterMoney;
    private TextView _characterRelFather;
    private TextView _characterPopularity;

    private ScrollView _textScrollView;

    private View _view;

    private final AddMusic _eventMusic;

    private ImageButton _preffButton;

    private final UpdateNavigationParams mUpdateNavigationParams;

    public FragmentSceneManager(UpdateNavigationParams mUpdateNavigationParams, AddMusic addMusic){
        this.mUpdateNavigationParams = mUpdateNavigationParams;
        _eventMusic = addMusic;
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
        SetTitle(scene.getTitleName());
        SetImage(scene.getImageName());
        SetEventText(scene.getEventText());
        SetButtons(scene.getButtons());
        if(scene.isHasAddEvent()) {
            if (scene.getTypeAddEvent().equals("ReactEvent"))
                SetAdditionalEvent(scene.getAddEvent());
            else
                SetNoteEvent(scene.getNoteEvent());
        }
        if(scene.getAddMusicName() != null)
            EventMusic(scene.getAddMusicName());
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
        for (int i = 0; i<MainActivity.chapterEvents.size(); i++){
            Event temp = MainActivity.chapterEvents.get(i);
            if(temp.getTitleName().equals(eventName)) {
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
        _animation = AnimationUtils.loadAnimation(requireActivity(), R.anim.alphadel);
        _view.startAnimation(_animation);
    }

    private void ViewCreating(){
        _animation = AnimationUtils.loadAnimation(requireActivity(), R.anim.alphacreate);
        _view.startAnimation(_animation);
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
        this._buttons = new Button[numbers];
        for(int i=0;i<numbers;i++){
            this._buttons[i] = new Button(requireActivity());
            this._buttons[i].setText(buttons[i].getName());

            int finalI = i; //Android studio предложил это решение

            this._buttons[i].setOnClickListener(v -> {
                if (SystemClock.elapsedRealtime() - _mLastClickTime < 1550){
                    return;
                }
                _mLastClickTime = SystemClock.elapsedRealtime();

                Fogging();

                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    SetEventReaction(buttons[finalI]);
                }, 1500);
          });
        }

        for(int i=0; i<numbers; i++){
            SetCreating(this._buttons[i]);
        }

    }

    private void SetContinueButton(){
        Button tempButton = new Button(requireActivity());
        tempButton.setText("Продолжить");

        SetCreating(tempButton);

        tempButton.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - _mLastClickTime < 1550){
                return;
            }
            _mLastClickTime = SystemClock.elapsedRealtime();

            _eventMusic.StopAddMusic();

            _eventButtonLayout.removeView(tempButton);
            ViewFogging();

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                String tempEvent = MainActivity.currentEvent.getNameEventToSet();
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
        _animation = AnimationUtils.loadAnimation(requireActivity(), R.anim.alphadel);
        SetAnimation(_animation);

        for (Button button : _buttons) {
            button.startAnimation(_animation);
        }
    }

    private void ScrollViewUP(ScrollView mScrollView){
        mScrollView.fullScroll(View.FOCUS_UP);
    }

    private void SetCreating(Button button){
        _eventButtonLayout.addView(button);

        _animation = AnimationUtils.loadAnimation(requireActivity(), R.anim.alphacreate);

        ScrollViewUP(_textScrollView);

        SetAnimation(_animation);
        button.startAnimation(_animation);
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

        for (Button value : _buttons) {
            _eventButtonLayout.removeView(value);
        }

        if(button.getReactionEventButtons() == null)
            SetContinueButton();

        CustomButton[] reaction = null;

        if(button.getToCheck() != null) {
            if (HasPassTheRoll(button.getToCheck(), new Comparison(new PositiveCompare()))) {

                buttonReact = button.getReaction()[0];

                if (MainActivity.currentEvent.getEventType().equals("StoryEvent"))
                    MainActivity.currentEvent.setLoop(false);

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
        int value = _rand.nextInt(MainActivity.chapterEvents.size());
        int current = MainActivity.chapterEvents.indexOf(MainActivity.currentEvent);

        while (current == value  || !CheckAdditionalEvent(MainActivity.chapterEvents.get(value)) || !CheckFrequency(MainActivity.chapterEvents.get(value)))
            value = _rand.nextInt(MainActivity.chapterEvents.size());


        MainActivity.currentEvent = MainActivity.chapterEvents.get(value);
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
        AdditionalEventFragment additionalEventFragment = new AdditionalEventFragment(this, addEvent);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.AddEvent, additionalEventFragment)
                .addToBackStack(null)
                .commit();
    }

    private void SetNoteEventFragment(NoteEvent noteEvent){
        NoteEventFragment noteEventFragment = new NoteEventFragment(this, noteEvent);
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
        _animation = AnimationUtils.loadAnimation(requireActivity(), R.anim.alphablackoutcreate);
        ActionActivity.blackout.startAnimation(_animation);
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
                        int finalValue = Math.max(MainActivity.character.getMoney() + Integer.parseInt(changeStat[1]), 0); //Отрицательных денег не должно быть
                        AnimateNumberScroll(MainActivity.character.getMoney(), finalValue, _characterMoney);

                        MainActivity.character.boostMoney(Integer.parseInt(changeStat[1]));
                        mUpdateNavigationParams.UpdateParam("Money", MainActivity.character.getMoney());
                        break;
                    }
                    case "FatherRelations": {
                        AnimateNumberScroll(MainActivity.character.getFatherRel(), MainActivity.character.getFatherRel() + Integer.parseInt(changeStat[1]), _characterRelFather);

                        MainActivity.character.boostFatherRel(Integer.parseInt(changeStat[1]));
                        mUpdateNavigationParams.UpdateParam("FatherRelations", MainActivity.character.getFatherRel());
                        break;
                    }
                    case "Popularity": {
                        AnimateNumberScroll(MainActivity.character.getPopularity(), MainActivity.character.getPopularity() + Integer.parseInt(changeStat[1]), _characterPopularity);

                        MainActivity.character.boosPopularity(Integer.parseInt(changeStat[1]));
                        mUpdateNavigationParams.UpdateParam("Popularity", MainActivity.character.getPopularity());
                        break;
                    }
                    case "FightingSkill": {
                        MainActivity.character.boostFightSkill((Integer.parseInt(changeStat[1])));
                        mUpdateNavigationParams.UpdateParam("FightingSkill", MainActivity.character.getFightSkill());
                        break;
                    }
                    case "ShopLvl": {
                        MainActivity.character.boostShopLvl((Integer.parseInt(changeStat[1])));
                        if(Integer.parseInt(changeStat[1]) == 0) { //Самый худший костыль, из всех, но в силу архитектуры чтения json-файла придется использовать этo
                            AnimateNumberScroll(MainActivity.character.getMoney(), MainActivity.character.getMoney() + MainActivity.character.getShopLvl() * 2, _characterMoney);
                            MainActivity.character.boostMoney(MainActivity.character.getShopLvl() * 2);
                            mUpdateNavigationParams.UpdateParam("Money", MainActivity.character.getMoney());
                        }
                        mUpdateNavigationParams.UpdateParam("ShopLvl", MainActivity.character.getShopLvl());
                        break;
                    }
                    case "HasEquip": {
                        MainActivity.character.setHasEquip(Boolean.parseBoolean(changeStat[1]));
                        mUpdateNavigationParams.UpdateParam("HasEquip", MainActivity.character.isHasEquip());
                        break;
                    }
                    case "HasHorse": {
                        MainActivity.character.setHasHorse(Boolean.parseBoolean(changeStat[1]));
                        mUpdateNavigationParams.UpdateParam("HasHorse", MainActivity.character.isHasHorse());
                        break;
                    }
                }
            }
        }
    }

    private void CheckLoop(Event event){
        if(event.isLoop())
            RemoveEvent(event);
    }

    private void RemoveEvent(Event event){
        MainActivity.chapterEvents.remove(event);
    }

    private boolean CheckAdditionalEvent(Event testEvent){
        if(testEvent.getAddEvent() == null) //Нет addEvent значит ничего проверять не надо
            return true;
        else{
            String[][] toCheck = testEvent.getAddEvent().getCheck();
            String type = testEvent.getAddEvent().getType();

            if(type.equals("Positive"))
                return HasPassTheRoll(toCheck, new Comparison(new PositiveCompare())); //Если не прошел ивент будет вызываться
            else
                return HasPassTheRoll(toCheck, new Comparison(new NegativeCompare()));
        }
    }

////////////////////////Rolling/////////////////////////////////////////////////////////////////////////

    private boolean HasPassTheRoll(String[][] _reactions, Comparison compare){ //Как я понял нет делегатов в Java(Я хотел передать проверку как ивент в C#)
       boolean hasPass = true;
       int rollValue = 0;

        for (String[] reaction : _reactions) {
            String temp = reaction[0];
            switch (temp) {
                case "Money": {
                    int paramsToCheck = Integer.parseInt(reaction[1]);
                    int myParams = MainActivity.character.getMoney();
                    hasPass = compare.Compare(myParams,paramsToCheck);
                    break;
                }
                case "Popularity": {
                    int paramsToCheck = Integer.parseInt(reaction[1]);
                    int myParams = MainActivity.character.getPopularity(); // Добавим щепотку реализма в роли случайности, условное подбрасывание кубика
                    hasPass = compare.Compare(myParams,paramsToCheck);
                    break;
                }
                case "FatherRelations": {
                    int paramsToCheck = Integer.parseInt(reaction[1]);
                    int myParams = MainActivity.character.getFatherRel();
                    hasPass = compare.Compare(myParams,paramsToCheck);
                    break;
                }
                case "FightingSkill": {
                    int paramsToCheck = Integer.parseInt(reaction[1]);
                    int myParams = MainActivity.character.getFightSkill();
                    hasPass = compare.Compare(myParams,paramsToCheck);
                    break;
                }
                case "ShopLvl": {
                    int paramsToCheck = Integer.parseInt(reaction[1]);
                    int myParams = MainActivity.character.getShopLvl();
                    hasPass = compare.Compare(myParams,paramsToCheck);
                    break;
                }
                case "HasEquip": {
                    int myParams = MainActivity.character.isHasEquip() ? 1 : 0;
                    int paramsToCheck = Boolean.parseBoolean(reaction[1])? 1 : 0;
                    hasPass = compare.Compare(myParams,paramsToCheck);
                    break;
                }
                case "HasHorse": {
                    int myParams = MainActivity.character.isHasHorse() ? 1 : 0;
                    int paramsToCheck = Boolean.parseBoolean(reaction[1])? 1 : 0;
                    hasPass = compare.Compare(myParams,paramsToCheck);
                    break;
                }
                case "RandomEvent": {
                    int paramsToCheck = Integer.parseInt(reaction[1]);
                    hasPass = compare.Compare(CubeRoll(),paramsToCheck);
                    break;
                }
                case "RandomValue": {
                    rollValue = CubeRoll();
                    SetRoll();
                    SetDiceRollFragment(rollValue,_reactions);
                    break;
                }
            }
        }

       return hasPass;
    }

    interface Compare{
        boolean IsSurpass(int a1, int a2);
    }

    private class PositiveCompare implements Compare{

        @Override
        public boolean IsSurpass(int a1, int a2) {
            return a1>=a2;
        }
    }

    private class NegativeCompare implements Compare{

        @Override
        public boolean IsSurpass(int a1, int a2) {
            return a1<a2;
        }
    }

    private class Comparison{
        Compare compare;

        public Comparison(Compare compare){
            this.compare = compare;
        }

        public void setCompare(Compare compare){
            this.compare = compare;
        }

        public boolean Compare(int a1, int a2){
            return compare.IsSurpass(a1, a2);
        }
    }

    private int CubeRoll(){ // Логичнее было разделить из-за абсолютно разного функционала
        Random rand = new Random();
        return  rand.nextInt(20) ;
    }

    private int FrequencyRoll(){
        Random rand = new Random();
        return  rand.nextInt(6);
    }

    private boolean CheckFrequency(Event mEvent){
        if(mEvent.isLoop()) //Если нет frequency
            return true;

        int frequency = mEvent.getFrequency();

        return FrequencyRoll()>=Math.abs(frequency-6); //Если частота меньше то шанс ролла меньше должен быть
    }
////////////////////////////Animations///////////////////////////////////////////////////////////
    private void AnimateNumberScroll(int initialValue, int finalValue, final TextView textview) {

      ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue, finalValue);
      valueAnimator.setDuration(1000);

      valueAnimator.addUpdateListener(valueAnimator1 -> textview.setText(valueAnimator1.getAnimatedValue().toString()));
      valueAnimator.start();
    }

///////////////////////////UpdateNavigationParams///////////////////////////////////////////////
    public interface UpdateNavigationParams{
        void UpdateParam(String name, int value);
        void UpdateParam(String name, boolean value);
    }
}